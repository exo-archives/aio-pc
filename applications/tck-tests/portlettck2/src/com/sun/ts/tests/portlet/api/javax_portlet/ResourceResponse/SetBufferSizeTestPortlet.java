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
 * This class tests the setBufferSize() method.
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 * @since 2.0
 */

public class SetBufferSizeTestPortlet extends LogicInServeResourcePortlet {
	
	public static final String TEST_NAME =
		"SetBufferSizeTest";

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
            int preferredBufferSize = bufferSize + 1;

            try {
                response.setBufferSize(preferredBufferSize);

                if (response.getBufferSize() >= preferredBufferSize) {
                    resultWriter.setStatus(ResultWriter.PASS);
                } else {
                    resultWriter.setStatus(ResultWriter.FAIL);

                    resultWriter.addDetail("The buffer size is smaller "
                                           + "than the size requested.");
                }
            } catch (IllegalStateException e) {
                resultWriter.setStatus(ResultWriter.FAIL);

                resultWriter.addDetail("ResourceResponse.setBufferSize() "
                                       + "threw IllegalStateException.");
            }
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);

            resultWriter.addDetail("ResourceResponse.getBufferSize() "
                                   + "returns " + bufferSize);
        }

        out.println(resultWriter.toString());
	}
}
