/**
 * Copyright 2007 IBM Corporation.
 */
package com.sun.ts.tests.portlet.api.javax_portlet.Portlet;

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
 * This class tests the serveResource method
 */
public class ServeResourceTestPortlet extends GenericPortlet {
	public static final String TEST_NAME = "ServeResourceTest";

	@Override
    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        ResourceURL url = response.createResourceURL();
        PortletURLTag urlTag = new PortletURLTag();
        urlTag.setTagContent(url.toString());        
        out.println(urlTag.toString());
        
    }
    @Override
	public void serveResource(ResourceRequest request, ResourceResponse response) throws PortletException, IOException {
    	response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        
        resultWriter.setStatus(ResultWriter.PASS);

        out.println(resultWriter.toString());  
        
	}
}
