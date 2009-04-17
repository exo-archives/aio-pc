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
import com.sun.ts.tests.portlet.common.util.tags.PortletTCKCustomTag;

/**
 * This class participates in testing that if the client request is
 * triggered by a render URL, the portal/portlet-container invokes the
 * render method for all the portlets in the portal page with the
 * possible exception of portlets for which their content is being
 * cached.  In the second request, it writes out a string that is
 * expected by the client.
 */
public class RenderInvokedForAllPortletsTest_1_Portlet extends GenericPortlet {
    public static final String TEST_NAME = "RenderInvokedForAllPortletsTest";

    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        RequestCount requestCount
            = new RequestCount(request, response,
                               RequestCount.MANAGED_VIA_SESSION);

        if (!requestCount.isFirstRequest()) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            PortletTCKCustomTag customTag = new PortletTCKCustomTag();
            customTag.setTagContent(CommonConstants.TEST_RESULT);
            out.println(customTag.toString());
        }
    }
}
