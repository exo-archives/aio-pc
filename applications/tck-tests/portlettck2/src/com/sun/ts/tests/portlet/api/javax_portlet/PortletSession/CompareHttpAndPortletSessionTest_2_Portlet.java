/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.ts.tests.portlet.api.javax_portlet.PortletSession;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletSession;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.PrintWriter;
import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;

/*
 * A portlet participating in the test that checks for a attribute put in
 * the session by an servlet in the same application.
 */


public class CompareHttpAndPortletSessionTest_2_Portlet extends GenericPortlet {

    public static String TEST_NAME="CompareHttpAndPortletSessionTest";
    
    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        RequestCount reqCount = new RequestCount(request, response,
                                        RequestCount.MANAGED_VIA_SESSION);

        if (!reqCount.isFirstRequest()) {
            PortletSession session = request.getPortletSession();

            String testName = (String)session.getAttribute(
                "testName", PortletSession.APPLICATION_SCOPE);

            if ((testName != null) && (testName.equals( TEST_NAME))) {
                resultWriter.setStatus(ResultWriter.PASS);
            } else {
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail("For session attribute testName "
                    + "Expected Value:" + TEST_NAME
                    + "Actual Value:" + testName);
            }
        	out.println(resultWriter.toString());
		}
    }
}
