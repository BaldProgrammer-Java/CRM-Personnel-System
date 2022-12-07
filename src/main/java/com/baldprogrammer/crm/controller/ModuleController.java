package com.baldprogrammer.crm.controller;

import com.baldprogrammer.crm.base.BaseController;
import com.baldprogrammer.crm.model.TreeModel;
import com.baldprogrammer.crm.service.ModuleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @PROJECT_NAME: CRM-Personnel-System
 * @DESCRIPTION:
 * @USER: baldprogrammer
 * @DATE: 2022/12/6 10:44 AM
 */
@Controller
@RequestMapping("/module")
public class ModuleController extends BaseController {

    @Resource
    private ModuleService moduleService;


    /**
     * 查询所有资源列表
     *
     * @return
     */
    @RequestMapping("/queryAllModules")
    @ResponseBody
    public List<TreeModel> queryAllModules(Integer roleId) {

        return moduleService.queryAllModules(roleId);
    }

    /**
     * 进入授权页面
     *
     * @param roleId
     * @return
     */
    @RequestMapping("/toAddGrantPage")
    public String toAddGrantPage(Integer roleId) {
        return "/role/grant";
    }

}
