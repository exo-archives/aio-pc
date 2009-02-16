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
import javax.servlet.http.HttpSession;

/**
 * Abstract parent class for almost all test servlets used in this package.
 * 
 * For use with portlets which are subclasses of the abstract test portlets.
 *  
 * @author Oliver Spindler <olisp_suhl@yahoo.com>
 */
public abstract class AbstractTestServlet extends HttpServlet {
 
	private static final long serialVersionUID = 1L;

	//session attribute names 
	private final String TEST_NAME_ATTR = "testname";
	private final String RESULT_ATTR = "result";
	private final String PRINT_RESULT_ATTR = "printResult";
	
	protected String testName = null;
	protected Boolean writeResult = null;
    	
	/*
	 * Reads the portlet testname and a boolean from the session.
	 * The boolean value indicates whether the servlet
	 * should write out the test results or save it in the session.
	 * Invokes getTestResult() and writes out the result or saves it in the session.
	 */
	@Override
	protected void service(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		
		//get test name
		testName = (String)getAndRemoveSessionAttribute(request, TEST_NAME_ATTR);
		if(testName == null){
			testName = "unnamed";
		}
		
		//write result or save it in the session?
		writeResult = 
			(Boolean)getAndRemoveSessionAttribute(request, PRINT_RESULT_ATTR);
		if(writeResult == null){
			writeResult = true;
		}
		
		//run the tests
		String testResult = getTestResult(request,response);
		
		if(writeResult && (testResult != null)){//write the result
			response.setContentType("text/html");
			
			PrintWriter out = response.getWriter();
	        out.println(testResult);
		}
		else{//save result in the session
			setSessionAttribute(request, RESULT_ATTR, testResult);			
		}
	}
	
	
	/**
	 * Runs the test and returns the result string.
	 * 
	 * @return the test result.
	 */
	protected abstract String getTestResult(HttpServletRequest request, 
			HttpServletResponse response) throws IOException;
	
	
	/**
	 * Helper method that reads a attribute from the session.
	 * 
	 * @param request
	 * @param attributeName
	 * @return
	 */
	protected Object getSessionAttribute(HttpServletRequest request, 
			String attributeName){
		
		HttpSession session = request.getSession();
		
		if(session == null){
			throw new NullPointerException("unable to get session");
		}
		
        Object result = session.getAttribute(attributeName);
        
        return result;
	}
                
	
	/**
	 * Helper method that reads and removes a attribute from the session.
	 * 
	 * @param request
	 * @param attributeName
	 * @return
	 */
	protected Object getAndRemoveSessionAttribute(HttpServletRequest request, 
			String attributeName){
		
		HttpSession session = request.getSession();
		
		if(session == null){
			throw new NullPointerException("unable to get session");
		}
		
        Object result = session.getAttribute(attributeName);
        session.removeAttribute(attributeName);
        
        return result;
	}
	
	
	/**
	 * Helper method that stores a attribute in the session.
	 * 
	 * @param request
	 * @param attributeName
	 * @param data
	 * @return
	 */
	protected void setSessionAttribute(HttpServletRequest request, String 
			attributeName, Object data){
		
		HttpSession session = request.getSession();
		
		if(session == null){
			throw new NullPointerException("unable to get session");
		}
		
        session.setAttribute(attributeName, data);
	}
	

}
