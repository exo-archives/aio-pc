/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * TODO:
 * 
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class ResponseBufferClearedForwardTestServlet extends AbstractTestServlet {
	
	private static final long serialVersionUID = 1L;
	
	/* (non-Javadoc)
	 * @see com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.AbstractTestServlet#getTestResult(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected String getTestResult(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		ResultWriter resultWriter = new ResultWriter(testName);
		resultWriter.setStatus(ResultWriter.PASS);
		return resultWriter.toString();
	}

}
