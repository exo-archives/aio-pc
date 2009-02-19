/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.forward;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletSession;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

/**
 * Abstract parent class for all test portlets that have to use 
 * the forward() method in processAction().
 * 
 * For use with a test servlet that is a subclass 
 * of <code>AbstractTestServlet</code> 
 * 
 * @author Oliver Spindler <olisp_suhl@yahoo.com>
 */

public abstract class AbstractActionForwardTestPortlet 
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
     * Prepares the forward e.g. sets attribute in the session.
     * 
     * @param session 
     * @param request 
     * @param response 
     */
    protected void prepareForward(PortletSession session, 
    		ActionRequest request, ActionResponse response){
    	
		//write the test name to the session
        session.setAttribute(TEST_NAME_ATTR, getTestName(), 
        		PortletSession.APPLICATION_SCOPE);
        
        //servlet saves result in the session 
        session.setAttribute(PRINT_RESULT_ATTR, false, 
        		PortletSession.APPLICATION_SCOPE);
    }
    
    /**
     * Checks results after forward() was invoked.
     * 
     * @param session 
     * @param request 
     */
    protected void checkForwardResults(PortletSession session,
    		ActionRequest request){
    	//nothing to do here
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
     * Creates and writes out an ActionURL with the parameters supplied 
     * by getParametersMap() when invoked the first time.
     * Otherwise reads the result from the session and if available 
     * writes it out.
     */
    @Override
    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {
		
		RequestCount requestCount = 
			new RequestCount(request, response,
					RequestCount.MANAGED_VIA_SESSION);
		
		if (requestCount.isFirstRequest()) {
			
			response.setContentType("text/html");
     		
		    PrintWriter out = response.getWriter();
			
			PortletURLTag urlTag = new PortletURLTag();
			PortletURL actionURL = response.createActionURL();
			actionURL.setParameters(getParametersMap());
			urlTag.setTagContent(actionURL.toString());
			
			out.println(urlTag.toString());
	
		}
		else {
			PortletSession session = request.getPortletSession();
			
			//get result from session
			String result = (String)session.getAttribute(RESULT_ATTR,
					PortletSession.APPLICATION_SCOPE);
						
			if(result != null){
				response.setContentType("text/html");
	        		
				PrintWriter out = response.getWriter();
				out.println(result);
			}
			
			//cleanup session
			session.removeAttribute(RESULT_ATTR, 
					PortletSession.APPLICATION_SCOPE);
			
		}
    }
    
    
    /*
     * Obtains a PortletRequestDispatcher. If this succeeded invokes prepareForward(), 
     * dispatcher.forward() and at last checkForwardResults().  
     */
	@Override
	public void processAction(ActionRequest request, 
			ActionResponse response) throws PortletException, IOException {
	
		String servletPath = getServletPath();
		String testName = getTestName();

		PortletSession session = request.getPortletSession(true);
		
        PortletRequestDispatcher dispatcher
            = getPortletContext().getRequestDispatcher(servletPath);

        if (dispatcher == null) {
            ResultWriter resultWriter = new ResultWriter(testName);
            resultWriter.setStatus(ResultWriter.FAIL);

            resultWriter.addDetail("Cannot get PortletRequestDispatcher for "
                                   + servletPath);

            session.setAttribute(RESULT_ATTR,resultWriter.toString(),PortletSession.APPLICATION_SCOPE);
        } 
        else {
        	
            //save data for the servlet in the session
            prepareForward(session, request, response);
        	
            dispatcher.forward(request, response);
            
            //maybe check results from the servlet
            checkForwardResults(session, request);
        }
		
	}
    
    
}
