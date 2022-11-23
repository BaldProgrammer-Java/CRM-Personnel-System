package com.baldprogrammer.crm.dao;


import com.baldprogrammer.crm.base.BaseMapper;
import com.baldprogrammer.crm.vo.User;

import java.util.List;
import java.util.Map;


public interface UserMapper extends BaseMapper<User, Integer> {

    public User queryUserByName(String userName);

}