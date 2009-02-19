/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.ActionResponse;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;
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
	
	private static final String RESULT =
		"result";
	
	public void processAction(ActionRequest request, ActionResponse response)
			throws PortletException, IOException {
		
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
		
		request.getPortletSession(true).setAttribute(RESULT, resultWriter.toString());
		
	}
	
	public void render(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		
		RequestCount requestCount = new RequestCount(
											request,
											response,
											RequestCount.MANAGED_VIA_SESSION);
		
		if (requestCount.isFirstRequest()) {
			
			String namespace = response.getNamespace();
			
			PortletURLTag urlTag = new PortletURLTag();
			
			PortletURL portletURL = response.createActionURL();
			portletURL.setParameter(NAMESPACE, namespace);
			
			urlTag.setTagContent(portletURL.toString());
			
			out.print(urlTag.toString());
			
		} else {
			
			out.println(request.getPortletSession(true).getAttribute(RESULT));
			
		}
	}

}
