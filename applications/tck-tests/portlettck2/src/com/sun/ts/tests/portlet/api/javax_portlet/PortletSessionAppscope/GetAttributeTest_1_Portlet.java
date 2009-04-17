/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletSessionAppscope;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletSession;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.PrintWriter;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;



/**
 * 	A test for PortletSession.getAttribute() method
 * This is the second portlet that tries to read the attribute set in
 * first portlet in first request.
 */


public class GetAttributeTest_1_Portlet extends GenericPortlet {

    public static String TEST_NAME = "GetAttributeTest";


    public void render(RenderRequest request, RenderResponse response ) throws PortletException, java.io.IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        PortletSession session = request.getPortletSession( true );
        RequestCount reqCount = new RequestCount(request, response,
                                        RequestCount.MANAGED_VIA_SESSION);
        
        if (!reqCount.isFirstRequest()) {

            String param = GetAttributeTestPortlet.ATTRIBUTE_NAME;
            String expectedResult = GetAttributeTestPortlet.ATTRIBUTE_VALUE;
            String value = (String)session.getAttribute( param, PortletSession.APPLICATION_SCOPE );

            if(value != null  && value.equals(expectedResult)) {
                resultWriter.setStatus(ResultWriter.PASS);
            } else if(value == null) {
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail( "PortletSession.getAttribute(object) returned a null result" );
            }
            else {
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail( "PortletSession.getAttribute(object) returned " + value);
                resultWriter.addDetail( "Expected Value " + expectedResult);
            }
            out.println(resultWriter.toString());
        }
    }
}
