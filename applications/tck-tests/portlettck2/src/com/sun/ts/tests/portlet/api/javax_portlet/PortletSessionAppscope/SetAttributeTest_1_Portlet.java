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
 * 	 A test for PortletSession.SetAttributeTest(String,Object, int) method
 */

public class SetAttributeTest_1_Portlet extends GenericPortlet {

    public static String TEST_NAME = "SetAttributeTest";


    public void render(RenderRequest request, RenderResponse response ) throws PortletException, java.io.IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
		RequestCount reqCount = new RequestCount(request, response,
										RequestCount.MANAGED_VIA_SESSION);
		
		if (!reqCount.isFirstRequest()) {
        	PortletSession session = request.getPortletSession( true );
        	String param = SetAttributeTestPortlet.ATTRIBUTE_NAME;
        	String expectedResult = SetAttributeTestPortlet.ATTRIBUTE_VALUE;
        
        	String result =(String ) session.getAttribute( param , PortletSession.APPLICATION_SCOPE);

        	if(result != null && result.equals( expectedResult ) ) {
            	resultWriter.setStatus(ResultWriter.PASS);
        	} else if(result == null) {
            	resultWriter.setStatus(ResultWriter.FAIL);
            	resultWriter.addDetail( "     PortletSession.getAttribute(" + param + ") returned null" );
        	}
        	else {
           	 resultWriter.setStatus(ResultWriter.FAIL);
           	 resultWriter.addDetail( "     PortletSession.getAttribute(" + param + ") returned an incorrect result" );
           	 resultWriter.addDetail( "     Expected result = " + expectedResult );
           	 resultWriter.addDetail( "     Actual result = |" + result + "|");
        	}
        	out.println(resultWriter.toString());
		}
    }
}
