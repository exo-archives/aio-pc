/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher;

import java.io.IOException;

import javax.servlet.RequestDispatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests that the getRequestDispatcher() method of the
 * HttpServletRequest provides the functionality defined by the
 * Servlet Specification.
 * 
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class RequestMethodsGetRequestDispatcherTestServlet extends AbstractTestServlet {
	
	private static final long serialVersionUID = 286L;


	@Override
	protected String getTestResult(HttpServletRequest request, 
			HttpServletResponse response) throws IOException {

        ResultWriter resultWriter = new ResultWriter(testName);
        String path = "/Included.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(path);

        if (dispatcher != null) {
            resultWriter.setStatus(ResultWriter.PASS);
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);

            resultWriter.addDetail("Cannot get RequestDispatcher object for "
                                   + path);
        }
        
        return resultWriter.toString();

	}

}
