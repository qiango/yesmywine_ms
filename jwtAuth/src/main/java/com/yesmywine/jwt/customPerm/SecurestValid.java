package com.yesmywine.jwt.customPerm;

import java.lang.annotation.*;

/**
 * Created by by on 2017/8/3.
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SecurestValid {
    public String value();
}