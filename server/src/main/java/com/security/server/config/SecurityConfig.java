package com.security.server.config;

import com.security.server.coder.MD5PwdEncoder;
import com.security.server.provider.UserAuthenticationProvider;
import com.security.server.provider.UserSmsAuthenticationProvider;
import com.security.server.support.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author 刘亚林
 * @description
 * @create 2020/3/3 14:25
 **/
@Configuration
@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.requestMatchers()
                //.antMatchers("/login", "/oauth/authorize")
                .antMatchers("/login/**", "/oauth/**", "logout", "login", "login**")
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").permitAll()
                .and()
                //自定义登出
                .logout()
                //.logoutSuccessUrl("/")
/*                .logoutSuccessHandler(
                        (request, response, authentication) -> {
                            String callback = request.getParameter("callback");
                            if (callback == null){
                                callback = "/login?logout";
                            }
                            response.sendRedirect(callback);
                        }
                )*/
                .permitAll()
                //.logoutSuccessHandler(logoutSuccessHandler)
                //.logoutSuccessUrl("/")
                .and()
                //忽略跨域
                .cors()
                .and().csrf().disable();

/*      此段代码可能导致重复重定向，怀疑是语法跟版本不兼容或者client token地址错误导致
        http.authorizeRequests()
                .antMatchers("/oauth/**","/login/**", "/logout", "/login").permitAll()
                .anyRequest().authenticated()   // 其他地址的访问均需验证权限
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout().logoutSuccessUrl("/");*/
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //忽略静态资源拦截
        web.ignoring().antMatchers("/assets/**");
    }

    /**
     * 配置用户
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
/*        auth.inMemoryAuthentication()
                .withUser("john")
                .password(passwordEncoder().encode("123"))
                .roles("USER");*/

        auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder())
                .and()
                // 加入自定义的安全认证
                .authenticationProvider(userAuthenticationProvider())
                .authenticationProvider(userSmsAuthenticationProvider());
    }

/*    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }*/

    @Bean
    public MD5PwdEncoder passwordEncoder(){
        return new MD5PwdEncoder();
    }

    public static void main(String[] args) {
        System.out.println(new MD5PwdEncoder().encode("1234"));
        //81dc9bdb52d04dc20036dbd8313ed055
        System.out.println(new MD5PwdEncoder().encode("secret"));
        //5ebe2294ecd0e0f08eab7690d2a6ee69
    }

    @Bean
    public UserAuthenticationProvider userAuthenticationProvider(){
        return new UserAuthenticationProvider();
    }

    @Bean
    public UserSmsAuthenticationProvider userSmsAuthenticationProvider(){
        return new UserSmsAuthenticationProvider();
    }
}
