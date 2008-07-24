/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletPreferences;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ReadOnlyException;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests the IllegalArgumentException thrown by the reset()
 * method.
 */
public class ResetIllegalArgumentExceptionTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "ResetIllegalArgumentExceptionTest";

    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        try {
            request.getPreferences().reset(null);
            resultWriter.setStatus(ResultWriter.FAIL);

            resultWriter.addDetail("IllegalArgumentException was not "
                                   + "thrown when key was null.");
        } catch (IllegalArgumentException e) {
            resultWriter.setStatus(ResultWriter.PASS);
        } 
        out.println(resultWriter.toString());
    }
}
