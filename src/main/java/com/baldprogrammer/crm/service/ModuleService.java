package com.baldprogrammer.crm.service;

import com.baldprogrammer.crm.base.BaseService;
import com.baldprogrammer.crm.dao.ModuleMapper;
import com.baldprogrammer.crm.dao.PermissionMapper;
import com.baldprogrammer.crm.model.TreeModel;
import com.baldprogrammer.crm.vo.Module;
import com.baldprogrammer.crm.vo.Permission;
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

    @Resource
    private PermissionMapper permissionMapper;

    /**
     * 查询所有资源列表
     *
     * @return
     */
    public List<TreeModel> queryAllModules(Integer roleId) {
        List<TreeModel> treeModelLIst = moduleMapper.queryAllModules();
        List<Permission> permissionIds = permissionMapper.queryRoleHasModuleIdsByRoleId(roleId);
        if (permissionIds != null && permissionIds.size() > 0) {
            treeModelLIst.forEach(treeModel -> {
                if (permissionIds.contains(treeModel.getId())) {
                    treeModel.setChecked(true);
                }
            });
        }
        return treeModelLIst;
    }

}
