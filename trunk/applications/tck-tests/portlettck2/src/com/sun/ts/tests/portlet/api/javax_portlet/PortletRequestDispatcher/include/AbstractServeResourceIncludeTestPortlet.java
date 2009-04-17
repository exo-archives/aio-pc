/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.include;

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
 * Abstract parent class for all test portlets that have to use the 
 * include() method in serveResource().
 * 
 * For use with a test servlet that is a subclass of 
 * <code>AbstractTestServlet</code> 
 * 
 * @author Oliver Spindler <olisp_suhl@yahoo.com>
 */
public abstract class AbstractServeResourceIncludeTestPortlet 
	extends GenericPortlet {
    
    //session attribute names
	protected final String TEST_NAME_ATTR = "testname";	
	protected final String RESULT_ATTR = "result";	
	protected final String PRINT_RESULT_ATTR = "printResult";
    
	
	/**
	 * Returns the path of the servlet to include.
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
     * Prepares the include f.e. sets attribute in the session.
     * 
     * @param session 
     * @param request 
     * @param response TODO
     */
    protected void prepareInclude(PortletSession session, 
    		ResourceRequest request, ResourceResponse response){
    	
		//write the test name to the session
        session.setAttribute(TEST_NAME_ATTR, getTestName(),
        		PortletSession.APPLICATION_SCOPE);
        
        //servlet writes the result  
        session.setAttribute(PRINT_RESULT_ATTR,isServletPrintingResults(),
        		PortletSession.APPLICATION_SCOPE);
    }
    
    
    /**
     * Checks results after include() was invoked.
     * 
     * @param session 
     * @param request 
     */
    protected void checkIncludeResults(PortletSession session, 
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
		
		//create ResourceURL
		PortletURLTag urlTag = new PortletURLTag();
		ResourceURL resourceURL = response.createResourceURL();
		resourceURL.setParameters(getParametersMap());
		urlTag.setTagContent(resourceURL.toString());
		
		out.println(urlTag.toString());	
		
    }

    
    /*
     * Obtains a PortletRequestDispatcher. If this succeeded invokes 
     * prepareInclude(), dispatcher.include() and checkIncludeResults().
     * At last reads and if available writes out the result 
     * if the servlet did not.  
     */
	@Override
	public void serveResource(ResourceRequest request, 
			ResourceResponse response) throws PortletException, IOException {
		
		response.setContentType("text/html");
		
		String servletPath = getServletPath();
		String testName = getTestName();
		
        PortletRequestDispatcher dispatcher =
            getPortletContext().getRequestDispatcher(servletPath);

        if (dispatcher == null) {
        	PrintWriter out = response.getWriter();
        	
            ResultWriter resultWriter = new ResultWriter(testName);
            resultWriter.setStatus(ResultWriter.FAIL);

            resultWriter.addDetail("Cannot get PortletRequestDispatcher for "
                                   + servletPath);

            out.println(resultWriter.toString());
        } else {
    		PortletSession session = request.getPortletSession(true);
            
            //save data for the servlet in the session
            prepareInclude(session, request, response);
        	
            dispatcher.include(request, response);
            
            //maybe check results from the servlet
            checkIncludeResults(session, request);
            
            if(!isServletPrintingResults()){
            	PrintWriter out = response.getWriter();
            	
            	//get result from session
    			String result = (String)session.getAttribute(RESULT_ATTR, 
    					PortletSession.APPLICATION_SCOPE);
    						
    			if(result != null){
    				out.println(result);
    			}
    			
    			//cleanup session
    			session.removeAttribute(RESULT_ATTR, 
    					PortletSession.APPLICATION_SCOPE);
            }
        }	
	}
    
    
}
