package com.baldprogrammer.crm.controller;

import com.baldprogrammer.crm.base.BaseController;
import com.baldprogrammer.crm.service.UserService;
import com.baldprogrammer.crm.utils.LoginUserUtil;
import com.baldprogrammer.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@Controller
public class IndexController extends BaseController {

    @Resource
    private UserService userService;

    /**
     * system index
     * @return
     */
    @RequestMapping("index")
    public String index() {
        return "index";
    }

    /**
     * system welcome
     * @return
     */
    @RequestMapping("welcome")
    public String welcome() {
        return "welcome";
    }

    /**
     * background management
     * @return
     */
    @RequestMapping("main")
    public String main(HttpServletRequest request) {

        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        User user = userService.selectByPrimaryKey(userId);
        request.getSession().setAttribute("user", user);
        return "main";
    }


}
