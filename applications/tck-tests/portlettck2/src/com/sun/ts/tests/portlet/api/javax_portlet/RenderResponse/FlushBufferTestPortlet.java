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
 * This class tests the flushBuffer() method.
 */
public class FlushBufferTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "FlushBufferTest";

    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        int bufferSize = response.getBufferSize();

        if (bufferSize == 0) {  // no buffering is used
            resultWriter.setStatus(ResultWriter.PASS);
        } else if (bufferSize > 0) {
            out.println(" ");
            response.flushBuffer();

            if (response.isCommitted()) {
                resultWriter.setStatus(ResultWriter.PASS);
            } else {
                resultWriter.setStatus(ResultWriter.FAIL);

                resultWriter.addDetail("The response was not committed "
                                       + "after calling "
                                       + "RenderResponse.flushBuffer().");
            }
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);

            resultWriter.addDetail("RenderResponse.getBufferSize() "
                                   + "returns " + bufferSize);
        }

        out.println(resultWriter.toString());
    }
}
