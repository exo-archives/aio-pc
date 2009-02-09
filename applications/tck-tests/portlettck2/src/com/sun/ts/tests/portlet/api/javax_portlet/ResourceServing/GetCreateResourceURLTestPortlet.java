/*
 * Copyright 2006 IBM Corporation
 */

/**
 * @author Kay Schieck <schieck@inf.uni-jena.de>
 */

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

public class GetCreateResourceURLTestPortlet extends GenericPortlet {
	public static String TEST_NAME = "GetCreateResourceURLTest";
	
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
    	
    	ResultWriter resultWriter = new ResultWriter(TEST_NAME);
    	resultWriter.setStatus(ResultWriter.PASS);
    	
    	out.println(resultWriter.toString());
    }
}