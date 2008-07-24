/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.WindowStateException;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;
import javax.portlet.PortletException;
import javax.portlet.GenericPortlet;

import java.io.PrintWriter;
import java.io.IOException;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 *  This class will test the constructor WindowStateException(text,cause,state).
 */

public class WindowStateExceptionCtr2TestPortlet extends GenericPortlet {

	static public String TEST_NAME = "WindowStateExceptionCtr2Test";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        WindowState windowState = new WindowState("NORMAL");
        Throwable expectedCause = new Throwable();

		WindowStateException windowStateException = 
              new WindowStateException(TEST_NAME, expectedCause, windowState);

        String text = windowStateException.getMessage();
        Throwable cause = windowStateException.getCause();
        WindowState state = windowStateException.getState();

        if ((text != null && text.equals(TEST_NAME)) &&
            (cause != null && cause.equals(expectedCause)) &&
             (state != null && state.equals(WindowState.NORMAL))) { 
				resultWriter.setStatus(ResultWriter.PASS);
        } else {
            resultWriter.addDetail("Expected : " 
                                    + TEST_NAME + " as text"
                                    + expectedCause + " as cause"
                                    + "NORMAL as window state");
            resultWriter.addDetail("Actual : "
                                   + text + " as text"
                                   + cause + " as cause"
                                   + state + " as window state");
        }
		out.println(resultWriter.toString());
    }
}
