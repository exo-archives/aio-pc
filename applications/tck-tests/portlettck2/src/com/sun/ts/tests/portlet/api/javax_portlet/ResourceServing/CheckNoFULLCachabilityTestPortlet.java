package com.sun.ts.tests.portlet.api.javax_portlet.ResourceServing;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceURL;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

public class CheckNoFULLCachabilityTestPortlet extends GenericPortlet {
	public static String TEST_NAME = "CheckNoFULLCachabilityTest";
	
	public void render(RenderRequest request, RenderResponse response )
		throws PortletException, IOException {
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		PortletURLTag urlTag = new PortletURLTag();
    	ResourceURL resourceURL = response.createResourceURL();
		resourceURL.setCacheability(ResourceURL.FULL);
		urlTag.setTagContent(resourceURL.toString());
		out.println(urlTag.toString());
	    
	}
	public void serveResource(ResourceRequest request, ResourceResponse response) 
		throws PortletException, IOException {
		
		response.setContentType("text/html");
		PrintWriter out =  response.getWriter();
		ResultWriter writer = new ResultWriter(TEST_NAME);
		PortletURLTag urlTag = new PortletURLTag();
    	ResourceURL resourceURL = response.createResourceURL();
    	boolean testFail = false;
    	
		// Tests to set cachebility to FULL
    	try{
			resourceURL.setCacheability(ResourceURL.FULL);
			}
		catch (IllegalStateException e){
			writer.setStatus(ResultWriter.FAIL);
			writer.addDetail("ResourceURL.FULL must not throw an IllegalStateException.");
			testFail = true;
		}
		
		// Tests to set cachebility to PORTLET		
		try{
			resourceURL.setCacheability(ResourceURL.PORTLET);
			writer.setStatus(ResultWriter.FAIL);
			writer.addDetail("ResourceURL.PORTLET must throw an IllegalStateException.");
			testFail = true;
			}
		catch (IllegalStateException e){	
		}
		
		// Tests to set cachebility to PAGE
		try{
			resourceURL.setCacheability(ResourceURL.PAGE);
			writer.setStatus(ResultWriter.FAIL);
			writer.addDetail("ResourceURL.PORTLET must throw an IllegalStateException.");
			testFail = true;
			}
		catch (IllegalStateException e){	
		}
		
		if (!testFail){
			writer.setStatus(ResultWriter.PASS);
		}
		
		out.println(writer.toString());
	}
}
