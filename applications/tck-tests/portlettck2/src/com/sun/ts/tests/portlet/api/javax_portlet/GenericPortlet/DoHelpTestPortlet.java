/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.GenericPortlet;

import java.io.IOException;
import java.io.PrintWriter;

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
 * This class tests that the default implementation of
 * GenericPortlet.doHelp() throws an exception.
 */
public class DoHelpTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "DoHelpTest";

    protected void doView(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        PortletSession session = request.getPortletSession();

        RequestCount requestCount
            = new RequestCount(request, response,
                               RequestCount.MANAGED_VIA_SESSION);

        if (requestCount.isFirstRequest()) {
            PortletURL url = response.createRenderURL();

            if (request.isPortletModeAllowed(PortletMode.HELP)) {
                try {
                    url.setPortletMode(PortletMode.HELP);
                    session.setAttribute("portletModeSet", Boolean.TRUE);
                } catch (PortletModeException e) {
                    resultWriter.setStatus(ResultWriter.FAIL);

                    resultWriter.addDetail("PortletModeException was "
                                           + "thrown when setting "
                                           + "portlet mode to HELP.");

                    out.println(resultWriter.toString());
                }
            }

            PortletURLTag urlTag = new PortletURLTag();
            urlTag.setTagContent(url.toString());        
            out.println(urlTag.toString());
        } else {
            if (session.getAttribute("portletModeSet") == null) {
                resultWriter.setStatus(ResultWriter.PASS);
            } else {
                session.removeAttribute("portletModeSet"); // clean up
                resultWriter.setStatus(ResultWriter.FAIL);

                resultWriter.addDetail("doView() is called when the "
                                       + "portlet mode is HELP.");
            }

            out.println(resultWriter.toString());
        }
    }

    protected void doHelp(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        try {
            super.doHelp(request, response);
            resultWriter.setStatus(ResultWriter.FAIL);

            resultWriter.addDetail("The default implementation of "
                                   + "GenericPortlet.doHelp() doesn't "
                                   + "throw an exception.");
        } catch (Exception e) {
            resultWriter.setStatus(ResultWriter.PASS);
        }

        out.println(resultWriter.toString());
    }
}
