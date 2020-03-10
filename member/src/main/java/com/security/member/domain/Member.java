package com.security.member.domain;

import lombok.Data;
import lombok.Setter;
import lombok.ToString;

/**
 * @author 刘亚林
 * @description
 * @create 2020/3/3 15:49
 **/
@Data
@ToString
public class Member {
    private String name;
    private String code;
    private String mobile;
    private Integer gender;
}
