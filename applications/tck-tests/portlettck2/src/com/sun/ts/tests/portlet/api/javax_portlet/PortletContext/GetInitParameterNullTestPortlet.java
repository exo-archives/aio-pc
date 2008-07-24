/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletContext;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.PrintWriter;
import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 *	This class tests getInitParameter(String) method when a invalid parameter
 *  is specified.
 */

public class GetInitParameterNullTestPortlet extends GenericPortlet {

	static public String TEST_NAME ="GetInitParameterNullTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        PortletContext context = getPortletContext();

        if (context != null) {
            String result = context.getInitParameter("Language");

            if(result == null ) {
			    resultWriter.setStatus(ResultWriter.PASS);
            } else {
			    resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail("getInitParameter(String) gave "
                                                + "incorrect results" );
                resultWriter.addDetail("Expected result = null");
                resultWriter.addDetail("Actual result = " + result);  
            }
        } else {
			resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("getPortletContext() returned null");
        }
		out.println(resultWriter.toString());
    }
}
