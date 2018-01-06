package com.dwidasa.admin.components;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.AfterRenderBody;
import org.apache.tapestry5.annotations.BeforeRenderBody;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

public class Window extends AbstractWindow
{
    @Environmental
    private JavaScriptSupport javaScriptSupport;

    private boolean hasBody = false;

    /**
     * Tapestry render phase method.
     * Called before component body is rendered.
     *
     * @param writer the markup writer
     */
    @BeforeRenderBody
    void beforeRenderBody(MarkupWriter writer)
    {
        hasBody = true;
        writer.element("div",
                       "id", getClientId() + "Content",
                       "style", "display:none;");
    }

    /**
     * Tapestry render phase method.
     * Called after component body is rendered.
     * return false to render body again.
     *
     * @param writer the markup writer
     */
    @AfterRenderBody
    void afterRenderBody(MarkupWriter writer)
    {
        writer.end();
    }


    /**
     * Tapestry render phase method. End a tag here.
     *
     * @param writer the markup writer
     */
    @AfterRender
    void afterRender(MarkupWriter writer)
    {
        JSONObject options = new JSONObject();

        options.put("className", getClassName());
        options.put("width", getWidth());
        options.put("height", getHeight());
        options.put("id", getClientId());
        options.put("title", getTitle());

        //
        // Let subclasses do more.
        //
        configure(options);

        javaScriptSupport.addScript("%s = new Window(%s);", getClientId(), options);

        if (hasBody)
        	javaScriptSupport.addScript("%s.setContent('%sContent');", getClientId(), getClientId());

        if (isShow())
        	javaScriptSupport.addScript("%s.show%s(%s);", getClientId(), isCenter() ? "Center" : "", isModal());
    }
}