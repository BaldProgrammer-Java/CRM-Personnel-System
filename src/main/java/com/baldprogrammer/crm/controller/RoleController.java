package com.baldprogrammer.crm.controller;

import com.baldprogrammer.crm.base.BaseController;
import com.baldprogrammer.crm.base.ResultInfo;
import com.baldprogrammer.crm.query.RoleQuery;
import com.baldprogrammer.crm.service.RoleSerivce;
import com.baldprogrammer.crm.vo.Role;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @PROJECT_NAME: CRM
 * @DESCRIPTION:
 * @USER: baldprogrammer
 * @DATE: 2022/12/5 3:24 PM
 */
@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

    @Resource
    private RoleSerivce roleSerivce;


    /**
     * 查询所有角色列表
     *
     * @param userId
     * @return
     */
    @RequestMapping("/queryAllRoles")
    @ResponseBody
    public List<Map<String, Object>> queryAllRoles(Integer userId) {
        return roleSerivce.queryAllRoles(userId);
    }

    /**
     * 分页条件查询角色列表
     *
     * @param roleQuery
     * @return
     */
    @GetMapping("/list")
    @ResponseBody
    public Map<String, Object> selectByParams(RoleQuery roleQuery) {
        return roleSerivce.queryByParamsForTable(roleQuery);
    }


    /**
     * 角色视图跳转
     *
     * @return
     */
    @RequestMapping("/index")
    public String index() {
        return "/role/role";
    }


    @PostMapping("/add")
    @ResponseBody
    public ResultInfo addRole(Role role) {
        roleSerivce.addRole(role);
        return success("角色添加成功！");
    }

    @RequestMapping("/toAddOrUpdateRolePage")
    public String toAddOrUpdateRolePage() {
        return "/role/add_update";
    }


}
