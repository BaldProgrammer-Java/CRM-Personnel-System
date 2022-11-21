package com.baldprogrammer.crm.service;

import com.baldprogrammer.crm.base.BaseService;
import com.baldprogrammer.crm.dao.CusDevPlanMapper;
import com.baldprogrammer.crm.dao.SaleChanceMapper;
import com.baldprogrammer.crm.query.CusDevPlanQuery;
import com.baldprogrammer.crm.utils.AssertUtil;
import com.baldprogrammer.crm.vo.CusDevPlan;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @PROJECT_NAME: CRM
 * @DESCRIPTION:
 * @USER: baldprogrammer
 * @DATE: 2022/11/17 7:31 PM
 */
@Service
public class CusDevPlanService extends BaseService<CusDevPlan, Integer> {

    @Resource
    private CusDevPlanMapper cusDevPlanMapper;

    @Resource
    private SaleChanceMapper saleChanceMapper;

    /**
     * 多条件查询客户啊开发计划
     *
     * @param cusDevPlanQuery
     * @return
     */
    public Map<String, Object> queryCusDevPlanByParams(CusDevPlanQuery cusDevPlanQuery) {

        Map<String, Object> map = new HashMap<>();
        //开启分页
        PageHelper.startPage(cusDevPlanQuery.getPage(), cusDevPlanQuery.getLimit());
        //得到对应的分页对象
        PageInfo<CusDevPlan> pageInfo = new PageInfo<>(cusDevPlanMapper.selectByParams(cusDevPlanQuery));
        //设置Map对象
        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        //设置分页好的列表
        map.put("data", pageInfo.getList());

        return map;
    }

    /**
     * 添加客户开发计划
     *
     * @param cusDevPlan
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCusDevPlan(CusDevPlan cusDevPlan) {
        //参数校验
        checkCusDevPlanParams(cusDevPlan);

        //设置默认值
        //设置是否有效
        cusDevPlan.setIsValid(1);
        //设置创建时间
        cusDevPlan.setCreateDate(new Date());
        //设置提交时间
        cusDevPlan.setUpdateDate(new Date());

        AssertUtil.isTrue(cusDevPlanMapper.insertSelective(cusDevPlan) != 1,"计划项数据添加失败");
    }

    /**
     * 更新用户开发计划项数据
     *
     * @param cusDevPlan
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCusDevPlan(CusDevPlan cusDevPlan) {
        //参数校验
        AssertUtil.isTrue(null == cusDevPlan.getId()
                || cusDevPlanMapper.selectByPrimaryKey(cusDevPlan.getId()) == null, "数据异常，请重试！");
        checkCusDevPlanParams(cusDevPlan);

        //设置参数的默认值
        //修改时间  系统当前时间
        cusDevPlan.setUpdateDate(new Date());

        //执行更新操作，判断受影响的行数
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan) != 1, "更新计划项失败！");


    }


    /**
     *参数校验
     *
     * @param cusDevPlan
     */
    private void checkCusDevPlanParams(CusDevPlan cusDevPlan) {
        //营销机会ID
        Integer sId = cusDevPlan.getSaleChanceId();
        AssertUtil.isTrue(null == sId || saleChanceMapper.selectByPrimaryKey(sId) == null,"数据异常，请重试");

        //计划项内容
        AssertUtil.isTrue(StringUtils.isBlank(cusDevPlan.getPlanItem()),"计划项内容不能为空！");

        //计划时间
        AssertUtil.isTrue(null == cusDevPlan.getPlanDate(),"计划时间不能为空！");
    }


    /**
     * 删除计划项
     *
     * @param id
     */
    public void deleteCusDevPlan(Integer id) {
        //判断ID是否为空，且数据存在
        AssertUtil.isTrue(null == id, "待删除记录不存在！");
        //通过ID查询计划项对象
        CusDevPlan cusDevPlan = cusDevPlanMapper.selectByPrimaryKey(id);
        //设置记录无效 (删除)
        cusDevPlan.setIsValid(0);
        cusDevPlan.setUpdateDate(new Date());
        //执行更新操作
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan) !=1, "计划项数据删除失败！");
    }
}
