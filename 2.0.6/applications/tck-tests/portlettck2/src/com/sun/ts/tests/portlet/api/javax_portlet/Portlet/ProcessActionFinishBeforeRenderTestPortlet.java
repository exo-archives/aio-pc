/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.Portlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

/**
 * This class participates in testing that the
 * portal/portlet-container waits until the action request finishes
 * before the render methods for all the portlets in the portal page
 * are invoked.  It waits 10 seconds and then stores an attribute in
 * the portlet session in processAction().  It then looks for that
 * attribute in render().
 */
public class ProcessActionFinishBeforeRenderTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "ProcessActionFinishBeforeRenderTest";

    public void processAction(ActionRequest request, ActionResponse response)
        throws PortletException, IOException {

        try {
            Thread.sleep(10 * 1000); // wait 10 seconds
        } catch (InterruptedException e) {
        }

        PortletSession session = request.getPortletSession();

        // then set an attribute in the session
        session.setAttribute(TEST_NAME, TEST_NAME,
                             PortletSession.APPLICATION_SCOPE);
    }

    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        RequestCount requestCount
            = new RequestCount(request, response,
                               RequestCount.MANAGED_VIA_SESSION);

        if (requestCount.isFirstRequest()) {
            PortletURL url = response.createActionURL();
            PortletURLTag urlTag = new PortletURLTag();
            urlTag.setTagContent(url.toString());        
            out.println(urlTag.toString());
        } else {
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
