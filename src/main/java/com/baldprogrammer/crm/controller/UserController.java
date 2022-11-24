package com.baldprogrammer.crm.controller;

import com.baldprogrammer.crm.base.BaseController;
import com.baldprogrammer.crm.base.ResultInfo;
import com.baldprogrammer.crm.model.UserModel;
import com.baldprogrammer.crm.query.UserQuery;
import com.baldprogrammer.crm.service.UserService;
import com.baldprogrammer.crm.utils.LoginUserUtil;
import com.baldprogrammer.crm.vo.User;
import com.mysql.cj.jdbc.SuspendableXAConnection;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


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


    /**
     * 查询用户列表
     *
     * @param userQuery
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String, Object> selectByParams(UserQuery userQuery) {
        return userService.queryByParamsForTable(userQuery);
    }

    /**
     * 进入用户列表页面
     *
     * @return
     */
    @RequestMapping("/index")
    public String index() {
        return "/user/user";
    }


    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public ResultInfo addUser(User user) {
        userService.addUser(user);
        return success("用户添加成功！");
    }


    /**
     * 修改用户
     *
     * @param user
     * @return
     */
    @PostMapping("/update")
    @ResponseBody
    public ResultInfo updateUser(User user) {
        userService.updateUser(user);
        return success("修改用户数据成功！");
    }


    /**
     * 打开用户或修改用户页面
     *
     * @return
     */
    @RequestMapping("/toAddOrUpdateUserPage")
    public String toAddOrUpdateUserPage(Integer id, HttpServletRequest request) {
        //判断id是否为空，不为空则是更新操作，查询用户对象
        if (id != null) {
            //通过id查询用户对象
            User user = userService.selectByPrimaryKey(id);
            //将数据设置到请求域中
            request.setAttribute("userInfo",user);
        }
        return "/user/add_update";
    }


    /**
     * 删除用户
     *
     * @param ids
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public ResultInfo deleteUser(Integer[] ids) {
        userService.deleteByIds(ids);
        return success("用户删除成功！");
    }

}
