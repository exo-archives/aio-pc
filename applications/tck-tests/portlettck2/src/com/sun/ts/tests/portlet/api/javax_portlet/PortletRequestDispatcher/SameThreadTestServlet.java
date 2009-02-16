/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests that the portlet container ensures that the
 * servlet called through a PortletRequestDispatcher is called in the
 * same thread as the PortletRequestDispatcher include invocation.
 * 
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class SameThreadTestServlet extends AbstractTestServlet {
    
	static final long serialVersionUID=286L;
	
	@Override
	protected String getTestResult(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		ResultWriter resultWriter = new ResultWriter(testName);
		
        String expectedResult = testName;
        String result = SameThreadTestThreadLocalizer.get();

        if ((result != null) && result.equals(expectedResult)) {
            resultWriter.setStatus(ResultWriter.PASS);
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("Expected result = " + expectedResult);
            resultWriter.addDetail("Actual result = " + result);
        }

        return resultWriter.toString();
	}
}
