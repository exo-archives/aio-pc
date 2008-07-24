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
import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.PrintWriter;
import com.sun.ts.tests.portlet.common.util.ResultWriter;


/**
 *	This class uses  PortletURL.setPortletMode() to set a portlet mode that is
 *  not supported by this portlet so that PortletModeException() is thrown.
 */

public class SetPortletModeExceptionTestPortlet extends GenericPortlet {

    public static String TEST_NAME="SetPortletModeExceptionTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        PortletURL portletURL = response.createRenderURL();

        try {
		    portletURL.setPortletMode(PortletMode.EDIT);
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("Expected PortletModeException() to be " 
                                 + " thrown as the current portlet does not "
                                 + " support this portlet mode"); 
        } catch (PortletModeException pme) {
            resultWriter.setStatus(ResultWriter.PASS);
        }
        out.println(resultWriter.toString());
    }
}
