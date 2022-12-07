package com.baldprogrammer.crm.service;

import com.baldprogrammer.crm.base.BaseService;
import com.baldprogrammer.crm.dao.ModuleMapper;
import com.baldprogrammer.crm.model.TreeModel;
import com.baldprogrammer.crm.vo.Module;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @PROJECT_NAME: CRM-Personnel-System
 * @DESCRIPTION:
 * @USER: baldprogrammer
 * @DATE: 2022/12/6 10:44 AM
 */
@Service
public class ModuleService extends BaseService<Module, Integer> {

    @Resource
    private ModuleMapper moduleMapper;

    /**
     * 查询所有资源列表
     *
     * @return
     */
    public List<TreeModel> queryAllModules() {
        List<TreeModel> treeModelLIst =  moduleMapper.queryAllModules();
        return treeModelLIst;
    }

}
