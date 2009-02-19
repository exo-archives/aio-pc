/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.forward.ResponseBufferPortletCommitForwardTestPortlet;
import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * TODO:
 * 
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class ResponseBufferPortletCommitForwardTestServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	public static final String TEST_NAME = 
		ResponseBufferPortletCommitForwardTestPortlet.TEST_NAME;

    public void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        resultWriter.setStatus(ResultWriter.FAIL);
        resultWriter.addDetail("The portlet forwarded to the servlet instead of" +
        					   " throwing an IllegalStateException.");
        out.println(resultWriter.toString());
    }
}
