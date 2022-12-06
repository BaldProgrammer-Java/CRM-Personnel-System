package com.baldprogrammer.crm.service;

import com.baldprogrammer.crm.base.BaseService;
import com.baldprogrammer.crm.dao.RoleMapper;
import com.baldprogrammer.crm.utils.AssertUtil;
import com.baldprogrammer.crm.vo.Role;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
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


    /**
     * 修改角色信息
     *
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateRole(Role role) {
        AssertUtil.isTrue(null == role.getId(), "待更新记录不存在！");
        Role temp = roleMapper.selectByPrimaryKey(role.getId());
        AssertUtil.isTrue(null == temp, "待更新记录不存在");
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()), "角色名称不能为空！");
        temp = roleMapper.selectByRoleName(role.getRoleName());
        AssertUtil.isTrue(null !=temp && (!temp.getId().equals(role.getId())),"角色名称已经存在，不可使用！");
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role) < 1, "修改角色信息失败！");
    }


    /**
     * 删除角色信息
     *
     * @param roleId
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteRole(Integer roleId) {
        //判断ID是否为空
        AssertUtil.isTrue(null == roleId, "待删除记录不存在！");
        //通过角色Id查询角色记录
        Role role = roleMapper.selectByPrimaryKey(roleId);
        //判断角色记录是否存在
        AssertUtil.isTrue(null == role, "待删除记录不存在！");
        //设置删除状态
        role.setIsValid(0);
        role.setUpdateDate(new Date());
        //执行更新操作
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role) < 1, "角色删除失败！");
    }
}
