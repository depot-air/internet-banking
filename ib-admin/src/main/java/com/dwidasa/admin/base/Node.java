package com.dwidasa.admin.base;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;

import com.dwidasa.engine.model.Menu;

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

	@Inject
	private PageRenderLinkSource pageRenderLinkSource;

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

	public String getMenuName() {
		return messages.get(node.getMenuName());
	}

	@SessionAttribute
	private String tabActive; // R=Regular Menu, F=Favorite

	Link onRedirect(String location, String parentId) {
		setActiveMenu(parentId);
		setActiveNode(location);
		Link link = pageRenderLinkSource.createPageRenderLinkWithContext(
				location, -1L);
		return link;
	}

	public boolean isActiveNode() {
		if (node.getLocation() != null && node.getLocation().equals(activeNode)) {
			return true;
		}
		return false;
	}
}
