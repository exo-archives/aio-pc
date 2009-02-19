/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.ts.tests.portlet.api.javax_portlet.PortletURL;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletSession;
import javax.portlet.PortletURL;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.PrintWriter;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;


/**
 *	This class will test for PortletURL.toString() method.
 */

public class ToStringTestPortlet extends GenericPortlet {

    public static String TEST_NAME="ToStringTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		RequestCount reqCount = new RequestCount(request, response,
										RequestCount.MANAGED_VIA_SESSION);

		if (reqCount.isFirstRequest()) {

			//write a portlet url to the outputstream
			PortletURLTag customTag = new PortletURLTag();
			customTag.setTagContent(getPortletURL(response));
			out.println(customTag.toString());
        }
        else {
            resultWriter.setStatus(ResultWriter.PASS);
			out.println(resultWriter.toString());
        }
    }

    protected String getPortletURL(RenderResponse response ) {
        PortletURL portletURL = response.createRenderURL();
        return portletURL.toString(); 
    }
}
