/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.ts.tests.portlet.api.javax_portlet.RenderRequest;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletSession;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.PrintWriter;
import com.sun.ts.tests.portlet.common.util.ResultWriter;


/**
 *	A  Test for PortletRequest.getPortletSession()
 */


public class GetPortletSessionTestPortlet extends GenericPortlet {

    public static String TEST_NAME="GetPortletSessionTest";
    
    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

		if (request.getPortletSession() != null) {
           	resultWriter.setStatus(ResultWriter.PASS);
		} else {
           	resultWriter.setStatus(ResultWriter.FAIL);
           	resultWriter.addDetail("Request.getPortletSession() returned null");
		}
        out.println(resultWriter.toString());
    }
}
