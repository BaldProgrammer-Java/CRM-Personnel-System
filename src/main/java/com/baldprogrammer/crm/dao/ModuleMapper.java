package com.baldprogrammer.crm.dao;

import com.baldprogrammer.crm.base.BaseMapper;
import com.baldprogrammer.crm.model.TreeModel;
import com.baldprogrammer.crm.vo.Module;

import java.util.List;

public interface ModuleMapper extends BaseMapper<Module, Integer> {

    // 查询所有的资源列表
    public List<TreeModel> queryAllModules();

}