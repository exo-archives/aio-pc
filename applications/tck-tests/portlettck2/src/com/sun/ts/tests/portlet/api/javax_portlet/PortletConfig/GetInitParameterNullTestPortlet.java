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
 *	This class tests the getInitParameter() method. The class checks
 *  whether the method returns null when the init parameter is not present.
 */

public class GetInitParameterNullTestPortlet extends GenericPortlet {

	static public String TEST_NAME = "GetInitParameterNullTest";

    /**
     *	Returns the init parameter using the Portlet Config from
     *  the deployment descriptor file
     */

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        PortletConfig portletConfig = getPortletConfig();

        if(portletConfig != null ) {
            String result = portletConfig.getInitParameter( "OS" );

            if (result == null) { 
                resultWriter.setStatus(ResultWriter.PASS);
            } else {
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail("getInitParameter(Language) did not ");
                resultWriter.addDetail("return the correct value");
                resultWriter.addDetail("Expected result = null"); 
                resultWriter.addDetail("Actual result = " + result );
            }
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("getPortletConfig returned null?" );
        }
        out.println(resultWriter.toString());
	}
}
