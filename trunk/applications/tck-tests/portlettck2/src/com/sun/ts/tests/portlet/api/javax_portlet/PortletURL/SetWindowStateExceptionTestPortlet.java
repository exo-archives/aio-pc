/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.ts.tests.portlet.api.javax_portlet.PortletURL;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletURL;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.PrintWriter;
import com.sun.ts.tests.portlet.common.util.ResultWriter;


/**
 *	This class uses PortletURL.setWindowState() to set a window state that is
 *  not supported by the portlet container.
 */

public class SetWindowStateExceptionTestPortlet extends GenericPortlet {

    public static String TEST_NAME="SetWindowStateExceptionTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        PortletURL portletURL = response.createActionURL();

        try {
		    portletURL.setWindowState(new WindowState("HALF-PAGE"));
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("Expected WindowStateException() to be "
                                + "thrown as the current portlet does not " 
                                + "support this window state"); 
        } catch (WindowStateException pme) {
            resultWriter.setStatus(ResultWriter.PASS);
        }
        out.println(resultWriter.toString());
    }
}
