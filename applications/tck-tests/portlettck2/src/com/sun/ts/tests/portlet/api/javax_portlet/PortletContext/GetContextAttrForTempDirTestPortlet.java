/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletContext;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.GenericPortlet;
import java.util.Enumeration;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 *	This class uses the getAttribute() method to get the value for the 
 *  attribute "javax.servlet.context.tempdir"
 */

public class GetContextAttrForTempDirTestPortlet extends GenericPortlet {

	static public String TEST_NAME="GetContextAttrForTempDirTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

		ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        PortletContext context = getPortletContext();

        if (context != null) {
            try {
                File tempDir = 
                    (File)context.getAttribute("javax.servlet.context.tempdir");
                resultWriter.setStatus(ResultWriter.PASS);
            } catch(RuntimeException ex) {
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail(
                     "Exception thrown while reading the attribute" +
                     "javax.servlet.context.tempdir" + ex.getMessage());
            }
        } else {
			resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("getPortletContext() returned null");
        }
        out.println(resultWriter.toString());
    }
}
