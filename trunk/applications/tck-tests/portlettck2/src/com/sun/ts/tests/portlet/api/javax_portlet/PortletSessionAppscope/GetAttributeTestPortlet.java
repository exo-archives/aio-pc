/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletSessionAppscope;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletSession;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.PrintWriter;


import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;


/**
 * 	First portlet for test for PortletSession.getAttribute() method
 *  which just sets an attribute.
 */


public class GetAttributeTestPortlet extends GenericPortlet {
    
    public static String TEST_NAME = "GetAttributeTest";

    public  static String ATTRIBUTE_NAME = "object";
    public  static String ATTRIBUTE_VALUE = "Java";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, java.io.IOException {

        
        PortletSession session = request.getPortletSession( true );
        RequestCount reqCount = new RequestCount(request, response,
                                            RequestCount.MANAGED_VIA_SESSION);

        if (reqCount.isFirstRequest()) {

            session.setAttribute( ATTRIBUTE_NAME, ATTRIBUTE_VALUE ,
                PortletSession.APPLICATION_SCOPE );
        }

    }
}
