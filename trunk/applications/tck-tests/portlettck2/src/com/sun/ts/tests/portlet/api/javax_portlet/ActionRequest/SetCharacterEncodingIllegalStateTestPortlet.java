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
import java.io.BufferedReader;
import java.io.PrintWriter;
import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 *	A Test for PortletRequest.setCharacterEncoding() method
 * Invoke the setCharacterEncoding() method after invoking
 * the getReader() method. Test passes if an
 * IllegatStateException() is thrown.
 */

public class SetCharacterEncodingIllegalStateTestPortlet extends LogicInProcessActionPortlet {

    public static String TEST_NAME="SetCharacterEncodingIllegalStateTest";


    public void processAction(ActionRequest request, ActionResponse response ) throws PortletException, IOException {

        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        BufferedReader reader = request.getReader();
        try {

            // Try setting a new character encoding set
            request.setCharacterEncoding(CommonConstants.CLIENT_CHARSET);

            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("Request.setCharacterEncoding() was "
                + " invoked after invoking Request.getReader(). "
                + " Expected an IllegalStateException() to be thrown."); 
        } catch(IllegalStateException ise) {
            resultWriter.setStatus(ResultWriter.PASS);
        }
        request.getPortletSession(true).setAttribute("TestResult", resultWriter.toString());
    }
}
