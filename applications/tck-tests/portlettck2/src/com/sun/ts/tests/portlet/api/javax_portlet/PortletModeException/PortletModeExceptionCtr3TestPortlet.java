/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletModeException;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.PortletException;
import javax.portlet.GenericPortlet;
import java.io.PrintWriter;
import java.io.IOException;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 *  This class will test the constructor PortletModeException(cause, mode).
 */

public class PortletModeExceptionCtr3TestPortlet extends GenericPortlet {

	static public String TEST_NAME = "PortletModeExceptionCtr3Test";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        PortletMode portletMode = new PortletMode("VIEW");
        Throwable expectedCause = new Throwable();

		PortletModeException portletModeException = 
                    new PortletModeException(expectedCause, portletMode);

         Throwable cause = portletModeException.getCause();
         PortletMode mode = portletModeException.getMode();

         if ((cause != null && cause.equals(expectedCause)) && 
             (mode != null && mode.equals(PortletMode.VIEW))) { 
				resultWriter.setStatus(ResultWriter.PASS);
         } else {
             resultWriter.addDetail("Expected : " 
                                    + expectedCause + " as cause"
                                    + "VIEW as portlet mode");
             resultWriter.addDetail("Actual : "
                                    + cause + " as cause"
                                    + mode + " as portlet mode");
         }
		out.println(resultWriter.toString());
    }
}
