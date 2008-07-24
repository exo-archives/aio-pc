/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletSessionUtil;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletSession;
import javax.portlet.PortletException;
import java.io.PrintWriter;
import java.io.IOException;
import com.sun.ts.tests.portlet.common.util.ResultWriter;


/* In the test portlet, an instance of a class
 * SampleBindingListener implementing HttpSessionBindingListener
 * interface is bound to the session with PORTLET_SCOPE.
 * In SampleBindingListener's valueBound() method,
 * if decodeAttributeName(attributeName) returns the attributeName
 * that was used to bind it to the portlet session, the test passes.
 */

public class DecodeAttributeNamePortletScopeTestPortlet extends GenericPortlet {

    public static String TEST_NAME="DecodeAttributeNamePortletScopeTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        PortletSession session = request.getPortletSession( true );

        // Create an instance of SampleBindingListener that has
        // all the information in constructor it needs
        // to evaluate and compute the test result.

        SampleBindingListener bindingListener = 
                            new SampleBindingListener(
                                SampleBindingListener.TEST_DECODE_ATTRIBUTE,
                                TEST_NAME,
                                "bindingAttribute",
                                PortletSession.PORTLET_SCOPE);

        // Set this instance as an portlet attribute.
        session.setAttribute( "bindingAttribute", 
                              bindingListener, 
                              PortletSession.PORTLET_SCOPE);

        // Get results computed by SampleBindingListener
        ResultWriter resultWriter = bindingListener.getResultWriter();
        out.println(resultWriter.toString());
    }
}
