/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */



package com.sun.ts.tests.portlet.api.javax_portlet.ActionRequest;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.PrintWriter;
import com.sun.ts.tests.portlet.common.util.ResultWriter;


/**
 *	A Test for getContentType
 */


public class GetContentTypeTestPortlet extends LogicInProcessActionPortlet {

    public static String TEST_NAME="GetContentTypeTest";

    public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {


        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        // in client side it was set to "text/plain"
        String contentType = request.getContentType();

        String expectedResult = "text/plain"; 

        if(contentType != null ) {
            if(contentType.equals( expectedResult ) ) {
                resultWriter.setStatus(ResultWriter.PASS);
            } else {
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail( 
                    "     PortletRequest.getContentType() returned "
                    + "   an incorrect result " );
                resultWriter.addDetail( 
                    "     Expected result = " + expectedResult  );
                resultWriter.addDetail( 
                    "     Actual result = |" + contentType + "|" );
            }
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail( 
                "     PortletRequest.getContentType() returned a null" );
        }
        request.getPortletSession(true).setAttribute("TestResult", resultWriter.toString());
    }
}
