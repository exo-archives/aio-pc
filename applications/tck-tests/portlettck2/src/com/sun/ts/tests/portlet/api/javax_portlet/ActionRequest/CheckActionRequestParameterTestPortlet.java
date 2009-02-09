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
import javax.portlet.PortletURL;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import java.io.IOException;
import java.io.PrintWriter;
import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;


/**
 * This test portlet will verify that the parameters set in the action request
 * are not visible in the render() method.
 */


public class CheckActionRequestParameterTestPortlet extends GenericPortlet {

    public static String TEST_NAME="CheckActionRequestParameterTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        PortletSession session = request.getPortletSession();
        RequestCount reqCount = new RequestCount(request, response,
                                        RequestCount.MANAGED_VIA_SESSION);

		String param = "BestLanguage";
		String expectedValue = "Java";

        if (reqCount.isFirstRequest()) {
            PortletURLTag customTag = new PortletURLTag();
            customTag.setTagContent(getPortletURL(response));
            out.println(customTag.toString());
        }
        else {
			String actualValue = request.getParameter(param);
			if (actualValue == null) {
				resultWriter.setStatus(ResultWriter.PASS);
			} else {
				resultWriter.setStatus(ResultWriter.FAIL);
				resultWriter.addDetail("RenderRequest.getParameter("
							+ param + ") returned an incorrect result" );
				resultWriter.addDetail("Expected result = null");
				resultWriter.addDetail("Actual result = " + actualValue);

			}
            out.println(resultWriter.toString());
        }
    }

	protected String getPortletURL(RenderResponse response ) {
		PortletURL portletURL = response.createActionURL();
		portletURL.setParameter("BestLanguage", "Java");
		return portletURL.toString();
	}

    public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {
		// Does nothing
    }
}
