/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletContext;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.GenericPortlet;
import java.io.IOException;
import java.io.PrintWriter;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class returns the name of this portlet web application correponding to 
 * as specified in the web.xml deployment descriptor for this web application 
 * by the display-name element.
 */

public class GetPortletContextNameTestPortlet extends GenericPortlet {

	static public String TEST_NAME = "GetPortletContextNameTest";
	static public String DISPLAY_NAME = "portlet_jp_PortletContext_web";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        PortletContext context = getPortletContext();

        if (context != null) {
			String contextName = context.getPortletContextName();
			if (contextName != null) {
            	if (contextName.equals(DISPLAY_NAME)) {
			    	resultWriter.setStatus(ResultWriter.PASS);
				} else {
			    	resultWriter.setStatus(ResultWriter.FAIL);
                	resultWriter.addDetail("Expected portletContextName : "
                                            		+ DISPLAY_NAME);
                	resultWriter.addDetail("Actual portletContextName : " 
                                            		+ contextName);
				}
			} else {
			   resultWriter.setStatus(ResultWriter.FAIL);
               resultWriter.addDetail("Expected portletContextName : "
                                            		+ DISPLAY_NAME);
               resultWriter.addDetail("Actual portletContextName : null");
			}
        } else {
			resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("getPortletContext() returned null");
        }
		out.println(resultWriter.toString());
    }
}
