/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.ts.tests.portlet.api.javax_portlet.RenderRequest;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletSession;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.PrintWriter;
import com.sun.ts.tests.portlet.common.util.ResultWriter;


/**
 *                Portlet will call getPortletSession() with true and 
 *                 makes sure the session
 *                 is returned and is not null. Now we save the object id
 *                 and call the api again with(false and true). The
 *                 session object returned should be the same.
 */


public class GetPortletSessionParamTestPortlet extends GenericPortlet {

    public static String TEST_NAME="GetPortletSessionParamTest";
    
    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
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
                " getPortletSession is invoked with a true parameter");
        }
        out.println(resultWriter.toString());
    }
}
