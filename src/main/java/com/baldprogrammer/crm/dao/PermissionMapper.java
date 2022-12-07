package com.baldprogrammer.crm.dao;

import com.baldprogrammer.crm.base.BaseMapper;
import com.baldprogrammer.crm.vo.Permission;

public interface PermissionMapper extends BaseMapper<Permission, Integer> {

    //通过角色ID查询权限记录
    Integer countPermissionByRoleId(Integer roleId);

    //通过角色ID删除权限记录
    void deletePermissionByRoleId(Integer roleId);



}