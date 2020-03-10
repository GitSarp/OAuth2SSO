## spring security OAuth2单点登录、单点注销实践

### 认证服务器端点
- /oauth/authorize  获取授权码(需要先登录)
- 参数
    - response_type=code
    - client_id=[clientId]
    - redirect_uri=[redirect_uri]
    - state=[random-number]
    
- /oauth/token 获取token
- 参数
    - grant_type=authorization_code
    - code=[authorization_code]
    - redirect_uri=[redirect_uri]
- 浏览器要求输入clientId和clientSecret，可以在postman中Authorization中type选择basic auth填入 

- /user/me 获取用户信息

### 问题
1. 循环重定向(**resolved**)
- 看客户端配置的端点是否正确
- 看sercurity config中的login** 是否通过验证，语法是否正确

2. 登出的时候传递token信息，避免被认证服务器重定向到登录
- 登出方式不可用
