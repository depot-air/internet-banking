package com.dwidasa.ib.annotations;

import java.lang.annotation.*;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/19/11
 * Time: 1:21 PM
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoValidate {
}
