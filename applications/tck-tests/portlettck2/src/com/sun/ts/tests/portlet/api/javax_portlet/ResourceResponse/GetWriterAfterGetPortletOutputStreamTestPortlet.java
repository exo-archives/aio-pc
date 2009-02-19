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
 * This class tests that IllegalStateException is thrown by the
 * getWriter() method after the getPortletOutputStream() method has
 * been called.
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 * @sinse 2.0
 */

public class GetWriterAfterGetPortletOutputStreamTestPortlet extends
		LogicInServeResourcePortlet {
	
	public static final String TEST_NAME =
		"GetWriterAfterGetPortletOutputStreamTest";

	@Override
	public void serveResource(ResourceRequest request, ResourceResponse response)
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
