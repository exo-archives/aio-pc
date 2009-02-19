/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletURL;

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
 * This class tests that the processAction method of the targeted
 * portlet of a render URL is not invoked.
 */
public class ProcessActionNotCalledForRenderURLTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "ProcessActionNotCalledForRenderURLTest";

    public void processAction(ActionRequest request, ActionResponse response)
        throws PortletException, IOException {

        request.getPortletSession().setAttribute(TEST_NAME, TEST_NAME);
    }

    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        RequestCount requestCount
            = new RequestCount(request, response,
                               RequestCount.MANAGED_VIA_SESSION);

        if (requestCount.isFirstRequest()) {
            PortletURL url = response.createRenderURL();
            PortletURLTag urlTag = new PortletURLTag();
            urlTag.setTagContent(url.toString());        
            out.println(urlTag.toString());
        } else {
            ResultWriter resultWriter = new ResultWriter(TEST_NAME);
            PortletSession session = request.getPortletSession();
            String result = (String)session.getAttribute(TEST_NAME);

            if (result == null) {
                resultWriter.setStatus(ResultWriter.PASS);
            } else {
                session.removeAttribute(TEST_NAME); // clean up
                resultWriter.setStatus(ResultWriter.FAIL);

                resultWriter.addDetail("processAction() is invoked for "
                                       + "a render URL.");
            }

            out.println(resultWriter.toString());            
        }
    }
}
