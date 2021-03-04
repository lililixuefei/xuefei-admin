package com.xuefei.modules.system.service.mapstruct;

import com.xuefei.base.BaseMapper;
import com.xuefei.modules.system.domain.User;
import com.xuefei.modules.system.service.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author xuefei
 * @version 1.0
 * @date 2021/3/4 14:11
 */
@Mapper(componentModel = "spring",uses = {RoleMapper.class, DeptMapper.class, JobMapper.class},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends BaseMapper<UserDto, User> {
}