/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.RenderRequest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests the IllegalArgumentException thrown by the
 * getAttribute() method.
 */
public class GetAttributeIllegalArgumentExceptionTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "GetAttributeIllegalArgumentExceptionTest";

    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        try {
            request.getAttribute(null);
            resultWriter.setStatus(ResultWriter.FAIL);

            resultWriter.addDetail("IllegalArgumentException was not "
                                   + "thrown when name was null.");
        } catch (IllegalArgumentException e) {
            resultWriter.setStatus(ResultWriter.PASS);
        }

        out.println(resultWriter.toString());
    }
}
