/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletContext;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletContext;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *	This class tests getInitParameter(String) method.
 */

import com.sun.ts.tests.portlet.common.util.ResultWriter;

public class GetInitParameterTestPortlet extends GenericPortlet {

	static public String TEST_NAME = "GetInitParameterTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        PortletContext context = getPortletContext();

        if (context != null) {
            String param = "EDITOR";
            String expectedResult = "VI";
            String result = context.getInitParameter( param );

            if(result != null ) {
                if(result.equals( "VI" ) ) {
				    resultWriter.setStatus(ResultWriter.PASS);
                } else {
				    resultWriter.setStatus(ResultWriter.FAIL);
                    resultWriter.addDetail("getInitParameter(String) gave "
                                                    + "incorrect results" );
                    resultWriter.addDetail("Expected result = "+expectedResult);
                    resultWriter.addDetail("Actual result = " + result); 
                }
            } else {
			    resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail("getInitParameter(" + param  
                                    + ") returned a null" );
            }
        } else {
			resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("getPortletContext() returned null");
        }
		out.println(resultWriter.toString());
    }
}
