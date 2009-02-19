/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.forward;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceURL;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

/**
 * Abstract parent class for all test portlets that have to use the forward() method
 * in serveResource().
 * 
 * For use with a test servlet that is a subclass of <code>AbstractTestServlet</code> 
 * 
 * @author Oliver Spindler <olisp_suhl@yahoo.com>
 */
public abstract class AbstractServeResourceForwardTestPortlet extends GenericPortlet {
    
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
     * Prepares the forward e.g. sets attribute in the session.
     * 
     * @param session 
     * @param request 
     * @param response 
     */
    protected void prepareForward(PortletSession session, 
    		ResourceRequest request, ResourceResponse response) throws IOException{
    	
		//write the test name to the session
        session.setAttribute(TEST_NAME_ATTR, getTestName(),
        		PortletSession.APPLICATION_SCOPE);
        
        //servlet writes the result  
        session.setAttribute(PRINT_RESULT_ATTR,isServletPrintingResults(),
        		PortletSession.APPLICATION_SCOPE);
    }
    
    
    /**
     * Checks results after forward() was invoked.
     * 
     * @param session 
     * @param request 
     */
    protected void checkForwardResults(PortletSession session, 
    		ResourceRequest request){
    	//nothing to do here
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
    
    
    /**
	 * Returns the parameter map for the actionURL.
	 * 
	 * @return
	 */
    protected Map<String, String[]> getParametersMap(){
    	return new HashMap<String, String[]>();
    }
    
    
    /*
     * Creates and writes out an ResourceURL with the 
     * parameters supplied by getParametersMap().
     */
    @Override
    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		
		PortletSession session = request.getPortletSession(true);
		
		String result = null;
		
		if(!isServletPrintingResults()){        	
        	//get result from session
			result = (String)session.getAttribute(RESULT_ATTR, 
					PortletSession.APPLICATION_SCOPE);
			//cleanup session
			session.removeAttribute(RESULT_ATTR, 
					PortletSession.APPLICATION_SCOPE);
		}
		
		if(result != null){
			response.setContentType("text/html");			
			out.println(result);
		}
		else{

			//create ResourceURL
			PortletURLTag urlTag = new PortletURLTag();
			ResourceURL resourceURL = response.createResourceURL();
			resourceURL.setParameters(getParametersMap());
			urlTag.setTagContent(resourceURL.toString());

			out.println(urlTag.toString());
		}
		
    }

    
    /*
     * Obtains a PortletRequestDispatcher. If this succeeded invokes 
     * prepareForward(), dispatcher.forward() and checkForwardResults().
     * If the servlet does not print the result an extra trip is needed
     * to print results!
     */
	@Override
	public void serveResource(ResourceRequest request, 
			ResourceResponse response) throws PortletException, IOException {
		
		String servletPath = getServletPath();
		String testName = getTestName();
		
		PortletSession session = request.getPortletSession(true);
		
		response.setContentType("text/html");
		
		String result = null;
		
		if(!isServletPrintingResults()){        	
        	//get result from session
			result = (String)session.getAttribute(RESULT_ATTR, 
					PortletSession.APPLICATION_SCOPE);
			//cleanup session
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

				//save data for the servlet in the session
				prepareForward(session, request, response);

				try{

					dispatcher.forward(request, response);

					//maybe check results from the servlet
					checkForwardResults(session, request);
				}
				catch(IllegalStateException e){}
			}	
		}
	}
    
    
}
