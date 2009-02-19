/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */




package com.sun.ts.tests.portlet.api.javax_portlet.PortletSession;

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
 * 	A test for PortletSession.invalidate() method
 */


public class InvalidateTestPortlet extends GenericPortlet {

    public static String TEST_NAME = "InvalidateTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, java.io.IOException{

        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        PortletSession session = request.getPortletSession( true );
        session.setAttribute("object", "JAVA");
        session.invalidate();

        try {
            Object value = session.getAttribute("object");
            resultWriter.setStatus(ResultWriter.FAIL );
            resultWriter.addDetail( "The exception IllegalStateException " +
                " should have been thrown" );
        } catch(IllegalStateException ise ) {
            resultWriter.setStatus(ResultWriter.PASS );
        }

        out.println(resultWriter.toString());
    }
}
