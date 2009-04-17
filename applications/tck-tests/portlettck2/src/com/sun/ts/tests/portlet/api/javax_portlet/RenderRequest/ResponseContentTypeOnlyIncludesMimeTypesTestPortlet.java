/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.RenderRequest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class verify that the content type only includes the
 * mime type.
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 *
 */

public class ResponseContentTypeOnlyIncludesMimeTypesTestPortlet extends
		GenericPortlet {
	
	public static final String TEST_NAME =
		"ResponseContentTypeOnlyIncludesMimeTypesTest";
	
	public void render(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);
	
		String contentType = request.getResponseContentType();
		String characterEncoding = response.getCharacterEncoding();
		
		if (contentType == null) {
			
			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("getResponseContentType() return value is null");
			
		} else {
			
			if (contentType.indexOf(characterEncoding) != -1) {
				
				resultWriter.setStatus(ResultWriter.FAIL);
				resultWriter.addDetail("content type includes character set");
				resultWriter.addDetail("content type:  " + contentType);
				resultWriter.addDetail("character set: " + characterEncoding);
				
			} else
				resultWriter.setStatus(ResultWriter.PASS);
			
		}
		
		out.println(resultWriter.toString());
		
	}
}
