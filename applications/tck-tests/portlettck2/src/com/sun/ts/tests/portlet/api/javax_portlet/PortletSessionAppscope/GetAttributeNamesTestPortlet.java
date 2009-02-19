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
import java.util.Enumeration;
import java.util.Vector;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.PrintWriter;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;



/**
 * 	A test for PortletSession.getAttributeNames() method
 * The first portlet setting the attributes in app scope.
 * Second portlet GetAttributeNamesTestPortlet would try to retreive it
 * in nextElement request.
 */


public class GetAttributeNamesTestPortlet extends GenericPortlet {

    
    public static String TEST_NAME = "GetAttributeNamesTest";

    public  static String FIRST_ATTRIBUTE = "object";
    public  static String SECOND_ATTRIBUTE = "object1";


    public void render(RenderRequest request, RenderResponse response ) throws PortletException, java.io.IOException {

        
        PortletSession session = request.getPortletSession( true );
        RequestCount reqCount = new RequestCount(request, response,
                                    RequestCount.MANAGED_VIA_SESSION);

        if (reqCount.isFirstRequest()) {


            // Binding values with the Session
            session.setAttribute( 
                    FIRST_ATTRIBUTE, "JSP", PortletSession.APPLICATION_SCOPE );
            session.setAttribute( 
                    FIRST_ATTRIBUTE, "PORTLET", PortletSession.APPLICATION_SCOPE);
            session.setAttribute( 
                    SECOND_ATTRIBUTE, "JAVA",PortletSession.APPLICATION_SCOPE );
        }

    }
}
