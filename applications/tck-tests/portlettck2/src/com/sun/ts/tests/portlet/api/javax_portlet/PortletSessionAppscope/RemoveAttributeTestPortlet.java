/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */



package com.sun.ts.tests.portlet.api.javax_portlet.PortletSessionAppscope;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletSession;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.PrintWriter;

import com.sun.ts.tests.portlet.common.util.ResultWriter;


/**
 * 	Test for PortletSession.removeAttribute() method
 */

public class RemoveAttributeTestPortlet extends GenericPortlet {

    public static String TEST_NAME = "RemoveAttributeTest";


    public void render(RenderRequest request, RenderResponse response ) throws PortletException, java.io.IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        PortletSession session = request.getPortletSession( true );
        session.setAttribute( "object", "Java", PortletSession.APPLICATION_SCOPE );
        session.removeAttribute( "object", PortletSession.APPLICATION_SCOPE );
        Object obj = session.getAttribute( "object", PortletSession.APPLICATION_SCOPE );

        if(obj == null ) {
            resultWriter.setStatus(ResultWriter.PASS);
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail( "     PortletSession.getAttribute(object, PortletSession.APPLICATION_SCOPE) returned a non-null result" );
            resultWriter.addDetail( "     Actual result = |" +(String ) obj + "|" );
        }
        out.println(resultWriter.toString());
    }
}
