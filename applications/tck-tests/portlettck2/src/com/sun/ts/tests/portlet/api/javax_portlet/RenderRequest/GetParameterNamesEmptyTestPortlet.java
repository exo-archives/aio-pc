/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.RenderRequest;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletURL;
import java.util.Iterator;
import java.util.Vector;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

import com.sun.ts.tests.portlet.common.util.MapCompare;

/**
 * Test for PortletRequest.getParameterNames() method
 * First request to the portlet writes a
 * action portletURL with no parameter in it, to the output stream.
 * The portlet URL string is extracted and used for second
 * request. In the second request, 
 * portlet uses PortletRequest.getParameterNames()
 * to see if the empty enumeration is returned.
 */


import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;

public class GetParameterNamesEmptyTestPortlet extends GenericPortlet {

    static public String TEST_NAME = "GetParameterNamesEmptyTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, java.io.IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();


        RequestCount reqCount = new RequestCount(request, response,
                                        RequestCount.MANAGED_VIA_SESSION);

        if (reqCount.isFirstRequest()) {

            //write a portlet url to the outputstream
            PortletURLTag customTag = new PortletURLTag();
            customTag.setTagContent(getPortletURL(response));
            out.println(customTag.toString());
        } else {
            computeResults(request, response);
            out.println( request.getPortletSession(true).getAttribute(
                            "resultGetParameterNamesEmpty"));
        }
    }

    protected String getPortletURL(RenderResponse response ) {

        PortletURL portletURL = response.createRenderURL();
        return portletURL.toString(); 
    }

    public void computeResults(RenderRequest request, RenderResponse response )
                    throws PortletException, java.io.IOException {

        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        Enumeration names = request.getParameterNames();
        if (names != null && !names.hasMoreElements()) {
            resultWriter.setStatus(ResultWriter.PASS);
        }
        else {
            resultWriter.setStatus(ResultWriter.FAIL);
            if ( names == null) {
                resultWriter.addDetail("Expected an empty enumeration.");
                resultWriter.addDetail("Found null returned");
            }
            else {
                resultWriter.addDetail("Expected an empty enumberation.");
                resultWriter.addDetail(
                    "Found a enumeration with size greater than 0");
            }
        }
        
        request.getPortletSession(true).setAttribute(
                                "resultGetParameterNamesEmpty",
                                resultWriter.toString());
   }

}
