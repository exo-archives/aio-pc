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
 * 	A test for PortletSession.getAttribute() method
 */


public class GetAttributeTestPortlet extends GenericPortlet {

    public static String TEST_NAME = "GetAttributeTest";


    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        PortletSession session = request.getPortletSession( true );
        session.setAttribute( "object", "Java" ,PortletSession.PORTLET_SCOPE );
        String value = (String)session.getAttribute("object");

        if(value != null  && value.equals("Java")) {
            resultWriter.setStatus(ResultWriter.PASS);
        } else if(value == null) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail( 
                "PortletSession.getAttribute(object) returned a null result" );
        }
        else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail( 
                "PortletSession.getAttribute(object) returned value:" + value
                + " expected value:" + "Java");
        }
            
        out.println(resultWriter.toString());
    }
}
