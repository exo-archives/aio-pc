/**
 * Copyright 2007 IBM Corporation.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletFilter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceURL;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

/**
 * This class is for have a portlet for the doFilter call.
 * @author <a href="mailto:dettborn@minet.uni-jena.de">Torsten Dettborn</a>
 */
public class InterfaceFilterTestPortlet extends GenericPortlet {
	public static final String TEST_NAME = "InterfaceFilterTest";
	
	private static final String TEST_FILTER = "filter";
	private static final String RENDER_FILTER = "render_filter";
	private static final String ACTION_FILTER = "action_filter";
	private static final String EVENT_FILTER = "event_filter";
	private static final String RESOURCE_FILTER = "resource_filter";
	private static final String TEST_RESULT = "result";
	private static final String TEST_PASS = "pass";
	private static final String TEST_FAIL = "fail";
	@Override
    public void init(PortletConfig config) throws PortletException {
        super.init(config);
        
    }

    @Override
    public void render(RenderRequest request, RenderResponse response) 
        throws PortletException, IOException {
    	
		
    	response.setContentType("text/html");    
        
        RequestCount requestCount
        	= new RequestCount(request, response,
       						RequestCount.MANAGED_VIA_SESSION);
        
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        //for the first request to get the URL's
        if (requestCount.isFirstRequest())
        {
        	PortletSession session = request.getPortletSession();
        	String result = (String)session.getAttribute(TEST_FILTER);
        	if (result == null){
        		resultWriter.setStatus(ResultWriter.FAIL);
    	        resultWriter.addDetail("There is no filter called.");
    	        out.println(resultWriter.toString());
        	}
        	else if (result.equals(RENDER_FILTER)){
        		//create actionURL for action and event filter test
            	PortletURLTag urlTag = new PortletURLTag();
    			PortletURL actionURL = response.createActionURL();
    			urlTag.setTagContent(actionURL.toString());
    			out.println(urlTag.toString());
        		resultWriter.setStatus(ResultWriter.PASS);
        		out.println(resultWriter.toString());
        	}
        	else{
        		resultWriter.setStatus(ResultWriter.FAIL);
    	        resultWriter.addDetail("Wrong filter interface is called.");
    	        resultWriter.addDetail("actual result: "+result);
    	        resultWriter.addDetail("expected result: "+RENDER_FILTER);
    	        out.println(resultWriter.toString());
        	}
        }
        else if (requestCount.getRequestNumber() == 1){
        	//evaluate action/event request
        	PortletSession session = request.getPortletSession();
        	String result = (String)session.getAttribute(TEST_FILTER);
        	if (result == null){
        		resultWriter.setStatus(ResultWriter.FAIL);
    	        resultWriter.addDetail("There is no filter called.");
    	        out.println(resultWriter.toString());
        	}
        	else if (result.equals(ACTION_FILTER)){
        		PortletURLTag urlTag = new PortletURLTag();
    			ResourceURL resourceURL = response.createResourceURL();
    			urlTag.setTagContent(resourceURL.toString());
    			out.println(urlTag.toString());
        		resultWriter.setStatus(ResultWriter.PASS);
        		out.println(resultWriter.toString());
        	}
        	else{
        		resultWriter.setStatus(ResultWriter.FAIL);
    	        resultWriter.addDetail("Wrong filter interface is called.");
    	        resultWriter.addDetail("actual result: "+result);
    	        resultWriter.addDetail("expected result: "+RENDER_FILTER);
    	        out.println(resultWriter.toString());
        	}
        }
        PortletSession session = request.getPortletSession();
		session.removeAttribute(TEST_FILTER);
    }

	@Override
	public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {
		
	}

	@Override
	public void serveResource(ResourceRequest request, ResourceResponse response) throws PortletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
		PortletSession session = request.getPortletSession();
    	String result = (String)session.getAttribute(TEST_FILTER);
    	if (result == null){
    		resultWriter.setStatus(ResultWriter.FAIL);
	        resultWriter.addDetail("There is no filter called.");
	        out.println(resultWriter.toString());
    	}
    	else if (result.equals(RESOURCE_FILTER)){
    		resultWriter.setStatus(ResultWriter.PASS);
    		out.println(resultWriter.toString());
    	}
    	else{
    		resultWriter.setStatus(ResultWriter.FAIL);
	        resultWriter.addDetail("Wrong filter interface is called.");
	        resultWriter.addDetail("actual result: "+result);
	        resultWriter.addDetail("expected result: "+RENDER_FILTER);
	        out.println(resultWriter.toString());
    	}
	}
    
}
