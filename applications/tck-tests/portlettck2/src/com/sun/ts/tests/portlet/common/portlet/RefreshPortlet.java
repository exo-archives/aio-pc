/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.common.portlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

/**
 * This class provides a refresh mechanism for other test portlets.
 * All it does is printing out a PortletURL with no action to the
 * output stream.
 */
public class RefreshPortlet extends GenericPortlet {
    public void doView(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        PortletURLTag urlTag = new PortletURLTag();
        urlTag.setTagContent(response.createRenderURL().toString());        
        out.println(urlTag.toString());
    }
}
