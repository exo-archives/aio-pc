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
import java.util.Date;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.PrintWriter;

import com.sun.ts.tests.portlet.common.util.ResultWriter;


/**
 *	A test for PortletSession.getLastAccessesTime() method
 */


public class GetLastAccessedTimeTestPortlet extends GenericPortlet {

    static public String TEST_NAME = "GetLastAccessedTimeTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, java.io.IOException{


        PortletSession session = request.getPortletSession( true );
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        
        long time = session.getLastAccessedTime();
        long currentTime = System.currentTimeMillis(); 
        

        if(time != 0 && time <= currentTime ) {
            resultWriter.setStatus(ResultWriter.PASS);
        } else if (time == 0) {
            resultWriter.setStatus(ResultWriter.FAIL);;
            resultWriter.addDetail(
                " PortletSession.getLastAccessedTime() returned 0 ");
            
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("PortletSession.getLastAccessedTime()" +
                " returned time later than current." );

        }

        out.println(resultWriter.toString());
    }

}
