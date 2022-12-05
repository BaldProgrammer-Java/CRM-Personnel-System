package com.baldprogrammer.crm.controller;

import com.baldprogrammer.crm.base.BaseController;
import com.baldprogrammer.crm.base.ResultInfo;
import com.baldprogrammer.crm.query.RoleQuery;
import com.baldprogrammer.crm.service.RoleSerivce;
import com.baldprogrammer.crm.vo.Role;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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

    /**
     * 添加角色Api
     *
     * @param role
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public ResultInfo addRole(Role role) {
        roleSerivce.addRole(role);
        return success("角色添加成功！");
    }

    /**
     * 添加/修改角色信息视图跳转
     *
     * @return
     */
    @RequestMapping("/toAddOrUpdateRolePage")
    public String toAddOrUpdateRolePage(Integer roleId, HttpServletRequest request) {
        //如果roleId不为空，则表示修改操作，通过角色ID查询角色记录，存到请求域中
        if (roleId != null) {
            Role role = roleSerivce.selectByPrimaryKey(roleId);
            request.setAttribute("role", role);
        }
        return "/role/add_update";
    }


    /**
     * 修改角色信息Api
     *
     * @return
     */
    @PostMapping("/update")
    @ResponseBody
    public ResultInfo updateRole(Role role) {
        roleSerivce.updateRole(role);
        return success("角色修改成功！");
    }


}
