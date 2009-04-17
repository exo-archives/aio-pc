/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletContext;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

public class GetIncludedServlet extends GenericServlet {
    public static final String TEST_NAME = GetNamedDispatcherTestPortlet.TEST_NAME;

    public void service(ServletRequest request, ServletResponse response)
        throws ServletException, IOException {

        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        String expectedResult = TEST_NAME;
        String result = (String)request.getAttribute(TEST_NAME);

        if ((result != null) && result.equals(expectedResult)) {
            resultWriter.setStatus(ResultWriter.PASS);
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("Expected result = " + expectedResult);

            resultWriter.addDetail("Actual result = "
                                   + ((result == null) ? "null" : result));
        }

        response.getWriter().println(resultWriter.toString());
    }
}
