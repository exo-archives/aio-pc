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
 *	This class tests the getInitParameter() method. This method
 *  returns an init parameter from the deployment descriptor file.
 */

public class GetInitParameterTestPortlet extends GenericPortlet {

	static public String TEST_NAME = "GetInitParameterTest";

    /**
     *	Returns the init parameter using the Portlet Config from
     *  the deployment descriptor file
     */

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        String expectedResult = "Java";
        PortletConfig portletConfig = getPortletConfig();

        if(portletConfig != null ) {
            String result = portletConfig.getInitParameter( "Language" );

            if (result != null &&  result.equals( expectedResult)) {
                resultWriter.setStatus(ResultWriter.PASS);
            } else {
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail("getInitParameter(Language) did not ");
                resultWriter.addDetail("return the correct parameter name ");
                resultWriter.addDetail("Expected result = " + expectedResult );
                resultWriter.addDetail("Actual result = " + result );
            }
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("getPortletConfig returned null?" );
        }
        out.println(resultWriter.toString());
	}
}
