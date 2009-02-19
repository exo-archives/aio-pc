/*
 * Copyright 2006 IBM Corporation
 */

package com.sun.ts.tests.portlet.ee.taglib;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;

/**
 *
 * Portlet to test if the portlet tag lib created namespace tag 
 * matches to the portlet response getNamespace()-method
 * 
 * @author Fred Thiele <ferdy@informatik.uni-jena.de>
 *
 *
 */
public class NamespaceTagUniquenessTestPortlet extends GenericPortlet {
	
	private final String TEST_NAME = "NamespaceTagUniquenessTest";
	
	private final String SERVLET_NAME="NamespaceTagUniquenessTestServlet";
		
	
	@Override
	public void render(RenderRequest request, RenderResponse response)
		throws PortletException, IOException {

		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);
		
		RequestCount requestCount = new RequestCount(request, response,
				RequestCount.MANAGED_VIA_SESSION);
		
		if (requestCount.isFirstRequest()) {
			
			PortletRequestDispatcher dispatcher = 
				this.getPortletContext().getNamedDispatcher(SERVLET_NAME);
			
			if (dispatcher == null) {
				resultWriter.setStatus(ResultWriter.FAIL);
				resultWriter.addDetail("Cannot get RequestDispatcher for servlet " +
						SERVLET_NAME);
				out.println(resultWriter.toString());
			}
			else {
				dispatcher.include(request, response);
			}
		}
		else{
			out.println(response.getNamespace());
		}
	}
}

