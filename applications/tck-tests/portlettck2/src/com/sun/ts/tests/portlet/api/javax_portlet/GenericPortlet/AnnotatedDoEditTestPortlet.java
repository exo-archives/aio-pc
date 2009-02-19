/**
 * Copyright 2007 IBM Corporation.
 */
package com.sun.ts.tests.portlet.api.javax_portlet.GenericPortlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.PortletSession;
import javax.portlet.PortletURL;
import javax.portlet.RenderMode;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

public class AnnotatedDoEditTestPortlet extends GenericPortlet {
	public static final String TEST_NAME = "AnnotatedDoEditTest";

	@RenderMode(name="view")
    public void testView(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        PortletSession session = request.getPortletSession();

        RequestCount requestCount
            = new RequestCount(request, response,
                               RequestCount.MANAGED_VIA_SESSION);

        if (requestCount.isFirstRequest()) {
            PortletURL url = response.createRenderURL();

            if (request.isPortletModeAllowed(PortletMode.EDIT)) {
                try {
                    url.setPortletMode(PortletMode.EDIT);
                    session.setAttribute("portletModeSet", Boolean.TRUE);
                } catch (PortletModeException e) {
                    resultWriter.setStatus(ResultWriter.FAIL);

                    resultWriter.addDetail("PortletModeException was "
                                           + "thrown when setting "
                                           + "portlet mode to EDIT.");

                    out.println(resultWriter.toString());
                }
            }

            PortletURLTag urlTag = new PortletURLTag();
            urlTag.setTagContent(url.toString());        
            out.println(urlTag.toString());
        } else {
            if (session.getAttribute("portletModeSet") == null) {
                resultWriter.setStatus(ResultWriter.PASS);
            } else {
                session.removeAttribute("portletModeSet"); // clean up
                resultWriter.setStatus(ResultWriter.FAIL);

                resultWriter.addDetail("doView() is called when the "
                                       + "portlet mode is EDIT.");
            }

            out.println(resultWriter.toString());
        }
    }
	
	@RenderMode(name="edit")
    public void testEdit(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        try {
            super.doEdit(request, response);
            resultWriter.setStatus(ResultWriter.FAIL);

            resultWriter.addDetail("The default implementation of "
                                   + "GenericPortlet.doEdit() doesn't "
                                   + "throw an exception.");
        } catch (Exception e) {
            resultWriter.setStatus(ResultWriter.PASS);
        }

        out.println(resultWriter.toString());
    }
}
