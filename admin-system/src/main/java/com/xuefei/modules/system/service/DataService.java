package com.xuefei.modules.system.service;

import com.xuefei.modules.system.service.dto.UserDto;

import java.util.List;

/**
 * @description: 数据权限服务类
 * @author: xuefei
 * @date: 2021/03/10 16:33
 */
public interface DataService {

    /**
     * 获取数据权限
     * @param user /
     * @return /
     */
    List<Long> getDeptIds(UserDto user);
}
