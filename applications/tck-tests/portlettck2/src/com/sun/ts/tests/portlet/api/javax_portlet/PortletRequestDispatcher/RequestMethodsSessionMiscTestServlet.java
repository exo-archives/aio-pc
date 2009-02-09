/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests that the getSession() method of
 * the HttpServletRequest provides the functionality
 * defined by the Servlet Specification.
 * 
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class RequestMethodsSessionMiscTestServlet extends AbstractTestServlet {
	
	private static final long serialVersionUID = 286L;
    
	@Override
	protected String getTestResult(HttpServletRequest request, 
			HttpServletResponse response) throws IOException {
		
		ResultWriter resultWriter = new ResultWriter(testName);
		
		resultWriter.setStatus(ResultWriter.PASS);
		
        checkGetSession(request, resultWriter);
        
		return resultWriter.toString();
	}

    private void checkGetSession(HttpServletRequest request,
                ResultWriter resultWriter) {
    	
    	HttpSession session = request.getSession(true);
    	if(session == null){
    		resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("getSession():");
            resultWriter.addDetail("Expected result = non-null instance of HttpSession");
            resultWriter.addDetail("Actual result = null");
    	}
    	
    	else{
    		checkSessionGetAttribute(session, resultWriter);
    		checkSessionSetAttribute(session, resultWriter);
    		checkSessionGetAttributeNames(session, resultWriter);    		
    		checkSessionRemoveAttribute(session, resultWriter);
    	}    	    	
    }
    
    private void checkSessionGetAttribute(HttpSession session, 
    		ResultWriter resultWriter){
    	
    	String expectedResult = "test1";
    	String result = (String)session.getAttribute("test1");
    	
    	if ((result == null) || !result.equals(expectedResult)) {
			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("getSession().getAttribute():");
			resultWriter.addDetail("Expected result = " + expectedResult);
			resultWriter.addDetail("Actual result = " + result);
		}    	
    }
    
    @SuppressWarnings("unchecked")
    private void checkSessionGetAttributeNames(HttpSession session, 
    		ResultWriter resultWriter){
    	
    	Enumeration<String> resultEnumeration =
    		(Enumeration<String>)session.getAttributeNames();
    	
    	if(resultEnumeration == null){
    		resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("getSession().getAttributeNames():");
			resultWriter.addDetail("Expected result = non-null instance of Enumeration");
			resultWriter.addDetail("Actual result = null");
    	}
    	
    	List<String> resultList = new ArrayList<String>();
    	while(resultEnumeration.hasMoreElements()){
    		resultList.add(resultEnumeration.nextElement());
    	}
    	
    	if(!resultList.contains("test1")){
    		resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("getSession().getAttributeNames():");
			resultWriter.addDetail("result enumeration does not contain expected elements");
    	}
    	
    	if(!resultList.contains("test2")){
    		resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("getSession().getAttributeNames():");
			resultWriter.addDetail("result enumeration does not contain expected elements");
    	}
    	
    }
    
    private void checkSessionSetAttribute(HttpSession session, 
    		ResultWriter resultWriter){
    	
    	String expectedResult = "test2";
    	
    	session.setAttribute(expectedResult, expectedResult);
    	    	
    	String result = (String)session.getAttribute(expectedResult);
    	
    	if ((result == null) || !result.equals(expectedResult)) {
			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("getSession().setAttribute():");
			resultWriter.addDetail("Expected result = " + expectedResult);
			resultWriter.addDetail("Actual result = " + result);
		}    	
    }
    
    private void checkSessionRemoveAttribute(HttpSession session, 
    		ResultWriter resultWriter){
    	
    	session.removeAttribute("test1");
    	    	
    	String result = (String)session.getAttribute("test1");
    	
    	if (result != null) {
			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("getSession().removeAttribute():");
			resultWriter.addDetail("Expected result = null");
			resultWriter.addDetail("Actual result = " + result);
		}
    	
    	session.removeAttribute("test2");
    	
    	result = (String)session.getAttribute("test2");
    	
    	if (result != null) {
			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("getSession().removeAttribute():");
			resultWriter.addDetail("Expected result = null");
			resultWriter.addDetail("Actual result = " + result);
		} 
    }

}
