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
import java.io.IOException;
import java.io.PrintWriter;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 *	This class uses the getMimeType() method to get the MIME type and 
 *  compares it with the expected value.
 */

public class GetMimeTypeTestPortlet extends GenericPortlet {

    private static final String EXPECTED_MIME_TYPE = "application/java";
    private static final String MIME_OBJECT =
        "/WEB-INF/classes/com/sun/ts/tests/portlet/api/javax_portlet/PortletContext/GetMimeTypeTestPortlet.class";
	static public String TEST_NAME = "GetMimeTypeTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        PortletContext context = getPortletContext();
        String result = context.getMimeType( MIME_OBJECT );

        if(result != null ) {
            if(result.equalsIgnoreCase( EXPECTED_MIME_TYPE ) ) {
				resultWriter.setStatus(ResultWriter.PASS);
            } else {
				resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail("PortletContext.getMimeType(" 
                           + MIME_OBJECT + ") returned incorrect result "
                           + "Expected result = " + EXPECTED_MIME_TYPE 
                           + "Actual result = " + result); 
            }
        } else {
			resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("PortletContext.getMimeType(" 
                            + MIME_OBJECT + " ) returned a null result"
                            + "Expected result = " + EXPECTED_MIME_TYPE);
        }
		out.println(resultWriter.toString());
    }
}
