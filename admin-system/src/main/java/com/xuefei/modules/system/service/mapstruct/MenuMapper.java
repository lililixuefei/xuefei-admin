package com.xuefei.modules.system.service.mapstruct;

import com.xuefei.base.BaseMapper;
import com.xuefei.modules.system.domain.Menu;
import com.xuefei.modules.system.service.dto.MenuDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author xuefei
 * @version 1.0
 * @date 2021/3/4 14:13
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuMapper extends BaseMapper<MenuDto, Menu> {
}
