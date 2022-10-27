package com.baldprogrammer.crm.config;

import com.baldprogrammer.crm.interceptor.NoLoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @PROJECT_NAME: CRM
 * @DESCRIPTION:
 * @USER: baldprogrammer
 * @DATE: 2022/10/26 10:35 AM
 */
@Configuration  //配置类
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Bean //将方法的返回值交给IOC维护
    public NoLoginInterceptor noLoginInterceptor() {

        return new NoLoginInterceptor();
    }

    //添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //需要一个实现了拦截器的实例对象
        registry.addInterceptor(noLoginInterceptor())
                //设置需要拦截的资源 默认拦截所有资源
                .addPathPatterns("/**")
                //设置不需要拦截的资源
                .excludePathPatterns("/css/**","/images/**","/js/**","/lib/**","/index","/user/login");
    }
}
