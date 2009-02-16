/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletSession;

import javax.portlet.GenericPortlet;
import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletSession;
import javax.portlet.PortletContext;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.PrintWriter;
import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;

/*
 * A portlet that just include a servlet in the same application, which 
 * would put an attribute in the session.
 */

public class CompareHttpAndPortletSessionTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "CompareHttpAndPortletSessionTest";

    public void render(RenderRequest request, RenderResponse response) throws PortletException, IOException {

        RequestCount reqCount = new RequestCount(request, response,
                                            RequestCount.MANAGED_VIA_SESSION);
        if (reqCount.isFirstRequest()) {
            String servletName = "CompareHttpAndPortletSessionTest_1_Servlet";
            PortletContext context = getPortletContext();
            PortletRequestDispatcher dispatcher = 
                        context.getNamedDispatcher(servletName);

            dispatcher.include(request, response);
    	}
    }
}
