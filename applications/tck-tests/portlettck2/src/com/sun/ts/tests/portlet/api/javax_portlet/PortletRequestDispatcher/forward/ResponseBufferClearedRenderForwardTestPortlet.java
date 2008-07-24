/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.forward;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class ResponseBufferClearedRenderForwardTestPortlet extends AbstractRenderForwardTestPortlet{
	
	public static final String TEST_NAME = 
		"ResponseBufferClearedRenderForwardTest";
	
	private static final String SERVLET_PATH = 
		"/ResponseBufferClearedForwardTestServlet";

	
	
	
	/* (non-Javadoc)
	 * @see com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.forward.AbstractRenderForwardTestPortlet#getServletPath()
	 */
	@Override
	protected String getServletPath() {
		return SERVLET_PATH;
	}




	/* (non-Javadoc)
	 * @see com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.forward.AbstractRenderForwardTestPortlet#getTestName()
	 */
	@Override
	protected String getTestName() {
		return TEST_NAME;
	}



	/* (non-Javadoc)
	 * @see com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.forward.AbstractRenderForwardTestPortlet#prepareForward(javax.portlet.PortletSession, javax.portlet.RenderRequest, javax.portlet.RenderResponse)
	 */
	@Override
	protected void prepareForward(PortletSession session,
			RenderRequest request, RenderResponse response) throws IOException{
		
		super.prepareForward(session, request, response);

		response.setContentType("text/html");

		if (response.getBufferSize() > 0){
			PrintWriter out = response.getWriter();
			ResultWriter resultWriter = new ResultWriter(TEST_NAME);
			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("Buffer has been committed.");
			resultWriter.addDetail("The Buffer must be cleared before the " +
			"servlet's service method is called");
			out.println(resultWriter.toString());
		}
		else{
			PrintWriter out = response.getWriter();
			ResultWriter resultWriter = new ResultWriter(TEST_NAME);
			resultWriter.setStatus(ResultWriter.PASS);
			out.println(resultWriter.toString());
		}

	}
   
}
