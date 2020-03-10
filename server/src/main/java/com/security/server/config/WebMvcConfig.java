package com.security.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author 刘亚林
 * @description
 * @create 2020/3/5 9:59
 **/
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {


/*    @Override
    protected void addViewControllers(ViewControllerRegistry registry) {
        // 注册访问 /login 转向 login.html 页面
        registry.addViewController("/login").setViewName("login.html");
        super.addViewControllers(registry);
    }*/

    /**
     *     自定义静态资源目录
     *     因为该目录非thymeleaf默认静态资源目录
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/assets/");
        super.addResourceHandlers(registry);
    }

    /**
     * 配置跨域，否则无法注销
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //设置允许跨域的路径
        registry.addMapping("/**")
                //设置允许跨域请求的域名
                .allowedOrigins("*")
                //这里：是否允许证书 不再默认开启
                .allowCredentials(true)
                //设置允许的方法
                .allowedMethods("*")
                //跨域允许时间
                .maxAge(3600);
    }
}
