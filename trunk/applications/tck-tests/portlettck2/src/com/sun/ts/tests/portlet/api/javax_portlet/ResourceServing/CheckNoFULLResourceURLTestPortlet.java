package com.sun.ts.tests.portlet.api.javax_portlet.ResourceServing;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceURL;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

public class CheckNoFULLResourceURLTestPortlet extends GenericPortlet {
public static String TEST_NAME = "CheckNoFULLResourceURLTest";
	
	public void render(RenderRequest request, RenderResponse response )
			throws PortletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		RequestCount requestCount = new RequestCount(request, response,
	                           					RequestCount.MANAGED_VIA_SESSION);
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
		
		boolean testFail = false;
		try{
			PortletURL actionURL = response.createActionURL();
			testFail = true;
			writer.setStatus(ResultWriter.FAIL);
			writer.addDetail("Creating an ActionURL throws no IllegalStateException.");
		}catch(IllegalStateException e){
		}
		try{
			PortletURL renderURL = response.createRenderURL();
			testFail = true;
			writer.setStatus(ResultWriter.FAIL);
			writer.addDetail("Creating an RenderURL throws no IllegalStateException.");
		}catch(IllegalStateException e){
		}
		if (!testFail)
			writer.setStatus(ResultWriter.PASS);
		
		
		out.println(writer.toString());
	}
}
