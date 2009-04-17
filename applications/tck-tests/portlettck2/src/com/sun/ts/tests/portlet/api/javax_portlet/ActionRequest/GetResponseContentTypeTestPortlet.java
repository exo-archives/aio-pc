/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.ts.tests.portlet.api.javax_portlet.ActionRequest;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import java.io.PrintWriter;
import java.io.IOException;
import com.sun.ts.tests.portlet.common.util.ResultWriter;


/**
 *	A  Test for PortletRequest.getResponseContentType()
 * Check that return value of
 * PortletRequest.getResponseContentType() is not null.
 */


public class GetResponseContentTypeTestPortlet extends LogicInProcessActionPortlet {

    public static String TEST_NAME="GetResponseContentTypeTest";
    
    public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        String responseType = request.getResponseContentType();

        if (responseType != null) {
            resultWriter.setStatus(ResultWriter.PASS);
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail( 
                "     PortletRequest.getResponseContentType() returned"
                + " a null result " );
        }
        request.getPortletSession(true).setAttribute("TestResult", resultWriter.toString());
    }
}
