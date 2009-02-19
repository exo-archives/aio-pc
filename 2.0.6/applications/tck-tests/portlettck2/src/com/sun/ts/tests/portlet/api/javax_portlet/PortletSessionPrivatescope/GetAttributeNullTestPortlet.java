/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletSessionPrivatescope;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletSession;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.PrintWriter;

import com.sun.ts.tests.portlet.common.util.ResultWriter;


/**
 * 	A negative test for PortletSession.getAttribute(String, int) method
 */


public class GetAttributeNullTestPortlet extends GenericPortlet {

    public static String TEST_NAME = "GetAttributeNullTest";


    public void render(RenderRequest request, RenderResponse response ) throws PortletException, java.io.IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        PortletSession session = request.getPortletSession( true );
        
        Object value = session.getAttribute( "import javax.portlet.RenderRequest;", PortletSession.PORTLET_SCOPE );

        if(value != null) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail( "Null Test: PortletSession.getAttribute(object) should have returned null." );
        }
        else {
            resultWriter.setStatus(ResultWriter.PASS);
        }
        out.println(resultWriter.toString());
    }
}
