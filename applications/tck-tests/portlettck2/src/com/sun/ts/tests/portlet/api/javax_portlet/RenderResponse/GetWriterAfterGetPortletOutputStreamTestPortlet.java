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
 * This class tests that IllegalStateException is thrown by the
 * getWriter() method after the getPortletOutputStream() method has
 * been called.
 */
public class GetWriterAfterGetPortletOutputStreamTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "GetWriterAfterGetPortletOutputStreamTest";

    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = new PrintWriter(response.getPortletOutputStream());
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        try {
            response.getWriter();
            resultWriter.setStatus(ResultWriter.FAIL);

            resultWriter.addDetail("IllegalStateException was not "
                                   + "thrown after calling "
                                   + "RenderResponse.getPortletOutputStream() "
                                   + "followed by "
                                   + "RenderResponse.getWriter().");
        } catch (IllegalStateException e) {
            resultWriter.setStatus(ResultWriter.PASS);
        }

        out.println(resultWriter.toString());
        out.flush();
    }
}
