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
import javax.portlet.PortletURL;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStream;
import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;


/**
 *	A Negative Test for PortletRequest.getReader() method
* First request is to a portlet that writes a render URL
*                 to the response object. In the second request in the
*                 render() method the getReader() is invoked after
*                 getPortletInputStream() method.
*                 Test passes if IllegalStateException exception is thrown.
*/

public class GetReaderIllegalStateAfterInputStreamTestPortlet extends LogicInProcessActionPortlet {

    public static String TEST_NAME="GetReaderIllegalStateAfterInputStreamTest";


    public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {

        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        InputStream stream = request.getPortletInputStream();
        try {
            BufferedReader reader = request.getReader();
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail( 
                "    PortletRequest.getReader() should have thrown an " +
                " IllegalStateException. " );
        } catch(IllegalStateException ise) {
            resultWriter.setStatus(ResultWriter.PASS);
        }
        request.getPortletSession(true).setAttribute("TestResult", resultWriter.toString());
    }

}
