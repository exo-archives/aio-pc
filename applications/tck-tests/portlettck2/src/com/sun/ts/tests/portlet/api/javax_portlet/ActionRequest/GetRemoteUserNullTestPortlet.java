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
 *	A Negative Test for getRemoteUser method
 */


public class GetRemoteUserNullTestPortlet extends LogicInProcessActionPortlet {

	public static String TEST_NAME="GetRemoteUserNullTest";

    public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {

        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        String result = request.getRemoteUser();

        if(result == null ) {
            resultWriter.setStatus(ResultWriter.PASS);
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail( 
                "PortletRequest.getRemoteUser() returned a Non-Null result " );
            resultWriter.addDetail( 
                "     Actual result = |" + result + "|" );

        }
        request.getPortletSession(true).setAttribute("TestResult", resultWriter.toString());
    }
}
