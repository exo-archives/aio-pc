/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.forward;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * Abstract parent class for all test portlets that have to use 
 * the forward() method in render().
 * 
 * For use with a test servlet that is a subclass 
 * of <code>AbstractTestServlet</code> 
 * 
 * @author Oliver Spindler <olisp_suhl@yahoo.com>
 */
public abstract class AbstractRenderForwardTestPortlet 
	extends GenericPortlet {
   
    //session attribute names
	protected final String TEST_NAME_ATTR = "testname";	
	protected final String RESULT_ATTR = "result";	
	protected final String PRINT_RESULT_ATTR = "printResult";
	
	
	/**
	 * Returns the path of the servlet to forward to.
	 * 
	 * @return the servlet path.
	 */
	protected abstract String getServletPath();
    
	
	/**
	 * Returns the current name of the test.
	 * 
	 * @return the test name.
	 */
    protected abstract String getTestName();
    
    
    /**
     * Perpares the session before the servlet will be included.
     * 
     * @param session 
     * @param request 
     */
    protected void prepareForward(PortletSession session, 
    		RenderRequest request, RenderResponse response) throws IOException{
    	
    	//write the test name to the session
        session.setAttribute(TEST_NAME_ATTR, getTestName(), 
        		PortletSession.APPLICATION_SCOPE);
        
        //servlet writes the result  
        session.setAttribute(PRINT_RESULT_ATTR, isServletPrintingResults(), 
        		PortletSession.APPLICATION_SCOPE);    	
    }
    
    
    /**
     * Checks the servlet results if needed.
     * 
     * @param session 
     * @param request 
     */
    protected void checkForwardResults(PortletSession session, 
    		RenderRequest request){
    	
    	//cleanup session
    	session.removeAttribute(TEST_NAME_ATTR, 
    			PortletSession.APPLICATION_SCOPE);
    	
    	session.removeAttribute(PRINT_RESULT_ATTR, 
    			PortletSession.APPLICATION_SCOPE);
    }
    
    
    /**
     * Returns if the servlet should print the test result  
     * or save it in the session.
     * 
     * @return 
     */
    protected boolean isServletPrintingResults(){
    	return true;
    }
	
    
    @Override
    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        String servletPath = getServletPath();
        
		String testName = getTestName();
		
		PortletSession session = request.getPortletSession(true);
		
		String result = null;
		
		if(!isServletPrintingResults()){
			//get result from session
			 result = (String)session.getAttribute(RESULT_ATTR, 
					PortletSession.APPLICATION_SCOPE);
			 
			 session.removeAttribute(RESULT_ATTR, 
					 PortletSession.APPLICATION_SCOPE);
		}
		
		if(result != null){
			response.setContentType("text/html");
			
			PrintWriter out = response.getWriter();
			out.println(result);
		}
		else{             
			PortletRequestDispatcher dispatcher = 
				getPortletContext().getRequestDispatcher(servletPath);

			if (dispatcher == null) {
				response.setContentType("text/html");        	        	

				PrintWriter out = response.getWriter();
				
				ResultWriter resultWriter = new ResultWriter(testName);
				resultWriter.setStatus(ResultWriter.FAIL);
				resultWriter.addDetail("Cannot get PortletRequestDispatcher for "
						+ servletPath);

				out.println(resultWriter.toString());
			} 
			else {
				prepareForward(session, request, response);
				
				try{
					dispatcher.forward(request, response);

					checkForwardResults(session, request);
				}
				catch(IllegalStateException e){}
			}
		}
       
    }
}
