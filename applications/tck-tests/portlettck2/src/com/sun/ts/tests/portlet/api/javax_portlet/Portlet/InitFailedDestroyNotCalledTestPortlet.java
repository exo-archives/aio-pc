/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.Portlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;

/**
 * This class participates in testing that destroy() is not called if
 * initialization failed.  It looks for a portlet context attribute in
 * render() that was set in the destroy() method of another portlet
 * that threw UnavailableException in init().
 */
public class InitFailedDestroyNotCalledTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "InitFailedDestroyNotCalledTest";

    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        RequestCount requestCount
            = new RequestCount(request, response,
                               RequestCount.MANAGED_VIA_SESSION);

        if (!requestCount.isFirstRequest()) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            ResultWriter resultWriter = new ResultWriter(TEST_NAME);
            PortletContext portletContext = getPortletContext();
            String result = (String)portletContext.getAttribute(TEST_NAME);

            if (result == null) {
                resultWriter.setStatus(ResultWriter.PASS);
            } else {
                portletContext.removeAttribute(TEST_NAME); // cleanup
                resultWriter.setStatus(ResultWriter.FAIL);

                resultWriter.addDetail("destroy() was called after "
                                       + "init() failed.");
            }

            out.println(resultWriter.toString());
        }
    }
}
