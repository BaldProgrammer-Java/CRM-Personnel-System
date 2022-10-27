package com.baldprogrammer.crm.controller;

import com.baldprogrammer.crm.base.BaseController;
import com.baldprogrammer.crm.base.ResultInfo;
import com.baldprogrammer.crm.exceptions.ParamsException;
import com.baldprogrammer.crm.model.UserModel;
import com.baldprogrammer.crm.service.UserService;
import com.baldprogrammer.crm.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    /**
     * 用户登陆
     *
     * @param userName
     * @param userPwd
     * @return resultInfo
     */
    @PostMapping("/login")
    @ResponseBody //表示返回Json格式
    public ResultInfo userLogin(String userName, String userPwd) {
        ResultInfo resultInfo = new ResultInfo();

        //调用service的登陆方法
        UserModel userModel = userService.userLogin(userName, userPwd);
        //设置resultInfo的result值
        resultInfo.setResult(userModel);


        //通过异常处理 捕获service的异常 如果service抛出异常  则表示登陆失败  否则登陆成功
      /*  try {
            //调用service的登陆方法
            UserModel userModel = userService.userLogin(userName, userPwd);
            //设置resultInfo的result值
            resultInfo.setResult(userModel);
        } catch (ParamsException p) {
            //抓取service异常并返回
            resultInfo.setCode(p.getCode());
            resultInfo.setMsg(p.getMsg());
            p.printStackTrace();
        } catch (Exception e) {
            //抓取普通异常并返回
            resultInfo.setCode(500);
            resultInfo.setMsg("登陆失败！");
        }*/
        return resultInfo;
    }

    /**
     * 用户修改密码
     *
     * @param request
     * @param oldPassword
     * @param newPassword
     * @param repeatPassword
     * @return
     */
    @PostMapping("/updatePwd")
    @ResponseBody
    public ResultInfo updateUserPassword(HttpServletRequest request, String oldPassword, String newPassword, String repeatPassword) {
        ResultInfo resultInfo = new ResultInfo();

        //获取cookie中的userId
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        //调用Service层修改密码方法
        userService.updatePassword(userId, oldPassword, newPassword, repeatPassword);

 /*       try {
            //获取cookie中的userId
            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
            //调用Service层修改密码方法
            userService.updatePassword(userId, oldPassword, newPassword, repeatPassword);
        } catch (ParamsException p) {
            resultInfo.setCode(p.getCode());
            resultInfo.setMsg(p.getMsg());
            p.printStackTrace();
        } catch (Exception e) {
            resultInfo.setCode(500);
            resultInfo.setMsg("修改密码失败！！！");
            e.printStackTrace();
        }*/
        return resultInfo;
    }

    /**
     * 进入修改密码页面
     *
     * @return
     */
    @RequestMapping("toPasswordPage")
    public String toPasswordPage() {
        return "user/password";
    }

}
