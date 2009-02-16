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
 * This class tests the setContentType() method with an invalid content type.
 */
public class SetInvalidContentTypeTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "SetInvalidContentTypeTest";

    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        String invalidContentType = "anObviouslyInvalidContentType";

        try {
            response.setContentType(invalidContentType);
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail(invalidContentType + " is allowed!");
        } catch (IllegalArgumentException e) {
            resultWriter.setStatus(ResultWriter.PASS);
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println(resultWriter.toString());
    }
}
