package com.dwidasa.admin.components;

import org.apache.tapestry5.annotations.Parameter;

public class Assign {
	@Parameter(required = true, cache = false)
    private Object to;

    Object beginRender() {
        return to;
    }
}
