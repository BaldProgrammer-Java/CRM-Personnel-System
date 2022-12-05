package com.baldprogrammer.crm.service;

import com.baldprogrammer.crm.base.BaseService;
import com.baldprogrammer.crm.dao.RoleMapper;
import com.baldprogrammer.crm.utils.AssertUtil;
import com.baldprogrammer.crm.vo.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.awt.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @PROJECT_NAME: CRM
 * @DESCRIPTION:
 * @USER: baldprogrammer
 * @DATE: 2022/12/5 3:23 PM
 */
@Service
public class RoleSerivce extends BaseService<Role, Integer> {

    @Resource
    private RoleMapper roleMapper;


    /**
     * 调用Mapper层查询方法查询角色数据
     *
     * @param userId
     * @return
     */
    public List<Map<String, Object>> queryAllRoles(Integer userId) {
        return roleMapper.queryAllRoles(userId);
    }


    /**
     *
     *
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addRole(Role role) {
        //判断角色名字
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"角色名称不能为空！");
        //通过用户角色查询角色记录
        Role temp = roleMapper.selectByRoleName(role.getRoleName());
        //判断用户记录是否存在  如果存在则提升用户已经存在
        AssertUtil.isTrue(temp != null, "角色名称已存在，请重新输入！");

        //设置默认参数
        role.setIsValid(1); //设置状态
        role.setCreateDate(new Date());  //设置创建时间
        role.setUpdateDate(new Date());  //设置更新时间


        //执行更新操作  判断受影响行数
        AssertUtil.isTrue(roleMapper.insertSelective(role) < 1, "角色添加失败！");
    }




}
