/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.ts.tests.portlet.api.javax_portlet.PortletSessionPrivatescope;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletSession;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.PrintWriter;

import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;
import com.sun.ts.tests.portlet.common.util.ResultWriter;



public class CheckSessionAttrInPrivateScopeTest_1_Portlet extends GenericPortlet{

    public static String TEST_NAME="CheckSessionAttrInPrivateScopeTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        RequestCount reqCount = new RequestCount(request, response, 
                                    RequestCount.MANAGED_VIA_SESSION);

        if (!reqCount.isFirstRequest()) {
            ResultWriter resultWriter = new ResultWriter(TEST_NAME);
            PortletSession session = request.getPortletSession();

            String testName = (String)session.getAttribute("testName",PortletSession.PORTLET_SCOPE);

            if (testName != null) {
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail("Should not have obtained an " 
                    + " attribute set "
                    + " by another portlet in the PORTLET_SCOPE mode");
            } else {
                resultWriter.setStatus(ResultWriter.PASS);
            }
            out.println(resultWriter.toString());
        }
    }
}
