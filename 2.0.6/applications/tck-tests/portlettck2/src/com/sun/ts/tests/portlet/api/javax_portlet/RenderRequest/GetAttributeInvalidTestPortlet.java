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
 * This portlets checks that getAttribute() returns null if no attribute
 * of the given name exists.
 */


public class GetAttributeInvalidTestPortlet extends GenericPortlet {
    public static String TEST_NAME="GetAttributeInvalidTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        String attr =(String ) request.getAttribute( "doesnotexist" );

        // No attribute was set. Expecting null value
        if(attr == null ) {
            resultWriter.setStatus(ResultWriter.PASS);
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail( 
                "PortletRequest.getAttribute() returned incorrect value " );
            resultWriter.addDetail( "Expected result= null " );
            resultWriter.addDetail( "Actual result = |" + attr + "|" );
        }
        out.println(resultWriter.toString());
    }
}
