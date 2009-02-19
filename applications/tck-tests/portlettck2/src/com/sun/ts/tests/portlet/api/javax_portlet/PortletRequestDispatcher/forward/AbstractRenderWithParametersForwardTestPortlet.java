/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher.forward;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.portlet.PortletException;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

/**
 * Abstract parent class for all test portlets that have to use 
 * the forward() method in render() with render parameters set.
 * 
 * For use with a test servlet that is a subclass 
 * of <code>AbstractTestServlet</code> 
 * 
 * @author Oliver Spindler <olisp_suhl@yahoo.com>
 */
public abstract class AbstractRenderWithParametersForwardTestPortlet 
	extends AbstractRenderForwardTestPortlet {
   
	/**
	 * Returns the parameter map for the renderURL.
	 * 
	 * @return
	 */
	protected abstract Map<String, String[]> getParametersMap();
	
	
    /*
     * Creates and writes out an ActionURL with the parameters supplied 
     * by getParametersMap() when invoked the first time.
     * Otherwise forwards the request to <code>super.render()</code>.
     */
	@Override
    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {
        
        RequestCount reqCount = 
        	new RequestCount(request, response, 
        			RequestCount.MANAGED_VIA_SESSION);

        if (reqCount.isFirstRequest()) {//create renderURL with parameters
        	
        	response.setContentType("text/html");
        	
        	PrintWriter out = response.getWriter();
        	
        	PortletURLTag urlTag = new PortletURLTag();
			PortletURL renderURL = response.createRenderURL();
			renderURL.setParameters(getParametersMap());
			urlTag.setTagContent(renderURL.toString());
			
			out.println(urlTag.toString());
        }        
        else{
        	super.render(request, response);
        }
       
    }
}
