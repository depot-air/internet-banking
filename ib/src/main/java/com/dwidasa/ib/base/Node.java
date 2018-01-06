package com.dwidasa.ib.base;

import com.dwidasa.engine.model.Menu;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/28/11
 * Time: 6:18 PM
 */
public class Node {
    @Parameter(required = true, cache = false)
    private Menu node;

    @Parameter
    private Integer position;

    @Property(write = false)
    private String lastNode;

    @SessionAttribute
    private String activeMenu;
    
    @SessionAttribute
    private String activeNode;

    @Inject
    private Messages messages;

    Boolean setupRender() {
        return node != null;
    }

    Boolean beforeRenderBody() {
        Boolean render = node.getChildrens().size() > 0;
        if (render) {
            node = node.getChildrens().get(position);
        }

        return render;
    }

    Boolean afterRenderBody() {
        position++;
        return node.getChildrens().size() <= position;
    }

    void afterRender() {
        if (node.getParent() != null) {
            node = node.getParent();
        }
    }

    public Menu getNode() {
        return node;
    }

    public void setLastNode(String lastNode) {
        this.lastNode = lastNode;
    }

    public String getActiveMenu() {
        return activeMenu;
    }

    public void setActiveMenu(String activeMenu) {
        this.activeMenu = activeMenu;
    }
    
    public String getActiveNode() {
		return activeNode;
	}

	public void setActiveNode(String activeNode) {
		this.activeNode = activeNode;
	}
	
	public boolean isActiveNode() {    	
    	if (node.getLocation() != null && activeNode != null && node.getLocation().toUpperCase().equals(activeNode.toUpperCase())) {
    		return true;
    	}
    	return false;
    }

    public String getMenuName() {
        return messages.get(node.getMenuName());
    }

    Object onRedirect(String location, String parentId) {
        setActiveMenu(parentId);
        setActiveNode(location);
        return location;
    }
    
    
}
