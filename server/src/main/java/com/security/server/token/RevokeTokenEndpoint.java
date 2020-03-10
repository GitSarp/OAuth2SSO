package com.security.server.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 刘亚林
 * @description
 * @create 2020/3/10 9:59
 **/
@FrameworkEndpoint
public class RevokeTokenEndpoint {

    @Autowired
    DefaultTokenServices tokenServices;

    //注销方式3
    @DeleteMapping("/oauth/token")
    @ResponseBody
    public String revokeToken(String access_token) {
        if (tokenServices.revokeToken(access_token)){
            return "注销成功";
        }else{
            return "注销失败";
        }
    }
}
