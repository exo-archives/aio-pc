/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.ActionRequest;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletURL;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStream;
import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;


/**
 *	A Negative Test for PortletRequest.getInputStream method
 *  getInputStream() should throw an IllegalStateException if the content has 
 * application-form Post Data
 */
public class GetInputStreamIllegalStateWithApplFormPostDataTestPortlet extends  LogicInProcessActionPortlet {

	static public String TEST_NAME = "GetInputStreamIllegalStateWithApplFormPostDataTest";

    public void processAction(ActionRequest request, ActionResponse response ) throws PortletException, IOException {
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        try {
            InputStream stream = request.getPortletInputStream();
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail( "     PortletRequest.getInputStream() "
                + " method did not throw IllegalStateException() " );
        } catch(IllegalStateException iae ) {
            resultWriter.setStatus(ResultWriter.PASS);
        }

        request.getPortletSession(true).setAttribute("TestResult", resultWriter.toString());

    }
}
