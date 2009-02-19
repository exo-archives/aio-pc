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
 * This class tests the resetBuffer() method.
 */
public class ResetBufferTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "ResetBufferTest";

    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        int bufferSize = response.getBufferSize();

        if (bufferSize == 0) {  // no buffering is used
            resultWriter.setStatus(ResultWriter.PASS);
        } else if (bufferSize > 0) {
            resultWriter.setStatus(ResultWriter.FAIL);

            resultWriter.addDetail("RenderResponse.resetBuffer() did not clear "
                                   + "the data that exist in the buffer.");

            String failedResult = resultWriter.toString();
            resultWriter = new ResultWriter(TEST_NAME);
            boolean continued = true;

            try {
                // Make sure the response isn't committed because of a 
                // filled buffer.
                response.setBufferSize(failedResult.getBytes().length + 1);
            } catch (IllegalStateException e) {
                continued = false;
                resultWriter.setStatus(ResultWriter.FAIL);

                resultWriter.addDetail("RenderResponse.setBufferSize() "
                                       + "threw IllegalStateException.");
            }

            if (continued) {
                // This failed result should be cleared by the
                // resetBuffer() call below.
                out.print(failedResult);

                try {
                    response.resetBuffer();
                    resultWriter.setStatus(ResultWriter.PASS);
                } catch (IllegalStateException e) {
                    resultWriter.setStatus(ResultWriter.FAIL);

                    resultWriter.addDetail("RenderResponse.resetBuffer() "
                                           + "threw IllegalStateException.");
                }
            }
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);

            resultWriter.addDetail("RenderResponse.getBufferSize() "
                                   + "returns " + bufferSize);
        }

        out.println(resultWriter.toString());
    }
}
