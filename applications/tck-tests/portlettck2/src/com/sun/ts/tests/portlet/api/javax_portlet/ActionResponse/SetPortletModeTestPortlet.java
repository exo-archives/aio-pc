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
import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.PortletSession;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

/**
 * This class tests the setPortletMode() method.
 */
public class SetPortletModeTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "SetPortletModeTest";

    public void processAction(ActionRequest request,
                              ActionResponse actionResponse)
        throws PortletException, IOException {

        PortletMode portletModeSet = null;
        PortletSession session = request.getPortletSession();

        try {
            if (request.isPortletModeAllowed(PortletMode.EDIT)) {
                portletModeSet = PortletMode.EDIT;
                actionResponse.setPortletMode(PortletMode.EDIT);
            } else if (request.isPortletModeAllowed(PortletMode.HELP)) {
                portletModeSet = PortletMode.HELP;
                actionResponse.setPortletMode(PortletMode.HELP);
            } else if (request.isPortletModeAllowed(PortletMode.VIEW)) {
                portletModeSet = PortletMode.VIEW;
                actionResponse.setPortletMode(PortletMode.VIEW);
            }

            session.setAttribute("portletModeSet", portletModeSet);
        } catch (PortletModeException e) {
            session.setAttribute(CommonConstants.ERROR_MESSAGE,
                                 "PortletModeException was thrown when "
                                 + "setting portlet mode to "
                                 + portletModeSet.toString());
        } catch (IllegalStateException e) {
            session.setAttribute(CommonConstants.ERROR_MESSAGE,
                                 "IllegalStateException was thrown when "
                                 + "setting portlet mode to "
                                 + portletModeSet.toString());
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
                PortletMode expectedResult
                    = (PortletMode)session.getAttribute("portletModeSet");

                PortletMode result = request.getPortletMode();
                session.removeAttribute("portletModeSet");

                if ((expectedResult == null) || result.equals(expectedResult)) {
                    resultWriter.setStatus(ResultWriter.PASS);
                } else {
                    resultWriter.setStatus(ResultWriter.FAIL);
                    resultWriter.addDetail("Portlet mode set incorrectly:");

                    resultWriter.addDetail("Expected result = "
                                           + expectedResult.toString());

                    resultWriter.addDetail("Actual result = "
                                           + result.toString());
                }
            } else {
                session.removeAttribute(CommonConstants.ERROR_MESSAGE);
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail(message);
            }

            out.println(resultWriter.toString());            
        }
    }
}
