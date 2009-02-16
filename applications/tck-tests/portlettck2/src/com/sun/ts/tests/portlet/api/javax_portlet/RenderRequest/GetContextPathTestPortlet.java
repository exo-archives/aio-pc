/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.RenderRequest;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.PrintWriter;

import com.sun.ts.tests.portlet.common.util.tags.PortletTCKCustomTag;


/**
 * A portlet participating in the test that just writes the 
 * context path on the output response.
 */

public class GetContextPathTestPortlet extends GenericPortlet {

    public static String TEST_NAME="GetContextPathTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String contextPath = request.getContextPath();

        PortletTCKCustomTag customTag
            = new PortletTCKCustomTag(CommonConstants.PATH_TO_SERVLET_TAG);

		String pathToServlet = response.encodeURL(contextPath +
						"/GetContextPathTestServlet");

        customTag.setTagContent(pathToServlet);
        out.println(customTag.toString());

    }
}
