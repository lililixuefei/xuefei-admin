
package com.xuefei.modules.system.service.dto;

import com.xuefei.base.BaseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author xuefei
 * @version 1.0
 * @date 2021/3/4 14:13
 */
@Getter
@Setter
@NoArgsConstructor
public class JobDto extends BaseDTO implements Serializable {

    private Long id;

    private Integer jobSort;

    private String name;

    private Boolean enabled;

    public JobDto(String name, Boolean enabled) {
        this.name = name;
        this.enabled = enabled;
    }
}