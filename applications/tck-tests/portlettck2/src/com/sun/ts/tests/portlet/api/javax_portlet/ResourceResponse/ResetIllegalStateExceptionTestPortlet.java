/*
 * Copyright 2007 IBM Corporation
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.ResourceResponse;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests the IllegalStateException thrown
 * by the reset() method.
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 * @since 2.0
 */

public class ResetIllegalStateExceptionTestPortlet extends
		LogicInServeResourcePortlet {
	
	public static final String TEST_NAME =
		"ResetIllegalStateExceptionTest";

	@Override
	public void serveResource(ResourceRequest request, ResourceResponse response)
			throws PortletException, IOException {
		
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        int bufferSize = response.getBufferSize();

        if (bufferSize == 0) {  // no buffering is used
            resultWriter.setStatus(ResultWriter.PASS);
        } else if (bufferSize > 0) {
            out.println(" ");
            response.flushBuffer(); // commit the response

            try {
                response.reset();
                resultWriter.setStatus(ResultWriter.FAIL);

                resultWriter.addDetail("RenderResponse.reset() did not "
                                       + "throw IllegalStateException "
                                       + "after the response has "
                                       + "already been committed.");
            } catch (IllegalStateException e) {
                resultWriter.setStatus(ResultWriter.PASS);
            }
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);

            resultWriter.addDetail("RenderResponse.getBufferSize() "
                                   + "returns " + bufferSize);
        }

        out.println(resultWriter.toString());
	}
}
