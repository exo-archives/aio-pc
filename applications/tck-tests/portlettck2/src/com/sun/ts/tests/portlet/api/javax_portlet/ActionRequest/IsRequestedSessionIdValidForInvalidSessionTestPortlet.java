/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.ActionRequest;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletSession;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletURL;
import java.io.IOException;
import java.io.PrintWriter;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;


/**
 *	A negative test for PortletRequest.isRequestedSessionIdValid() method
 * First request issued to a portlet just writes a PortletURL 
 * to output stream. Second request will invalidate
 * the session. Then the method isRequestedSessionIdValid()
 * is invoked. Test passses if this method returns false.
 */


public class IsRequestedSessionIdValidForInvalidSessionTestPortlet extends GenericPortlet {

    public static String TEST_NAME = "IsRequestedSessionIdValidForInvalidSessionTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        RequestCount reqCount = new RequestCount(request, response,
                                        RequestCount.MANAGED_VIA_PARAM);

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        if (reqCount.isFirstRequest()) {
            PortletSession session = request.getPortletSession(true);

            //write a portlet url to the outputstream
            PortletURLTag customTag = new PortletURLTag();
            customTag.setTagContent(getPortletURL(request, response));
            out.println(customTag.toString());

        }
        else {
            out.println(request.getParameter("TestResult"));
        }

    }

    public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        request.getPortletSession().invalidate();

        // session is invalidated. The method should return false.
        if (!request.isRequestedSessionIdValid()) {
            resultWriter.setStatus(ResultWriter.PASS );
        }   
        else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail(
                "Expected value of isRequestedSessionIdValid() = false");
            resultWriter.addDetail(
                "Actual value of isRequestedSessionIdValid() = true");
        }
        response.setRenderParameter("TestResult", resultWriter.toString());

        // before leaving set up the request counter so
        // that render can recognize this
        // as the second request.
        response.setRenderParameter(
                        RequestCount.REQUEST_COUNTER,
                        request.getParameter(RequestCount.REQUEST_COUNTER));

    }


    protected String getPortletURL(RenderRequest request, RenderResponse response ) {

        PortletURL portletURL = response.createActionURL();
        portletURL.setParameter(RequestCount.REQUEST_COUNTER, 
                                    Integer.toString(1));
        return portletURL.toString();
    }
}
