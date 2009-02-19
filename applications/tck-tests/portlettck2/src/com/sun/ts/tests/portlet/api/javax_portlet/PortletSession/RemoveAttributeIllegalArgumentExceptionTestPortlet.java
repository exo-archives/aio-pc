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
 * removeAttribute(String) and removeAttribute(String, int) methods.
 */
public class RemoveAttributeIllegalArgumentExceptionTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "RemoveAttributeIllegalArgumentExceptionTest";

    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        resultWriter.setStatus(ResultWriter.PASS);
        PortletSession session = request.getPortletSession();

        try {
            session.removeAttribute(null);
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("PortletSession.removeAttribute(name):");

            resultWriter.addDetail("IllegalArgumentException was not "
                                   + "thrown when name was null.");
        } catch (IllegalArgumentException e) {
        }

        try {
            session.removeAttribute(null, PortletSession.APPLICATION_SCOPE);
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("PortletSession.removeAttribute(name, scope):");

            resultWriter.addDetail("IllegalArgumentException was not "
                                   + "thrown when name was null.");
        } catch (IllegalArgumentException e) {
        }

        out.println(resultWriter.toString());
    }
}
