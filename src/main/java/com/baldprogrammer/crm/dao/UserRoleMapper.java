package com.baldprogrammer.crm.dao;

import com.baldprogrammer.crm.base.BaseMapper;
import com.baldprogrammer.crm.vo.UserRole;

public interface UserRoleMapper extends BaseMapper<UserRole, Integer> {

    //根据用户Id查询用户角色记录
    Integer countUserRoleByUserId(Integer userId);

    //根据用户Id删除用户角色记录
    Integer deleteUserRoleByUserId(Integer userId);
}