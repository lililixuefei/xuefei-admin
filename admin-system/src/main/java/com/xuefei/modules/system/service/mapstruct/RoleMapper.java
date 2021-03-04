package com.xuefei.modules.system.service.mapstruct;

import com.xuefei.base.BaseMapper;
import com.xuefei.modules.system.domain.Role;
import com.xuefei.modules.system.service.dto.RoleDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author xuefei
 * @version 1.0
 * @date 2021/3/4 14:12
 */
@Mapper(componentModel = "spring", uses = {MenuMapper.class, DeptMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper extends BaseMapper<RoleDto, Role> {

}

