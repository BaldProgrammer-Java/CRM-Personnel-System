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
 * @DATE: 2022/10/27 3:28 PM
 */
@Service
public class SaleChanceService extends BaseService<SaleChance, Integer> {

    @Resource
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

    /**
     * 更新营销机会
     *
     * @param saleChance
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChance(SaleChance saleChance) {
        //参数校验
        AssertUtil.isTrue(null == saleChance.getId(), "待更新记录不存在！");
        //通过主键查询对象
        SaleChance temp = saleChanceMapper.selectByPrimaryKey(saleChance.getId());
        //判断数据库中对应的记录存在
        AssertUtil.isTrue(temp == null, "待更新记录不存在！");
        checkSaleChanceParams(saleChance.getCustomerName(), saleChance.getLinkMan(), saleChance.getLinkPhone());

        /*设置相关参数默认值*/
        saleChance.setUpdateDate(new Date());
        // assignMan指派人
        if (StringUtils.isBlank(temp.getAssignMan())) {
            //判断修改的信息是否存在
            if (!StringUtils.isBlank(saleChance.getAssignMan())) {
                saleChance.setAssignTime(new Date());
                saleChance.setState(StateStatus.STATED.getType());
                saleChance.setDevResult(DevResult.DEVING.getStatus());
            }
        } else {
            if (StringUtils.isBlank(saleChance.getAssignMan())) {
                saleChance.setAssignTime(null);
                saleChance.setState(StateStatus.UNSTATE.getType());
                saleChance.setDevResult(DevResult.UNDEV.getStatus());
            } else {
                if (saleChance.getAssignMan().equals(temp.getAssignMan())) {
                    saleChance.setAssignTime(new Date());
                }
            }
        }
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance) != 1, "更新营销机会失败！");
    }


    /**
     * 参数校验
     *
     * @param CustomerName
     * @param LinkMan
     * @param LinkPhone
     */
    private void checkSaleChanceParams(String CustomerName, String LinkMan, String LinkPhone) {
        //customerName客户名称  非空
        AssertUtil.isTrue(StringUtils.isBlank(CustomerName), "客户名称不能为空！");
        //linkMan联系人         非空
        AssertUtil.isTrue(StringUtils.isBlank(LinkMan), "联系人不能为空！");
        //linkPhone联系号码     非空  手机号码格式正确
        AssertUtil.isTrue(StringUtils.isBlank(LinkPhone), "联系号码不能为空！");
        AssertUtil.isTrue(!PhoneUtil.isMobile(LinkPhone), "联系号码格式不正确！");
    }


    /**
     * 删除营销机会
     *
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteSaleChance(Integer[] ids) {
        //判断id是否为空
        AssertUtil.isTrue(null == ids || ids.length < 1, "待删除记录不存在！");
        //执行删除(更新)操作 判断受影响的行数
        AssertUtil.isTrue(saleChanceMapper.deleteBatch(ids) != ids.length, "营销机会数据删除失败");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChanceDevResult(Integer id, Integer devResult) {
        //判断ID是否为空
        AssertUtil.isTrue(null == id, "待更新记录不存在！");
        //通过id查询营销机会数据
        SaleChance saleChance = saleChanceMapper.selectByPrimaryKey(id);
        //判断对象是否为空
        AssertUtil.isTrue(null == saleChance, "待更新记录不存在！");
        //设置开发状态
        saleChance.setDevResult(devResult);
        //执行更新操作，判断受影响的行数
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance) != 1, "开发状态更新失败！");
    }
}
