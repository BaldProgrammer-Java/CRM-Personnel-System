package com.baldprogrammer.crm.dao;


import com.baldprogrammer.crm.base.BaseMapper;
import com.baldprogrammer.crm.vo.User;


public interface UserMapper extends BaseMapper<User, Integer> {

    public User queryUserByName(String userName);

}