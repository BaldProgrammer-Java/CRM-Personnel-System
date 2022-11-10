package com.baldprogrammer.crm.controller;

import com.baldprogrammer.crm.base.BaseController;
import com.baldprogrammer.crm.base.ResultInfo;
import com.baldprogrammer.crm.enums.StateStatus;
import com.baldprogrammer.crm.query.SaleChanceQuery;
import com.baldprogrammer.crm.service.SaleChanceService;
import com.baldprogrammer.crm.utils.CookieUtil;
import com.baldprogrammer.crm.utils.LoginUserUtil;
import com.baldprogrammer.crm.vo.SaleChance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @PROJECT_NAME: CRM
 * @DESCRIPTION:
 * @USER: baldprogrammer
 * @DATE: 2022/10/27 3:32 PM
 */
@Controller
@RequestMapping("/sale_chance")
public class SaleChanceController extends BaseController {

    @Autowired
    private SaleChanceService saleChanceService;

    /**
     * 营销机会数据查询 (分页多条件查询)
     *
     * @param saleChanceQuery
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String, Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery, Integer flag, HttpServletRequest request) {
        //判断flag的值
        if (flag != null && flag == 1) {
            //客户开发计划  设置分配状态
            saleChanceQuery.setState(StateStatus.STATED.getType());
            //设置指派人  当前用户的ID
            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
            saleChanceQuery.setAssignMan(userId);
        }
        return saleChanceService.querySaleChanceByParams(saleChanceQuery);
    }

    /**
     * 进入营销机会管理视图
     *
     * @return
     */
    @RequestMapping("/index")
    public String index() {
        return "saleChance/sale_chance";
    }

    /**
     * 添加营销机会
     *
     * @param saleChance
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public ResultInfo addSaleChance(SaleChance saleChance, HttpServletRequest request) {
        //从Cookie中获取当前登陆的用户名
        String userName = CookieUtil.getCookieValue(request, "userName");
        //设置用户名到营销机会对象
        saleChance.setCreateMan(userName);
        //调用Service层的添加方法
        saleChanceService.addSaleChance(saleChance);
        return success("营销机会数据添加成功！");
    }

    /**
     * 更新营销机会
     *
     * @param saleChance
     * @return
     */
    @PostMapping("/update")
    @ResponseBody
    public ResultInfo updateSaleChance(SaleChance saleChance) {
        //调用Service层的修改方法
        saleChanceService.updateSaleChance(saleChance);
        return success("营销机会数据更新成功！");
    }

    /**
     * 进入添加/修改营销机会数据页面
     *
     * @param saleChanceId
     * @param request
     * @return
     */
    @RequestMapping("/toSaleChancePage")
    public String toSaleChancePage(Integer saleChanceId, HttpServletRequest request) {

        // 判断saleChanceId是否为空
        if (saleChanceId != null) {
            // 通过ID查询营销机会数据
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(saleChanceId);
            //  将数据设置到请求中
            request.setAttribute("saleChance",saleChance);

        }

        return "saleChance/add_update";
    }

    /**
     * 删除营销机会
     * @param ids
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public ResultInfo deleteSaleChance(Integer[] ids) {
        //调用service层的删除方法
        saleChanceService.deleteBatch(ids);
        return success("营销机会数据删除成功！");
    }

}
