/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletMode;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletMode;
import javax.portlet.PortletException;
import javax.portlet.GenericPortlet;
import java.io.PrintWriter;
import java.io.IOException;
import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 *	This class will test PortletMode.hashCode() method.
 */

public class HashCodeTestPortlet extends GenericPortlet {

	static public String TEST_NAME = "HashCodeTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        PortletMode portletMode = new PortletMode("help");

        if (portletMode != null) {
            if (portletMode.hashCode() == PortletMode.HELP.hashCode()) {
                resultWriter.setStatus(ResultWriter.PASS);
            } else {
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail("Expected PortletMode.hashCode() -> " 
                                                    + PortletMode.HELP.hashCode());
                resultWriter.addDetail("PortletMode.hashCode() returned " 
                                                    + portletMode.hashCode() );
             }
         } else {
             resultWriter.setStatus(ResultWriter.FAIL);
             resultWriter.addDetail("portletMode object is null");
         }
         out.println(resultWriter.toString());
    }
}
