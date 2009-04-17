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

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;

/**
 * 	 A test for PortletSession.SetAttributeTest(String,Object, int) method
 */

public class SetAttributeTestPortlet extends GenericPortlet {

    
    public  static String ATTRIBUTE_NAME = "object";
    public  static String ATTRIBUTE_VALUE = "Portlet";


    public void render(RenderRequest request, RenderResponse response ) throws PortletException, java.io.IOException {
		RequestCount reqCount = new RequestCount(request, response,
										RequestCount.MANAGED_VIA_SESSION);
		if (reqCount.isFirstRequest()) {
        	PortletSession session = request.getPortletSession( true );
        	String param = ATTRIBUTE_NAME;
        	String expectedResult = ATTRIBUTE_VALUE;
        
        	session.setAttribute( param, expectedResult , PortletSession.APPLICATION_SCOPE);
		}
    }
}
