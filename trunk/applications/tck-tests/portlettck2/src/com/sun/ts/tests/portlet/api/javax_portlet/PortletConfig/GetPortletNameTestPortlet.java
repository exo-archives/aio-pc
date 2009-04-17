/**
 * Copyright 2007 IBM Corporation.
 */
/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletConfig;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 *	This class tests the getPortletName() method. This method returns
 *  the name of the portlet.
 */

public class GetPortletNameTestPortlet extends GenericPortlet {

	static public String TEST_NAME = "GetPortletNameTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME); 
		String expectedResult = "GetPortletNameTestPortlet";
        PortletConfig portletConfig = getPortletConfig();

		if(portletConfig != null) {
        	//get this portlet's name
        	String portletName = portletConfig.getPortletName();

        	if (portletName != null) {
                resultWriter.setStatus(ResultWriter.PASS); 
        	} else {
                resultWriter.setStatus(ResultWriter.FAIL); 
				resultWriter.addDetail("getPortletName() ");
                resultWriter.addDetail("returned null." );
        	}
    	} else {
            resultWriter.setStatus(ResultWriter.FAIL); 
			resultWriter.addDetail("getPortletConfig() returned null" );
		}
        out.println(resultWriter.toString());
	}
}
