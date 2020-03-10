package com.security.server.dao;

import com.security.server.domain.SysPermission;
import com.security.server.domain.SysRole;
import com.security.server.domain.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Arrays;

@Slf4j
@Repository
public class UserDao {

    private SysRole superAdmin = new SysRole("SUPER_ADMIN", "超级管理员");

    private SysRole admin = new SysRole("ADMIN", "管理员");
    private SysRole developer = new SysRole("DEVELOPER", "开发者");

    {
        SysPermission p1 = new SysPermission();
        p1.setCode("memberExport");
        p1.setName("会员列表导出");
        p1.setUrl("/member/export");

        SysPermission p2 = new SysPermission();
        p2.setCode("BookList");
        p2.setName("图书列表");
        p2.setUrl("/book/list");

/*
        //访问服务端不要指定权限？
        SysPermission p3 = new SysPermission();
        p3.setCode("ServerHome");
        p3.setName("服务端首页");
        p3.setUrl("/index");
        superAdmin.setPermissionList(Arrays.asList(p1, p2, p3));*/

        superAdmin.setPermissionList(Arrays.asList(p1, p2));
        admin.setPermissionList(Arrays.asList(p1, p2));
        developer.setPermissionList(Arrays.asList(p1));

    }

    public SysUser selectByName(String username) {
        log.info("从数据库中查询用户");
        if ("zhangsan".equals(username)) {
            SysUser sysUser = new SysUser("zhangsan", "81dc9bdb52d04dc20036dbd8313ed055");
            sysUser.setRoleList(Arrays.asList(admin, developer));
            return sysUser;
        }else if ("lisi".equals(username)) {
            SysUser sysUser = new SysUser("lisi", "81dc9bdb52d04dc20036dbd8313ed055");
            sysUser.setRoleList(Arrays.asList(developer));
            return sysUser;
        }else if("freaxjj".equals(username)){
            SysUser sysUser = new SysUser("freaxjj", "81dc9bdb52d04dc20036dbd8313ed055");
            sysUser.setRoleList(Arrays.asList(superAdmin));
            return sysUser;
        }
        return null;
    }

}
