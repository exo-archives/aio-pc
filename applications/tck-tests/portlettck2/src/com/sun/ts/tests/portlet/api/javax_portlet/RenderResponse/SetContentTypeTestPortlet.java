/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.RenderResponse;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests the setContentType() method with a valid content type.
 */
public class SetContentTypeTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "SetContentTypeTest";

    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        String contentType = "text/html";

        try {
            response.setContentType(contentType);
            resultWriter.setStatus(ResultWriter.PASS);
        } catch (IllegalArgumentException e) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail(contentType + " isn't allowed!");
        }

        PrintWriter out = response.getWriter();
        out.println(resultWriter.toString());
    }
}
