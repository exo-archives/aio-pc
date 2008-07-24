/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.forward;

import java.io.IOException;
import java.io.PrintWriter;

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
 * @author Oliver Spindler <olisp_suhl@yahoo.com> (since JSR 268)
 */
public class PathNotSetActionForwardTestPortlet 
	extends GenericPortlet {
    
	private final String TEST_NAME = 
		"PathNotSetActionForwardTest";
	
	private final String SERVLET_NAME = 
		"PathNotSetForwardTestServlet";
	
	//session attribute names
	private final String TEST_NAME_ATTR = "testname";
	private final String RESULT_ATTR = "result";	
	private final String PRINT_RESULT_ATTR = "printResult";
	
    
    /**
     * Prepares the forward e.g. sets attribute in the session.
     * 
     * @param session 
     * @param request 
     * @param response 
     */
    private void prepareForward(PortletSession session, 
    		ActionRequest request, ActionResponse response){
    	
		//write the test name to the session
        session.setAttribute(TEST_NAME_ATTR, TEST_NAME, 
        		PortletSession.APPLICATION_SCOPE);
        
        //servlet saves result in the session 
        session.setAttribute(PRINT_RESULT_ATTR, false, 
        		PortletSession.APPLICATION_SCOPE);
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

		PortletSession session = request.getPortletSession(true);
		
        PortletRequestDispatcher dispatcher =
        	getPortletContext().getNamedDispatcher(SERVLET_NAME);

        if (dispatcher == null) {
            ResultWriter resultWriter = new ResultWriter(TEST_NAME);
            resultWriter.setStatus(ResultWriter.FAIL);

            resultWriter.addDetail("Cannot get PortletRequestDispatcher for "
                                   + SERVLET_NAME);

            session.setAttribute(RESULT_ATTR, resultWriter.toString(), 
            		PortletSession.APPLICATION_SCOPE);
        } 
        else {
        	
            //save data for the servlet in the session
            prepareForward(session, request, response);
        	
            dispatcher.forward(request, response);
            
        }
		
	}
    
    
}
