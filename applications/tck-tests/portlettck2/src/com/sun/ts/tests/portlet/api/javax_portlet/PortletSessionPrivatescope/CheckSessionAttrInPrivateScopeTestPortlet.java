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

/*
 * One of the portlets participating in test. On first request, it puts 
 * an attribute in the session. In second, it checks for it.
 */

public class CheckSessionAttrInPrivateScopeTestPortlet extends GenericPortlet {

    public static String TEST_NAME="CheckSessionAttrInPrivateScopeTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        RequestCount reqCount = new RequestCount(request, response, 
                                        RequestCount.MANAGED_VIA_SESSION);

        if (reqCount.isFirstRequest()) {
            PortletSession session = request.getPortletSession();
            session.setAttribute("testName", TEST_NAME, PortletSession.PORTLET_SCOPE);
        }
        else {
            PortletSession session = request.getPortletSession();
            ResultWriter resultWriter = new ResultWriter(TEST_NAME);
            String attr = (String)session.getAttribute("testName",PortletSession.PORTLET_SCOPE);
            if (attr != null) {
                if (!attr.equals(TEST_NAME)) {
                    resultWriter.setStatus(ResultWriter.FAIL);
                    resultWriter.addDetail(
                        "Attribute set by the first portlet in the session " +
                        " in the first request is different when trying to " +
                        " obtain the same in the second request. " 
                        + " Expected = " + TEST_NAME 
                        + " Actual = " + attr);
                } else {
                    resultWriter.setStatus(ResultWriter.PASS);
                }
            } else {
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail("Attribute set by the first portlet "
                    + " in the session for the first request is different "
                    + " when trying to obtain the same in the second request. "
                    + " Expected = " + TEST_NAME 
                    + " Actual = " + attr);
            }
        
            out.println(resultWriter.toString());
        }
    }

    
}
