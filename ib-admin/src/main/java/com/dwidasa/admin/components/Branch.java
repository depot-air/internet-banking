package com.dwidasa.admin.components;

import com.dwidasa.admin.base.Node;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/28/11
 * Time: 6:19 PM
 */
public class Branch extends Node {
    public String getHasChildren() {
        return getNode().getChildrens() != null && getNode().getChildrens().size() != 0 ? "true" : "false";
    }

    public String getActiveBranch() {
        Long activeMenu = -1L;
        if (getActiveMenu() != null) {
            activeMenu = Long.valueOf(getActiveMenu());
        }
        return getNode().getId().equals(activeMenu) ? "true" : "false";
    }
}
