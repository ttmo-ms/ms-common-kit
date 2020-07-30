package com.ttmo.ms.common.kit.dynamic.query.repository;

import com.ttmo.ms.common.kit.dynamic.query.annotation.DynamicQuery;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * TODO
 *
 * @author Jover Zhang
 */
public class TargetRepository {

    @DynamicQuery("fields")
    public String targetMethod(Object fields) {
        String result;
        if (fields.getClass().isArray()) {
            result = Arrays.toString((String[]) fields);
        } else {
            result = (String) fields;
        }
        return result;
    }

}
