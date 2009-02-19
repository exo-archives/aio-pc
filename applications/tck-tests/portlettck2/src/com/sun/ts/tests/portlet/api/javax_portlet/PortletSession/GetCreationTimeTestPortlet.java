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
 *	A test for PortletSession.GetCreationTime()  method
 */


public class GetCreationTimeTestPortlet extends GenericPortlet {

    public static String TEST_NAME="GetCreationTimeTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, java.io.IOException {

        PortletSession session = request.getPortletSession( true );
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        

        long time = session.getCreationTime();
        long currentTime = System.currentTimeMillis(); 
        

        if(time != 0 && time <= currentTime ) {
            resultWriter.setStatus(ResultWriter.PASS);;
        } else if (time == 0) {
            resultWriter.setStatus(ResultWriter.FAIL);;
            resultWriter.addDetail(
                " PortletSession.getCreationTime() returned 0 ");
            
        }else {
            resultWriter.setStatus(ResultWriter.FAIL);;
            resultWriter.addDetail(
                "PortletSession.getCreationTime() returned a time later than");
            resultWriter.addDetail(" current time." );
        }

        out.println(resultWriter.toString());
    }
}
