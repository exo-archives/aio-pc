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
import java.io.IOException;
import java.io.PrintWriter;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 *	A test for PortletSession.SetMaxInactiveInterval() method
 *	gives the max time the session can remain inactive before the engine expires it
 */


public class SetMaxInactiveIntervalTestPortlet extends GenericPortlet {

    public static String TEST_NAME = "SetMaxInactiveIntervalTest";


    public void render(RenderRequest request, RenderResponse response ) throws PortletException, java.io.IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        PortletSession session = request.getPortletSession( true );
        int expectedResult = 500000;
        session.setMaxInactiveInterval( expectedResult );
        int result = session.getMaxInactiveInterval();

        if(result == expectedResult ) {
            resultWriter.setStatus(ResultWriter.PASS);
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail( "    PortletSession.getMaxInactiveInterval() returned incorrect result " );
            resultWriter.addDetail( "    Expected result = " + expectedResult  );
            resultWriter.addDetail( "    Actual result = |" + result + "|" );
        }
        out.println(resultWriter.toString());
    }
}
