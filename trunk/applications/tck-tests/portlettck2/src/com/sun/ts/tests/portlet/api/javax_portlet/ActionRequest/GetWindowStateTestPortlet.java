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
import javax.portlet.WindowState;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.WindowStateException;
import java.io.IOException;
import java.io.PrintWriter;
import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;


/**
 *	A  Test for PortletRequest.getWindowState()
 * Make a first request to the portlet and writes 
 * a PortletURL in the response object and then in
 * the second request which invokes the processAction()
 * method verify that the window state is WindowState.NORMAL using
 * request.getWindowState()
 */


public class GetWindowStateTestPortlet extends GenericPortlet {

    public static String TEST_NAME="GetWindowStateTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        PortletSession session = request.getPortletSession();
        RequestCount reqCount = new RequestCount(request, response,
                                        RequestCount.MANAGED_VIA_SESSION);
        if (reqCount.isFirstRequest()) {
            WindowState windowStateSet = null;
            PortletURLTag customTag = new PortletURLTag();
            PortletURL portletURL = response.createActionURL();
            customTag.setTagContent(portletURL.toString());
            out.println(customTag.toString());
        }
        else {
            out.println(session.getAttribute("resultGetWindowState"));
        }
    }

    public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        PortletSession session = request.getPortletSession();

        WindowState result = request.getWindowState();

        if ((result != null) && (result.equals(WindowState.NORMAL))) {
            resultWriter.setStatus(ResultWriter.PASS);
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("Expected result = " + WindowState.NORMAL.toString());
            resultWriter.addDetail("Actual result = " + result);
        }

        session.setAttribute("resultGetWindowState", resultWriter.toString());
    }
}
