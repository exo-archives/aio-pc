/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.RenderRequest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class verify that the PortletRequest.getAttribute(PortletRequest.LIFECYCLE_PHASE)
 * method returned the correct value.
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 *
 */

public class GetLifecyclePhaseAttributeTestPortlet extends GenericPortlet {
	
	public static final String TEST_NAME =
		"GetLifecyclePhaseAttributeTest";
	
	public void render(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);
		
		String phase = (String)request.getAttribute(PortletRequest.LIFECYCLE_PHASE);
		
		if (phase != null && phase.equals(PortletRequest.RENDER_PHASE))
			resultWriter.setStatus(ResultWriter.PASS);
		else {
			
			resultWriter.setStatus(ResultWriter.FAIL);
			
			resultWriter.addDetail("PortletRequest.getAttribute(PortletRequest.LIFECYCLE_PHASE)");
			resultWriter.addDetail("not returned PortletRequest.RENDER_PHASE");
			
		}
		
		out.println(resultWriter.toString());
		
	}
}
