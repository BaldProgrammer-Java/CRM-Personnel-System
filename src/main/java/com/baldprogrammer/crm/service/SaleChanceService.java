package com.baldprogrammer.crm.service;

import com.baldprogrammer.crm.base.BaseService;
import com.baldprogrammer.crm.dao.SaleChanceMapper;
import com.baldprogrammer.crm.enums.DevResult;
import com.baldprogrammer.crm.enums.StateStatus;
import com.baldprogrammer.crm.query.SaleChanceQuery;
import com.baldprogrammer.crm.utils.AssertUtil;
import com.baldprogrammer.crm.utils.PhoneUtil;
import com.baldprogrammer.crm.vo.SaleChance;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @PROJECT_NAME: CRM
 * @DESCRIPTION:
 * @USER: baldprogrammer
 * @DATE: 2022/10/27 3:28 PM
 */
@Service
public class SaleChanceService extends BaseService<SaleChance, Integer> {

    @Autowired
    private SaleChanceMapper saleChanceMapper;

    /**
     * 多条件分页查询营销机会  (return Map)
     *
     * @param saleChanceQuery
     * @return
     */
    public Map<String, Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery) {

        Map<String, Object> map = new HashMap<>();
        //开启分页
        PageHelper.startPage(saleChanceQuery.getPage(), saleChanceQuery.getLimit());
        //得到对应的分页对象
        PageInfo<SaleChance> pageInfo = new PageInfo<>(saleChanceMapper.selectByParams(saleChanceQuery));
        //设置Map对象
        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        //设置分页好的列表
        map.put("data", pageInfo.getList());

        return map;
    }

    /**
     * 添加营销机会
     *
     * @param saleChance
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addSaleChance(SaleChance saleChance) {
        //校验参数
        checkSaleChanceParams(saleChance.getCustomerName(), saleChance.getLinkMan(), saleChance.getLinkPhone());

        //设置相关字段默认值
        saleChance.setIsValid(1);
        saleChance.setCreateDate(new Date());
        saleChance.setUpdateDate(new Date());
        if (StringUtils.isBlank(saleChance.getAssignMan())) {
            //已经设置指派人       枚举对象
            saleChance.setState(StateStatus.UNSTATE.getType());
            //设置指派时间  null
            saleChance.setAssignTime(null);
            //设置开发状态
            saleChance.setDevResult(DevResult.UNDEV.getStatus());
        } else {
            //分配状态
            saleChance.setState(StateStatus.STATED.getType());
            //设置指派时间
            saleChance.setAssignTime(new Date());
            //设置开发状态
            saleChance.setDevResult(DevResult.DEVING.getStatus());
        }
        //执行添加操作  判断受影响行数
        AssertUtil.isTrue(saleChanceMapper.insertSelective(saleChance) != 1, "添加营销失败！");
    }

    private void checkSaleChanceParams(String CustomerName, String LinkMan, String LinkPhone) {
        //customerName客户名称  非空
        AssertUtil.isTrue(StringUtils.isBlank(CustomerName), "客户名称不能为空！");
        //linkMan联系人         非空
        AssertUtil.isTrue(StringUtils.isBlank(LinkMan), "联系人不能为空！");
        //linkPhone联系号码     非空  手机号码格式正确
        AssertUtil.isTrue(StringUtils.isBlank(LinkPhone), "联系号码不能为空！");
        AssertUtil.isTrue(!PhoneUtil.isMobile(LinkPhone), "联系号码格式不正确！");
    }


}
