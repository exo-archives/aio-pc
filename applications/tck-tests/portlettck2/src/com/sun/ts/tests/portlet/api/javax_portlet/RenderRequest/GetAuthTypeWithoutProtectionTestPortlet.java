/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.ts.tests.portlet.api.javax_portlet.RenderRequest;


import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.GenericPortlet;

import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.PrintWriter;
import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 *	A Negative Test for getAuthType method
 */


public class GetAuthTypeWithoutProtectionTestPortlet extends GenericPortlet {

	public static String TEST_NAME="GetAuthTypeWithoutProtectionTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        String result = request.getAuthType();

        if(result == null ) {

            resultWriter.setStatus(ResultWriter.PASS);
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail( 
                "PortletRequest.getAuthType() returned a non-null result, "
                + " even though Portlet is not protected" );
            resultWriter.addDetail( "     Actual result = |" + result + "|" );
        }

        out.println(resultWriter.toString());
    }
}
