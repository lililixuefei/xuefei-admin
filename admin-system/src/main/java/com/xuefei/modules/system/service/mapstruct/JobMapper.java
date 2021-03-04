package com.xuefei.modules.system.service.mapstruct;

import com.xuefei.base.BaseMapper;
import com.xuefei.modules.system.domain.Job;
import com.xuefei.modules.system.service.dto.JobDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author xuefei
 * @version 1.0
 * @date 2021/3/4 14:14
 */
@Mapper(componentModel = "spring",uses = {DeptMapper.class},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JobMapper extends BaseMapper<JobDto, Job> {
}
