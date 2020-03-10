package com.security.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author 刘亚林
 * @description
 * @create 2020/3/3 14:12
 **/
@Configuration
//开启单点登录
@EnableOAuth2Sso
public class UiSecurityConfig  extends WebSecurityConfigurerAdapter {
    @Value("${security.oauth2.client.access-token-uri}")
    private String oauthHost;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //首页和登录页允许，其他请求均要鉴权
        http.antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/", "/login**", "/index**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                //.csrf().disable()
                .logout()
//                .logoutSuccessHandler(
//                        ((request, response, authentication) -> {
//                            response.sendRedirect(oauthHost.split("oauth")[0] + "logout?callback=http://" + request.getHeader("Host"));
//                        })
//                )
                .logoutSuccessUrl("http://localhost:8081/auth/oauth/exit")
                .and().cors().and().csrf().disable();

    }
}
