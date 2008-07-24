/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.ts.tests.portlet.api.javax_portlet.RenderRequest;

import java.util.Enumeration;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import java.io.PrintWriter;
import java.io.IOException;
import com.sun.ts.tests.portlet.common.util.ResultWriter;


/**
 *	A  Test for PortletRequest.getResponseContentTypes()
 * Checks that return value of this method is not null.
 * And the first item in the list is same as returned by
 * getResponseContentType().
 */


public class GetResponseContentTypesTestPortlet extends GenericPortlet {

    public static String TEST_NAME="GetResponseContentTypesTest";
    
    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
		Enumeration responseTypes = request.getResponseContentTypes();

		if (responseTypes != null && responseTypes.hasMoreElements()) { 
			String firstElement = (String)responseTypes.nextElement();
			if (firstElement.equals(request.getResponseContentType())) {
              	resultWriter.setStatus(ResultWriter.PASS);
			}
            else {
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail( "First element of " +
                    " PortletRequest.getResponseContentTypes() must be equal"+ 
                    " to value returned by getResponseContentType()" );
                resultWriter.addDetail( "First element returned is:" + 
                    firstElement);
                resultWriter.addDetail( "First element expected is:" + 
                    request.getResponseContentType());
            }

        } else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail( 
                "PortletRequest.getResponseContentTypes() returned a "
                + " null result " );
        }
        out.println(resultWriter.toString());
    }
}
