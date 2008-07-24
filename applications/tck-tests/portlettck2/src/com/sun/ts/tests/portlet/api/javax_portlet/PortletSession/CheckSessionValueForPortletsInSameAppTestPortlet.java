/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletSession;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletSession;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.PrintWriter;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;

/*
 * One of the portlets participicating in the test that puts a attribute
 * in the session.
 */

public class CheckSessionValueForPortletsInSameAppTestPortlet extends GenericPortlet {

    public static String TEST_NAME="CheckSessionValueForPortletsInSameAppTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        PortletSession session = request.getPortletSession();

        RequestCount reqCount = new RequestCount(request, response,
                                            RequestCount.MANAGED_VIA_SESSION);

        if (reqCount.isFirstRequest()) {

            if (session != null) {
               session.setAttribute(
                    "testName", TEST_NAME, PortletSession.APPLICATION_SCOPE);
            }
        }
    }
}
