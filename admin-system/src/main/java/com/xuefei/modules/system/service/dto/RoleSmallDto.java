package com.xuefei.modules.system.service.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @author: xuefei
 * @date: 2021/01/05 23:42
 */
@Data
public class RoleSmallDto implements Serializable {

    private Long id;

    private String name;

    private Integer level;

    private String dataScope;
}
