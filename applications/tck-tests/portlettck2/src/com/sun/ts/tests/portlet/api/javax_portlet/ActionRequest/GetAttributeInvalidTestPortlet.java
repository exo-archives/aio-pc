/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.ActionRequest;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
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


public class GetAttributeInvalidTestPortlet extends LogicInProcessActionPortlet {
    public static String TEST_NAME="GetAttributeInvalidTest";

    public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {
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
        request.getPortletSession(true).setAttribute("TestResult", resultWriter.toString());
    }
}
