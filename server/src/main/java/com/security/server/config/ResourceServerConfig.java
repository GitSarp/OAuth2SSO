package com.security.server.config;

import com.security.server.handler.UserLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

/*    @Autowired
    UserLogoutSuccessHandler logoutSuccessHandler;*/

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.requestMatchers().antMatchers("/user/**")
                .and()
                .authorizeRequests()
                .anyRequest().authenticated();

/*                .and()
                .logout()
                .logoutSuccessHandler(logoutSuccessHandler)
                .and()
                //忽略跨域
                .csrf().disable();*/
    }
}
