/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortalContext;

import java.util.StringTokenizer;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortalContext;
import javax.portlet.WindowState;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.ArrayList;


import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.ListCompare;

public class GetSupportedWindowStatesTestPortlet extends GenericPortlet {

	static public String TEST_NAME = "GetSupportedWindowStatesTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		ResultWriter resultWriter = new ResultWriter(TEST_NAME); 
        PortalContext context = request.getPortalContext();

        //expected
        ArrayList expectedWindowStates = new ArrayList();
        expectedWindowStates.add(WindowState.NORMAL.toString());
        expectedWindowStates.add(WindowState.MINIMIZED.toString());
        expectedWindowStates.add(WindowState.MAXIMIZED.toString());

        if (context != null) {
            Enumeration windowStates = context.getSupportedWindowStates();
            if ( windowStates != null ) {
                ArrayList actualWindowStates = new ArrayList();
                while(windowStates.hasMoreElements()) {
                   actualWindowStates.add(windowStates.nextElement().toString());
                }

                ListCompare compare = new ListCompare(
                            expectedWindowStates.iterator(),
                            actualWindowStates.iterator(),
                            null,
                            ListCompare.SUBSET_MATCH);
                if (compare.misMatch()) {
                    resultWriter.setStatus(ResultWriter.FAIL);
                    resultWriter.addDetail("PortalContext.getSupportedWindowStates()" +
                                            " returned unexpected results.");
                    resultWriter.addDetail(compare.getMisMatchReason());
                }
                else {
                    resultWriter.setStatus(ResultWriter.PASS);
                }
            } 
            else {
			   resultWriter.setStatus(ResultWriter.FAIL); 
               resultWriter.addDetail("Obtained a null window states enumeration"); 
            }
        } 
        else {
			resultWriter.setStatus(ResultWriter.FAIL); 
            resultWriter.addDetail( "Request.getPortalContext() returned null");
        }
		out.println(resultWriter.toString()); 
    }
}
