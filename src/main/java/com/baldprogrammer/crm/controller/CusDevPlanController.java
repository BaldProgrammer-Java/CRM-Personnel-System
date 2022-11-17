package com.baldprogrammer.crm.controller;

import com.baldprogrammer.crm.base.BaseController;
import com.baldprogrammer.crm.base.ResultInfo;
import com.baldprogrammer.crm.query.CusDevPlanQuery;
import com.baldprogrammer.crm.service.CusDevPlanService;
import com.baldprogrammer.crm.service.SaleChanceService;
import com.baldprogrammer.crm.vo.CusDevPlan;
import com.baldprogrammer.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @PROJECT_NAME: CRM
 * @DESCRIPTION:
 * @USER: baldprogrammer
 * @DATE: 2022/11/10 3:55 PM
 */
@RequestMapping("/cus_dev_plan")
@Controller
public class CusDevPlanController extends BaseController {

    @Resource
    private SaleChanceService saleChanceService;

    @Resource
    private CusDevPlanService cusDevPlanService;

    /**
     * 进入客户开发计划页面
     *
     * @return
     */
    @RequestMapping("/index")
    public String index() {
        return "/cusDevPlan/cus_dev_plan";
    }

    /**
     * 打开计划项开发与详情页面
     *
     * @param id
     * @return
     */
    @RequestMapping("/toCusDevPlanPage")
    public String toCusDevPlanPage(Integer id, HttpServletRequest request) {
        //通过id查询营销机会对象
        SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
        //将对象设置到请求域
        request.setAttribute("saleChance", saleChance);
        return "/cusDevPlan/cus_dev_plan_data";
    }


    /**
     * 客户开发计划数据查询 (分页多条件查询)
     *
     * @param cusDevPlanQuery
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String, Object> queryCusDevPlanByParams(CusDevPlanQuery cusDevPlanQuery) {

        return cusDevPlanService.queryCusDevPlanByParams(cusDevPlanQuery);
    }

    /**
     * 添加计划项
     *
     * @param cusDevPlan
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public ResultInfo addCusDevPlan(CusDevPlan cusDevPlan) {
        cusDevPlanService.addCusDevPlan(cusDevPlan);
        return success("计划项添加成功！");
    }

    /**
     * 进入添加或修改计划项页面
     *
     * @return
     */
    @RequestMapping("/toAddOrUpdateCusDevPlanPage")
    public String toAddOrUpdateCusDevPlanPage() {
        return "/cusDevPlan/add_update";
    }

}
