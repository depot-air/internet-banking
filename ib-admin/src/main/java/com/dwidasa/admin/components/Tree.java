package com.dwidasa.admin.components;

import com.dwidasa.engine.model.Menu;
import com.dwidasa.admin.base.Node;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/28/11
 * Time: 6:20 PM
 */
public class Tree {
    @Parameter(required = true)
    private List<Menu> source;

    @InjectComponent
    private Branch branch;

    @InjectComponent
    private Leaf leaf;

    private Menu current;

    private Map<Menu, Integer> positions;

    private Integer index;

    public Menu getCurrent() {
        return current;
    }

    public void setCurrent(Menu menuItem) {
        if (!positions.containsKey(menuItem)) {
            positions.put(menuItem, 0);
        }

        current = menuItem;
    }

    public Integer getCurrentPosition() {
        return positions.get(this.current);
    }

    public void setCurrentPosition(Integer position) {
        positions.put(current, position);
    }

    public Node getCurrentNode() {
        Node result;
        if (current.getParent() == null || current.getChildrens().size() > 0) {
            result = branch;
        }
        else {
            result = leaf;
        }

        if (index == source.size() - 1) {
            result.setLastNode("true");
        }
        else {
            result.setLastNode("false");
        }

        return result;
    }

    void setupRender() {
        index = 0;
    }

    Boolean beforeRenderTemplate() {
        positions = new HashMap<Menu, Integer>();
        setCurrent(source.get(index));

        return Boolean.TRUE;
    }

    Boolean afterRenderTemplate() {
        return index++ >= source.size() - 1;
    }
}
