package com.xuefei.utils;

import cn.hutool.core.util.ObjectUtil;
import com.xuefei.exception.BadRequestException;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;

/**
 * @author xuefei
 * @version 1.0
 * @date 2021/3/4 14:30
 */
public class ValidationUtil{

    /**
     * 验证空
     */
    public static void isNull(Object obj, String entity, String parameter , Object value){
        if(ObjectUtil.isNull(obj)){
            String msg = entity + " 不存在: "+ parameter +" is "+ value;
            throw new BadRequestException(msg);
        }
    }

    /**
     * 验证是否为邮箱
     */
    public static boolean isEmail(String email) {
        return new EmailValidator().isValid(email, null);
    }
}
