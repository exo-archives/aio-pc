/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * This class tests the PortletException thrown by the
 * include(PortletRequest, RenderResponse) method.
 */
public class IncludePortletExceptionTestServlet extends GenericServlet {
	
	static final long serialVersionUID=286L;
	

    public void service(ServletRequest request, ServletResponse response)
        throws ServletException, IOException {

        throw new ServletException("IncludePortletExceptionTest");
    }
}
