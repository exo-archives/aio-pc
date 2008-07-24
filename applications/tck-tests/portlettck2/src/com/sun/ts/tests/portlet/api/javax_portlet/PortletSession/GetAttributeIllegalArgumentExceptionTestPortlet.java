/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletSession;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests the IllegalArgumentException thrown by the
 * getAttribute(String) and getAttribute(String, int) methods.
 */
public class GetAttributeIllegalArgumentExceptionTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "GetAttributeIllegalArgumentExceptionTest";

    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        resultWriter.setStatus(ResultWriter.PASS);
        PortletSession session = request.getPortletSession();

        try {
            session.getAttribute(null);
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("PortletSession.getAttribute(name):");

            resultWriter.addDetail("IllegalArgumentException was not "
                                   + "thrown when name was null.");
        } catch (IllegalArgumentException e) {
        }

        try {
            session.getAttribute(null, PortletSession.APPLICATION_SCOPE);
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("PortletSession.getAttribute(name, scope):");

            resultWriter.addDetail("IllegalArgumentException was not "
                                   + "thrown when name was null.");
        } catch (IllegalArgumentException e) {
        }

        out.println(resultWriter.toString());
    }
}
