package com.baldprogrammer.crm.interceptor;

import com.baldprogrammer.crm.dao.UserMapper;
import com.baldprogrammer.crm.exceptions.NoLoginException;
import com.baldprogrammer.crm.utils.LoginUserUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @PROJECT_NAME: CRM
 * @DESCRIPTION:
 * @USER: baldprogrammer
 * @DATE: 2022/10/25 10:27 PM
 */

public class NoLoginInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private UserMapper userMapper;

    /**
     * 拦截用户是否是登陆状态
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取cookie中的用户ID
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        //判断用户ID是否为空 且数据库中存在该ID的用户记录
        if (null == userId || userMapper.selectByPrimaryKey(userId) == null) {
            //抛出未登陆异常
            throw new NoLoginException();
        }
        return true;
    }


}
