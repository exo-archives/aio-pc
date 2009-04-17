/*
 * Copyright 2007 IBM Corporation
 */
package com.sun.ts.tests.portlet.api.javax_portlet.Portlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.PortletURL;
import javax.portlet.ProcessAction;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

public class AnnotatedProcessActionTestPortlet extends GenericPortlet {
	public static final String TEST_NAME = "AnnotatedProcessActionTest";

	@ProcessAction(name=TEST_NAME)
    public void testAction(ActionRequest request, ActionResponse response)
        throws PortletException, IOException {

        request.getPortletSession().setAttribute(TEST_NAME, TEST_NAME);
    }

    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        RequestCount requestCount
            = new RequestCount(request, response,
                               RequestCount.MANAGED_VIA_SESSION);

        if (requestCount.isFirstRequest()) {
            PortletURL url = response.createActionURL();
            url.setParameter(ActionRequest.ACTION_NAME, TEST_NAME);
            PortletURLTag urlTag = new PortletURLTag();
            urlTag.setTagContent(url.toString());        
            out.println(urlTag.toString());
        } else {
            ResultWriter resultWriter = new ResultWriter(TEST_NAME);
            PortletSession session = request.getPortletSession();
            String expectedResult = TEST_NAME;
            String result = (String)session.getAttribute(TEST_NAME);

            if (result != null) {
                session.removeAttribute(TEST_NAME); // cleanup
            }

            if ((result != null) && result.equals(expectedResult)) {
                resultWriter.setStatus(ResultWriter.PASS);
            } else {
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail("Expected result = " + expectedResult);
                resultWriter.addDetail("Actual result = " + result);
            }

            out.println(resultWriter.toString());            
        }
    }
}
