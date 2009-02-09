/**
 * Copyright 2007 IBM Corporation.
 */
package com.sun.ts.tests.portlet.api.javax_portlet.GenericPortlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.RenderMode;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

public class AnnotatedDoViewTestPortlet extends GenericPortlet{
	public static final String TEST_NAME = "AnnotatedDoViewTest";

	@RenderMode(name="view") 
    public void testAnnotatedView(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        try {
            super.doView(request, response);
            resultWriter.setStatus(ResultWriter.FAIL);

            resultWriter.addDetail("The default implementation of "
                                   + "GenericPortlet.doView() doesn't "
                                   + "throw an exception.");
        } catch (Exception e) {
            resultWriter.setStatus(ResultWriter.PASS);
        }

        out.println(resultWriter.toString());
    }
}
