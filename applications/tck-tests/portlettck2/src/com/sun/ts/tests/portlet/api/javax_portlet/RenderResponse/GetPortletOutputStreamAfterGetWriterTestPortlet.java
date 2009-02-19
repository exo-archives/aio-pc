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
 * getPortletOutputStream() method after the getWriter() method has
 * been called.
 */
public class GetPortletOutputStreamAfterGetWriterTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "GetPortletOutputStreamAfterGetWriterTest";

    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        try {
            response.getPortletOutputStream();
            resultWriter.setStatus(ResultWriter.FAIL);

            resultWriter.addDetail("IllegalStateException was not "
                                   + "thrown after calling "
                                   + "RenderResponse.getWriter() "
                                   + "followed by "
                                   + "RenderResponse.getPortletOutputStream().");
        } catch (IllegalStateException e) {
            resultWriter.setStatus(ResultWriter.PASS);
        }

        out.println(resultWriter.toString());
    }
}
