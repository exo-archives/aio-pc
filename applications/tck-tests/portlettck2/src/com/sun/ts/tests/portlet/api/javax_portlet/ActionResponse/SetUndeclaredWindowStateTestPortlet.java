/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.ActionResponse;

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
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

/**
 * This class tests the WindowStateException thrown by the
 * setWindowState() method when setting a window state that was not
 * declared in the deployment descriptor.
 */
public class SetUndeclaredWindowStateTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "SetUndeclaredWindowStateTest";

    public void processAction(ActionRequest request,
                              ActionResponse actionResponse)
        throws PortletException, IOException {

        PortletSession session = request.getPortletSession();

        try {
            actionResponse.setWindowState(new WindowState("INVALID"));

            session.setAttribute(CommonConstants.ERROR_MESSAGE,
                                 "WindowStateException was not thrown "
                                 + "when setting a window state that "
                                 + "was not declared in the deployment "
                                 + "descriptor."); 
        } catch (IllegalStateException e) {
            session.setAttribute(CommonConstants.ERROR_MESSAGE,
                                 "IllegalStateException was thrown when "
                                 + "setting window state to INVALID");
        } catch (WindowStateException e) {
        }
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
            PortletSession session = request.getPortletSession();

            String message
                = (String)session.getAttribute(CommonConstants.ERROR_MESSAGE);

            if (message == null) {
                resultWriter.setStatus(ResultWriter.PASS);
            } else {
                session.removeAttribute(CommonConstants.ERROR_MESSAGE);
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail(message);
            }

            out.println(resultWriter.toString());            
        }
    }
}
