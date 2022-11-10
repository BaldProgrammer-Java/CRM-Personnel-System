package com.baldprogrammer.crm.controller;

import com.baldprogrammer.crm.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @PROJECT_NAME: CRM
 * @DESCRIPTION:
 * @USER: baldprogrammer
 * @DATE: 2022/11/10 3:55 PM
 */
@RequestMapping("/cus_dev_plan")
@Controller
public class CusDevPlanController extends BaseController {

    /**
     * 进入客户开发计划页面
     *
     * @return
     */
    @RequestMapping("/index")
    public String index() {
        return "cusDevPlan/cus_dev_plan";
    }

}
