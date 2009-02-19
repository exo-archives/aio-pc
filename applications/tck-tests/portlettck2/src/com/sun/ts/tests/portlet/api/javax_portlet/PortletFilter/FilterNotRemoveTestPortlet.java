/**
 * Copyright 2007 IBM Corporation.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletFilter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

public class FilterNotRemoveTestPortlet extends GenericPortlet{
	public static final String TEST_NAME = "FilterNotRemoveTest";

	public void render(RenderRequest request, RenderResponse response) 
        throws PortletException, IOException {
		response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        boolean fail = false;
        
		
		PortletSession session = request.getPortletSession();
		String result = (String)session.getAttribute("FilterNotRemoveTestOneFilter");
		if (result == null){
			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("Filter has been removed from the for the portlet.");
			fail = true;
		}
		result = (String)session.getAttribute("FilterNotRemoveTestTwoFilter");
		if (result == null){
			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("Filter has been removed from the for the portlet.");
			fail = true;
		}
		result = (String)session.getAttribute("FilterNotRemoveTestThreeFilter");
		if (result == null){
			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("Filter has been removed from the for the portlet.");
			fail = true;
		}
		
		if (!fail)
			resultWriter.setStatus(ResultWriter.PASS);
		out.println(resultWriter.toString());
    }
}
