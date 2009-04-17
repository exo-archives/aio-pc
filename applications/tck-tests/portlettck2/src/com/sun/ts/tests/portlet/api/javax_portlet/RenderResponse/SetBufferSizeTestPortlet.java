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
 * This class tests the setBufferSize() method.
 */
public class SetBufferSizeTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "SetBufferSizeTest";

    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        int bufferSize = response.getBufferSize();

        if (bufferSize == 0) {  // no buffering is used
            resultWriter.setStatus(ResultWriter.PASS);
        } else if (bufferSize > 0) {
            int preferredBufferSize = bufferSize + 1;

            try {
                response.setBufferSize(preferredBufferSize);

                int foo = response.getBufferSize();
                if (response.getBufferSize() >= preferredBufferSize) {
                    resultWriter.setStatus(ResultWriter.PASS);
                } else {
                    resultWriter.setStatus(ResultWriter.FAIL);

                    resultWriter.addDetail("The buffer size is smaller "
                                           + "than the size requested.");
                }
            } catch (IllegalStateException e) {
                resultWriter.setStatus(ResultWriter.FAIL);

                resultWriter.addDetail("RenderResponse.setBufferSize() "
                                       + "threw IllegalStateException.");
            }
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);

            resultWriter.addDetail("RenderResponse.getBufferSize() "
                                   + "returns " + bufferSize);
        }

        out.println(resultWriter.toString());
    }
}
