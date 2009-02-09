/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.RenderResponse;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests the encodeURL() method.
 */
public class EncodeURLTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "EncodeURLTest";

    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        String path = request.getContextPath();

        if (path.length() == 0) {
            // The portlet application is rooted at the base of the
            // web server URL namespace, hence
            // RenderRequest.getContextPath() returned an empty string.
            path = "/";
        }

        try {
            String result = response.encodeURL(path);

            if (result != null) {
                resultWriter.setStatus(ResultWriter.PASS);
            } else {
                resultWriter.setStatus(ResultWriter.FAIL);

                resultWriter.addDetail("RenderResponse.encodeURL(\""
                                       + path + "\") returned null.");
            }
        } catch (IllegalArgumentException e) {
            resultWriter.setStatus(ResultWriter.FAIL);

            resultWriter.addDetail("IllegalArgumentException was thrown by "
                                   + "RenderResponse.encodeURL(\""
                                   + path + "\").");
        }

        out.println(resultWriter.toString());
    }
}
