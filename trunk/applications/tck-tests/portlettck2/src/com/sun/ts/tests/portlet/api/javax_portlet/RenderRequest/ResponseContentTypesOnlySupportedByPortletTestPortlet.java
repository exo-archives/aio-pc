/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.RenderRequest;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ListCompare;
import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests that the getResponseContentTypes method returns
 * only the content types supported by the current portlet mode of the
 * portlet.
 */
public class ResponseContentTypesOnlySupportedByPortletTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "ResponseContentTypesOnlySupportedByPortletTest";

    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        String[] expectedResult = {"text/html"};
        Enumeration result = request.getResponseContentTypes();

        ListCompare listCompare
            = new ListCompare(expectedResult, result, "text/html",
                              ListCompare.ALL_ELEMENTS_AND_ORDER_MATCH);

        if (listCompare.misMatch()) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail(listCompare.getMisMatchReason());
        } else {
            resultWriter.setStatus(ResultWriter.PASS);
        }

        out.println(resultWriter.toString());
    }
}
