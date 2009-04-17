/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.ResourceResponse;

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

/**
 * This class test the result of response.getNamespace() method.
 * The result value must the same for the lifetime of the portlet window.
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 *
 */

public class GetNamespaceSameValueTestPortlet extends GenericPortlet {

	public static final String TEST_NAME =
		"GetNamespaceSameValueTest";
	
	private static final String NAMESPACE =
		"namespace-key";
	
	public void render(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
	
		String namespace = response.getNamespace();
		
		PortletURLTag urlTag = new PortletURLTag();
		
		ResourceURL resourceURL = response.createResourceURL();
		resourceURL.setParameter(NAMESPACE, namespace);
		
		urlTag.setTagContent(resourceURL.toString());
		
		out.print(urlTag.toString());
			
	}
	
	public void serveResource(ResourceRequest request, ResourceResponse response)
			throws PortletException, IOException {
		
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);
		
		String actual = response.getNamespace();
		String last = request.getParameter(NAMESPACE);
		
		if (actual != null && actual.equals(last))
			resultWriter.setStatus(ResultWriter.PASS);
		else {
			
			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("actual namespace different from last request namespace");
			resultWriter.addDetail("actual :" + actual);
			resultWriter.addDetail("last   :" + last);
							
		}
		
		out.println(resultWriter.toString());
		
	}
}
