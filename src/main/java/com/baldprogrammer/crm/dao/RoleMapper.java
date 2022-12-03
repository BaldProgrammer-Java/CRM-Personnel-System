package com.baldprogrammer.crm.dao;


import com.baldprogrammer.crm.base.BaseMapper;
import com.baldprogrammer.crm.vo.Role;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role, Integer> {

    //查询所有角色列表
    public List<Map<String, Object>> queryAllRoles(Integer userId);

}