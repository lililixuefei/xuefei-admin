package com.xuefei.modules.system.service.dto;

import com.xuefei.annotation.Query;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * @description:
 * @author: xuefei
 * @date: 2021/03/09 15:44
 */
@Data
public class RoleQueryCriteria {
    @Query(blurry = "name,description")
    private String blurry;

    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;
}
