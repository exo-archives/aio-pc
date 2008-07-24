/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.ts.tests.portlet.api.javax_portlet.PortletURL;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletURL;
import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.PortletSession;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.PrintWriter;
import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;


/**
 *	This class uses PortletURL.setPortletMode() to set a portlet mode that is 
 *  supported by this portlet. 
 */

public class SetPortletModeTestPortlet extends GenericPortlet {

    public static String TEST_NAME="SetPortletModeTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        PortletSession session = request.getPortletSession();
        RequestCount reqCount = new RequestCount(request, response,
                                                 RequestCount.MANAGED_VIA_SESSION);

        if (reqCount.isFirstRequest()) {
            PortletMode portletModeSet = null;
            PortletURLTag customTag = new PortletURLTag();
            PortletURL portletURL = response.createActionURL();

            try {
                if (request.isPortletModeAllowed(PortletMode.EDIT)) {
                    portletModeSet = PortletMode.EDIT;
                    portletURL.setPortletMode(PortletMode.EDIT);
                } else if (request.isPortletModeAllowed(PortletMode.HELP)) {
                    portletModeSet = PortletMode.HELP;
                    portletURL.setPortletMode(PortletMode.HELP);
                } else if (request.isPortletModeAllowed(PortletMode.VIEW)) {
                    portletModeSet = PortletMode.VIEW;
                    portletURL.setPortletMode(PortletMode.VIEW);
                }

                session.setAttribute("portletModeSet", portletModeSet);
                customTag.setTagContent(portletURL.toString());
                out.println(customTag.toString());
            } catch (PortletModeException e) {
                resultWriter.setStatus(ResultWriter.FAIL);

                resultWriter.addDetail("PortletModeException was thrown "
                                       + "when setting portlet mode to "
                                       + portletModeSet.toString());

                out.println(resultWriter.toString());
            }
        }
        else {
            out.println(session.getAttribute("resultSetPortletModeTest"));
        }
    }

    public void processAction(ActionRequest request, ActionResponse response ) throws PortletException, IOException {
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        PortletSession session = request.getPortletSession();

        PortletMode expectedResult
            = (PortletMode)session.getAttribute("portletModeSet");

        PortletMode result = request.getPortletMode();

        if (expectedResult == null) {
            resultWriter.setStatus(ResultWriter.PASS);
        } else {
            session.removeAttribute("portletModeSet"); // clean up

            if ((result != null) && result.equals(expectedResult)) {
                resultWriter.setStatus(ResultWriter.PASS);
            } else {
              	resultWriter.setStatus(ResultWriter.FAIL);
              	resultWriter.addDetail("Expected result = " + expectedResult);
             	resultWriter.addDetail("Actual result = " + result);
            }
        }

        session.setAttribute("resultSetPortletModeTest", resultWriter.toString());
    }
}
