package com.dwidasa.ib.components;

import org.apache.tapestry5.annotations.Parameter;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/28/11
 * Time: 6:18 PM
 */
public class Assign {
    @Parameter(required = true, cache = false)
    private Object to;

    Object beginRender() {
        return to;
    }
}
