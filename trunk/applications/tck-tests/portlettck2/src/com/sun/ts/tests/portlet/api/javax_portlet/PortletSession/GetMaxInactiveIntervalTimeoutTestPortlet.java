/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletSession;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletSession;
import javax.portlet.PortletException;
import javax.portlet.PortletURL;
import java.io.IOException;
import java.io.PrintWriter;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;


/**
 *	A test for PortletSession.GetMaxInactiveInterval() method
 *	gives the max time the session can remain inactive before the engine expires it
 */


public class GetMaxInactiveIntervalTimeoutTestPortlet extends GenericPortlet {

    public static String TEST_NAME = "GetMaxInactiveIntervalTimeoutTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        RequestCount reqCount = new RequestCount(request, response,
                                        RequestCount.MANAGED_VIA_PARAM);

        if (reqCount.isFirstRequest()) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            // first trip :: sets the timeout interval
            PortletSession session = request.getPortletSession(true);
            session.setMaxInactiveInterval(5);

            //write a portlet url to the outputstream
            PortletURLTag customTag = new PortletURLTag();
            customTag.setTagContent(getPortletURL(response));
            out.println(customTag.toString());

        }
        else {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            ResultWriter resultWriter = new ResultWriter(TEST_NAME);
            // second request is coming after the timeout so session 
            // should be null

            PortletSession session = request.getPortletSession(false);
            if (session == null) {
                resultWriter.setStatus(ResultWriter.PASS );
            }   
            else {
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail("PortletSession should have expired.");
                resultWriter.addDetail(
                    "request.getSession() is not null after being inactive for " );
                resultWriter.addDetail(
                " time greater than set in setMaxInactiveInterval.");
            }
            out.println(resultWriter.toString());
        }

    }

    /*
     * Write a URL that can be used to come back.
     * This URL also has the next request number in it.
     */

    protected String getPortletURL(RenderResponse response ) {

        PortletURL portletURL = response.createRenderURL();
        portletURL.setParameter(RequestCount.REQUEST_COUNTER, Integer.toString(1));
        return portletURL.toString();
    }

}
