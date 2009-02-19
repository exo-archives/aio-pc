/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */



package com.sun.ts.tests.portlet.api.javax_portlet.RenderRequest;


import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.PrintWriter;
import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 *	A Negative test for getParameter(String)
 */

public class GetInvalidParameterTestPortlet extends GenericPortlet {

    public static String TEST_NAME="GetInvalidParameterTest";
    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        //we are not settting any parameter in the client side so we should get null
        String result = request.getParameter( "doesnotexist" );

        if(result == null ) {
            resultWriter.setStatus(ResultWriter.PASS);
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail( 
                "     PortletRequest.getParameter(doesnotexist) returned"
                + " incorrect result" );
            resultWriter.addDetail( "     Expected result = null " );
            resultWriter.addDetail( "     Actual result = |" + result + "|" );
        }
        out.println(resultWriter.toString());
    }
}
