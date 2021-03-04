package com.xuefei.modules.security.service.dto;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @description: 用户登录数据传输对象
 * @author: xuefei
 * @date: 2020/12/04 13:07
 */
@Getter
@Setter
public class AuthUserDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String code;

    private String uuid = "";
}
