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
 * This class tests the getNamespace() method.
 */
public class GetNamespaceTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "GetNamespaceTest";

    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        String result = response.getNamespace();

        if (result != null) {
            String previousResult = result;
            result = response.getNamespace();

            if (previousResult.equals(result)) {
                resultWriter.setStatus(ResultWriter.PASS);
            } else {
                resultWriter.setStatus(ResultWriter.FAIL);

                resultWriter.addDetail("RenderResponse.getNamespace() "
                                       + "did not return the same results: ");

                resultWriter.addDetail("First result = " + previousResult);
                resultWriter.addDetail("Second result = " + result);
            }
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);

            resultWriter.addDetail("RenderResponse.getNamespace() "
                                   + "returns null.");
        }

        out.println(resultWriter.toString());
    }
}
