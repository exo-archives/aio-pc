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

public class GetMethodTestPortlet extends GenericPortlet {
	public static String TEST_NAME = "GetMethodTest";
	
	public void render(RenderRequest request, RenderResponse response )
		throws PortletException, IOException {
		
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		
		PortletURLTag urlTag = new PortletURLTag();
		ResourceURL resourceURL = response.createResourceURL();
		urlTag.setTagContent(resourceURL.toString());
		
		out.println(urlTag.toString());
	}

    public void serveResource(ResourceRequest request, ResourceResponse response)
		throws PortletException, IOException {
    	
    	response.setContentType("text/html");
    	PrintWriter out = response.getWriter();
    	ResultWriter resultWriter;
    	if (request.getMethod().equals("GET")){
    		resultWriter = new ResultWriter(TEST_NAME);
        	resultWriter.setStatus(ResultWriter.PASS);
    	}
    	else{
    		resultWriter = new ResultWriter(TEST_NAME);
        	resultWriter.setStatus(ResultWriter.FAIL);
        	resultWriter.addDetail("The result for getMethod() don't return GET as expected the value is:"
        			+ request.getMethod() );
    	}
    	out.println(resultWriter.toString());
    }
}
