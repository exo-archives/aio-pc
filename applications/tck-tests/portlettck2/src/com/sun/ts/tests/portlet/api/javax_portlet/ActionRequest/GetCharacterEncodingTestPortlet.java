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
import java.io.UnsupportedEncodingException;


/*
 *	A Test For getCharacterEncoding()
 *  Checks that getCharacterEncoding() returns the encoding
 *  set by this client in the request.
 *
 */


public class GetCharacterEncodingTestPortlet extends LogicInProcessActionPortlet {

    public static String TEST_NAME="GetCharacterEncodingTest";

    public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {

        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        String expectedResult = CommonConstants.CLIENT_CHARSET; 
        String encoding = request.getCharacterEncoding();

        if(encoding != null ) {
            if(encoding.equalsIgnoreCase( expectedResult ) ) {
                resultWriter.setStatus(ResultWriter.PASS);
            } else {
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail( 
                    "PortletRequest.getCharacterEncoding() returned the "
                    + " wrong result" );
                resultWriter.addDetail( "     Expected result = " + expectedResult  );
                resultWriter.addDetail( "     Actual result = |" + encoding + "|" );

            }

        } else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail( "     PortletRequest.getCharacterEncoding() returned a null result " );

        }
            request.getPortletSession(true).setAttribute("TestResult", resultWriter.toString());
    }
}
