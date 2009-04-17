/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.ts.tests.portlet.api.javax_portlet.ActionRequest;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ActionResponse;
import javax.portlet.PortletSession;
import javax.portlet.PortletException;
import javax.portlet.PortletURL;
import javax.portlet.GenericPortlet;
import java.io.IOException;
import java.io.PrintWriter;
import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;



/**
 *                 Portlet calls the
 *                 getPortletSession() with true and makes sure the session
 *                 is returned and is not null. Now we save the object id
 *                 and call the api again with(false and true). The
 *                 session object returned should be the same.
 */


public class GetPortletSessionParamTestPortlet extends GenericPortlet {

    public static String TEST_NAME="GetPortletSessionParamTest";
    
    /**
     * In first request, writes an actionURL with parameter to the output steam.
     * In second request, writes the test results
     */

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        RequestCount reqCount = new RequestCount(request,response,
										RequestCount.MANAGED_VIA_PARAM);
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
    public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {


        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        PortletSession session = null;

        session = request.getPortletSession(true);
        if (session != null) {
            String objId = session.getId();
            session.setAttribute("objId", session.getId(), 
                        PortletSession.APPLICATION_SCOPE);
            session = request.getPortletSession(true);
            if (session.getAttribute("objId", 
                        PortletSession.APPLICATION_SCOPE).equals(objId)) {
                session = request.getPortletSession(false);
                if (session.getAttribute("objId", 
                        PortletSession.APPLICATION_SCOPE).equals(objId)) {
                    resultWriter.setStatus(ResultWriter.PASS);
                } else {
                    resultWriter.setStatus(ResultWriter.FAIL);
                    resultWriter.addDetail("Different object id obtained");
                    resultWriter.addDetail("Expected = " + objId);
                    resultWriter.addDetail("Obtained = " + 
                        session.getAttribute("objId", 
                            PortletSession.APPLICATION_SCOPE));
                }
            } else {
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail("Different object id obtained");
                resultWriter.addDetail("Expected = " + objId);
                resultWriter.addDetail("Obtained = " + 
                    session.getAttribute("objId", 
                                PortletSession.APPLICATION_SCOPE));
            }
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("Session is not created when " +
                " getPortletSession is invoked with a true parameter.");
        }
        request.getPortletSession(true).setAttribute("TestResult", resultWriter.toString());
        // before leaving set up the request counter so
        // that render can recognize this
        // as the second request.
        response.setRenderParameter(
                    RequestCount.REQUEST_COUNTER,
                    request.getParameter(RequestCount.REQUEST_COUNTER));


    }

    protected String getPortletURL(RenderResponse response ) {

        PortletURL portletURL = response.createActionURL();
        portletURL.setParameter(RequestCount.REQUEST_COUNTER,
                                                Integer.toString(1));

        return portletURL.toString(); 
    }
}
