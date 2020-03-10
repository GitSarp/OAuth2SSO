package com.security.server.config;

import com.security.server.coder.MD5PwdEncoder;
import com.security.server.token.UserTokenEnhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author 刘亚林
 * @description 授权服务器配置
 * @create 2020/3/3 14:21
 **/
@Configuration
//授权服务器
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

//    @Autowired
//    private BCryptPasswordEncoder passwordEncoder;
//    private MD5PwdEncoder passwordEncoder;

//    @Autowired
//    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Resource
    private DataSource dataSource;



    /**
     * 配置授权服务器的安全，实际是暴露获取token的端点/oauth/token
     * /oauth/authorize端点也应该是安全的
     * 默认的设置覆盖到了绝大多数需求，所以一般情况下你不需要做任何事情。
     */
    @Override
    public void configure(
            AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                //允许外部表单获取token
                .allowFormAuthenticationForClients();
    }

    /**
     * 配置ClientDetailsService
     * 至少配置一个客户端client，否则服务器将不会启动。
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource);
        //内存客户端配置
/*        clients.inMemory()
                .withClient("SampleClientId")//对应客户端clientId
                .secret(passwordEncoder.encode("secret"))
                // 该client允许的授权类型
                .authorizedGrantTypes("authorization_code", "refresh_token")//授权码方式
                .scopes("user_info")
                .autoApprove(true)
                .redirectUris(
                        "http://localhost:8082/ui/login","http://localhost:8083/ui2/login");*/
    }

    /**
     * 该方法是用来配置Authorization Server endpoints的一些非安全特性的，比如token存储、token自定义、授权类型等等的
     * 默认情况下，你不需要做任何事情，除非你需要密码授权，那么在这种情况下你需要提供一个AuthenticationManager
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //允许get方法获取token
        endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);

        DefaultTokenServices tokenServices = tokenServices();
        tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
        endpoints.tokenServices(tokenServices);
    }

    @Bean
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        //token持久化容器，redis或者jwt
        tokenServices.setTokenStore(tokenStore());
        //是否支持refresh_token，默认false
        tokenServices.setSupportRefreshToken(true);
        //客户端信息
        //tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
        //token生成
        tokenServices.setTokenEnhancer(tokenEnhancer());
        //tokenServices.setTokenEnhancer(jwtAccessTokenConverter());
        //access_token 的有效时长 (秒), 默认 12 小时
        tokenServices.setAccessTokenValiditySeconds(60*15);
        //refresh_token 的有效时长 (秒), 默认 30 天
        tokenServices.setRefreshTokenValiditySeconds(60*20);
        //是否复用refresh_token,默认为true(如果为false,则每次请求刷新都会删除旧的refresh_token,创建新的refresh_token)
        tokenServices.setReuseRefreshToken(true);
        return tokenServices;
    }

    /**
     *     jwt || redis
     *     如果保存在内存中，那么资源服务器与认证服务器必须在同一个工程中。
     *     如果不保存access_token，则没法通过access_token取得用户信息
     */
    @Bean
    public TokenStore tokenStore() {
        //return new InMemoryTokenStore();
        //return new JwtTokenStore(jwtAccessTokenConverter());
        //return new JdbcTokenStore();
        //存储在redis中
        return new RedisTokenStore(redisConnectionFactory);
    }

    //jwt
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey("cjs");   //  Sets the JWT signing key
        return jwtAccessTokenConverter;
    }


    /**
     * @Description 自定义生成令牌token
     * @Date 2019/7/9 19:58
     * @Version  1.0
     */
    @Bean
    public TokenEnhancer tokenEnhancer(){
        //return jwtAccessTokenConverter();
        return new UserTokenEnhancer();
    }

}
