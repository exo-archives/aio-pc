/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.Portlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;

/**
 * This class participates in testing that the
 * portal/portlet-container waits until the action request finishes
 * before the render methods for all the portlets in the portal page
 * are invoked.  It looks for a portlet session attribute in render()
 * that was set in the processAction() method of another portlet.
 */
public class ProcessActionFinishBeforeRenderTest_1_Portlet extends GenericPortlet {
    public static final String TEST_NAME = ProcessActionFinishBeforeRenderTestPortlet.TEST_NAME;

    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        RequestCount requestCount
            = new RequestCount(request, response,
                               RequestCount.MANAGED_VIA_SESSION);

        if (!requestCount.isFirstRequest()) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            ResultWriter resultWriter = new ResultWriter(TEST_NAME);
            String expectedResult = TEST_NAME;
            PortletSession session = request.getPortletSession();

            String result = (String)session.getAttribute(TEST_NAME,
                                                         PortletSession.APPLICATION_SCOPE);

            if ((result != null) && result.equals(expectedResult)) {
                resultWriter.setStatus(ResultWriter.PASS);
            } else {
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail("Expected result = " + expectedResult);
                resultWriter.addDetail("Actual result = " + result);
            }

            out.println(resultWriter.toString());            
        }
    }
}
