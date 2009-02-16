/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.ts.tests.portlet.api.javax_portlet.ActionRequest;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletMode;
import javax.portlet.PortletException;
import javax.portlet.PortletURL;
import java.io.IOException;
import java.io.PrintWriter;
import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;




public class LogicInProcessActionPortlet extends GenericPortlet {


    /**
     * In first request, writes an actionURL with parameter to the output steam.
     * In second request, writes the test results
     */

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        RequestCount reqCount = new RequestCount(request,response,
										RequestCount.MANAGED_VIA_SESSION);
        if (reqCount.isFirstRequest()) {
            //write a portlet url to the outputstream
            PortletURLTag customTag = new PortletURLTag();
            customTag.setTagContent(getPortletURL(response));        
            out.println(customTag.toString());
        }
        else {
            out.println(request.getPortletSession(true).getAttribute("TestResult"));
        }
    }

    protected String getPortletURL(RenderResponse response ) {

        PortletURL portletURL = response.createActionURL();
        return portletURL.toString(); 
    }
}
