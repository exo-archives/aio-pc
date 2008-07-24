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
 *  This class will test the constructor PortletModeException(text, mode).
 */

public class PortletModeExceptionCtr1TestPortlet extends GenericPortlet {

	static public String TEST_NAME = "PortletModeExceptionCtr1Test";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        PortletMode portletMode = new PortletMode("VIEW");

		PortletModeException portletModeException = 
                            new PortletModeException(TEST_NAME, portletMode);

        String text = portletModeException.getMessage();
        PortletMode mode = portletModeException.getMode();

        if ((text != null && text.equals(TEST_NAME)) &&
             (mode != null && mode.equals(PortletMode.VIEW))) { 
				resultWriter.setStatus(ResultWriter.PASS);
        } else {
            resultWriter.addDetail("Expected : " 
                                   + "PortletModeExceptionCtr1Test as text"
                                   + "VIEW as portlet mode");
            resultWriter.addDetail("Actual : "
                                   + text + " as text"
                                   + mode + " as portlet mode");
        }
		out.println(resultWriter.toString());
    }
}
