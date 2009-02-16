/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.Portlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;

/**
 * This class tests that there is only one Portlet object per portlet
 * definition per JVM.
 */
public class OnePortletObjectPerJVMTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "OnePortletObjectPerJVMTest";
    private static int onePerJVMCounter = 0;
    private int onePerPortletCounter = 0;

    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        RequestCount requestCount
            = new RequestCount(request, response,
                               RequestCount.MANAGED_VIA_SESSION);

        if (!requestCount.isFirstRequest()) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            ResultWriter resultWriter = new ResultWriter(TEST_NAME);

            if (++onePerJVMCounter == ++onePerPortletCounter) {
                resultWriter.setStatus(ResultWriter.PASS);
            } else {
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail("The two counter values are different:");
                resultWriter.addDetail("onePerJVMCounter = " + onePerJVMCounter);
                resultWriter.addDetail("onePerPortletCounter = " + onePerPortletCounter);
            }

            out.println(resultWriter.toString());
        }
    }
}
