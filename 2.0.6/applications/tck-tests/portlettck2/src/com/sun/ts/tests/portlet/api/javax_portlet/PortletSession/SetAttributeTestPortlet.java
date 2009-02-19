/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletSession;

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

/**
 * 	 A test for PortletSession.SetAttributeTest(String,Object) method
 */

public class SetAttributeTestPortlet extends GenericPortlet {

    public static String TEST_NAME = "SetAttributeTest";


    public void render(RenderRequest request, RenderResponse response ) throws PortletException, java.io.IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        PortletSession session = request.getPortletSession( true );
        String param = "object";
        String expectedResult = "Java";

        session.setAttribute( param, expectedResult);

        String result = (String) session.getAttribute( 
                    param , PortletSession.PORTLET_SCOPE);

        if(result.equals( expectedResult ) ) {
            resultWriter.setStatus(ResultWriter.PASS);
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail( "     PortletSession.getAttribute(" + param + ") returned an incorrect result" );
            resultWriter.addDetail( "     Expected result = " + expectedResult );
            resultWriter.addDetail( "     Actual result = |" + result + "|");
        }
        out.println(resultWriter.toString());
    }
}
