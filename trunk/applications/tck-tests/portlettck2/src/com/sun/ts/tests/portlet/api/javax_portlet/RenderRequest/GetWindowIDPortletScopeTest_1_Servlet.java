/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.RenderRequest;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests the attribute an checks that the getWindowID return
 * value is the same as that one used by the container for scoping
 * session attributes.
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 *
 */

public class GetWindowIDPortletScopeTest_1_Servlet extends HttpServlet {
	
	public static final String TEST_NAME =
		GetWindowIDPortletScopeTestPortlet.TEST_NAME;
	
	public void service(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);
		
		String attributeName = findAttribute(request);
		String value = (String)request.getSession().getAttribute(attributeName);
			
		if (value != null && attributeName != null &&
				attributeName.indexOf(value) != -1) {
			
			resultWriter.setStatus(ResultWriter.PASS);
			
		} else {
			
			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("Cannot find windowID in attribute.");
			
		}
		
		out.println(resultWriter.toString());
		
	}
	
	private String findAttribute(HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		
		String result = null;
		
		if (session != null) {
			
			Enumeration<String> attrNames = session.getAttributeNames(); 
			
			while (attrNames.hasMoreElements()) {
				
				String name = attrNames.nextElement();
				
				if (name.indexOf(TEST_NAME) != -1) {
					result = name;
					break;
				}		
			}
		}
		
		return result;
	}
}
