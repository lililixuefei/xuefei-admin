package com.xuefei.exception;

import org.springframework.util.StringUtils;

/**
 * @description:
 * @author: xuefei
 * @date: 2021/03/09 15:49
 */
public class EntityExistException extends RuntimeException {

    public EntityExistException(Class clazz, String field, String val) {
        super(EntityExistException.generateMessage(clazz.getSimpleName(), field, val));
    }

    private static String generateMessage(String entity, String field, String val) {
        return StringUtils.capitalize(entity)
                + " with " + field + " "+ val + " existed";
    }
}
