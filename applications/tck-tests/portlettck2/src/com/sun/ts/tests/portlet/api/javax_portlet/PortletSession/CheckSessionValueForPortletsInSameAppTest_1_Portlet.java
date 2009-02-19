/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletSession;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletSession;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.PrintWriter;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;


/*
 * One of the portlets participating in the test, that checks for an
 * attribute put in the session by another portlet in the same application.
 */

public class CheckSessionValueForPortletsInSameAppTest_1_Portlet extends GenericPortlet {

    public static String TEST_NAME=CheckSessionValueForPortletsInSameAppTestPortlet.TEST_NAME;
    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        RequestCount reqCount = new RequestCount(request, response,
                                        RequestCount.MANAGED_VIA_SESSION);

        if (!reqCount.isFirstRequest()) {

            String expectedValue = TEST_NAME;
            PortletSession session = request.getPortletSession();

            if (session != null) {
                String value = (String)session.getAttribute("testName", 
                                PortletSession.APPLICATION_SCOPE);
                if (value != null && value.equals(expectedValue)) {
                    resultWriter.setStatus(ResultWriter.PASS);
                } else {
                    resultWriter.setStatus(ResultWriter.FAIL);
                    resultWriter.addDetail("Expected value: " + expectedValue);
                    resultWriter.addDetail("Actual value: " + value);
                }
            } else {
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail("Request.getSession()returned a null value");
            }
           out.println(resultWriter.toString());
        }
    }
}
