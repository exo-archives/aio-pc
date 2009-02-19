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
 * 	Negative Test for getAttributeNames method
 */

public class GetAttributeNamesIllegalStateTestPortlet extends GenericPortlet {

    public static String TEST_NAME = "GetAttributeNamesIllegalStateTest";


    public void render(RenderRequest request, RenderResponse response ) throws PortletException, java.io.IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        PortletSession session = request.getPortletSession( true );
        session.invalidate();

        try {
            session.getAttributeNames(PortletSession.APPLICATION_SCOPE);
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail( 
                "The expection IllegalStateException should have been thrown<BR>" );
        } catch(IllegalStateException ise ) {
            resultWriter.setStatus(ResultWriter.PASS);
        }
        out.println(resultWriter.toString());
    }
}
