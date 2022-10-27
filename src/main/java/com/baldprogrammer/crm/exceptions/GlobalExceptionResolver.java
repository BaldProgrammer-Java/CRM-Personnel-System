package com.baldprogrammer.crm.exceptions;

import com.alibaba.fastjson.JSON;
import com.baldprogrammer.crm.base.ResultInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 全局异常处理器
 *
 * @PROJECT_NAME: CRM
 * @DESCRIPTION:
 * @USER: baldprogrammer
 * @DATE: 2022/10/25 5:10 PM
 */

@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {

    /**
     * 异常处理方法
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @return
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        /*
        * 非法请求拦截
        * */
        if (ex instanceof NoLoginException) {
            //重定向到登陆页面
            ModelAndView mv = new ModelAndView("redirect:/index");
            return mv;
        }
        /*
        * 设置默认异常处理 (返回View)
        * */
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("code", 500);
        modelAndView.addObject("msg", "系统异常，请重试...");

        //判断HandlerMethod
        if (handler instanceof HandlerMethod) {
            //类型转换
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //获取方法上声明的@ResponseBody注解对象
            ResponseBody responseBody = handlerMethod.getMethod().getDeclaredAnnotation(ResponseBody.class);
            //判断ResponseBody对象是否为空  (如果对象为空，则表示返回视图;如果不为空，则表示返回数据)
            if (responseBody == null) {
                if (ex instanceof ParamsException) {
                    ParamsException p = (ParamsException) ex;
                    modelAndView.addObject("code", p.getCode());
                    modelAndView.addObject("msg", p.getMsg());
                }
                return modelAndView;
            } else {
                //设置默认异常处理
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setCode(500);
                resultInfo.setMsg("系统异常，请重试...");
                //判断异常类型是否是自定义异常
                if (ex instanceof ParamsException) {
                    ParamsException p = (ParamsException) ex;
                    resultInfo.setCode(p.getCode());
                    resultInfo.setMsg(p.getMsg());
                }
                //设置响应类型及编码格式 (Json)
                response.setContentType("application/json;charset=UTF-8");
                //得到字符输出流
                PrintWriter out = null;
                try {
                    //得到输出流
                    out = response.getWriter();
                    //转换Json字符串
                    String json = JSON.toJSONString(resultInfo);
                    //输出流
                    out.write(json);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    //如果对象不为空  则关闭对象
                    if (out != null) {
                        out.close();
                    }
                }

                return null;
            }
        }

        return modelAndView;
    }
}
