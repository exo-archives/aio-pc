/**
 * Copyright 2007 IBM Corporation.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletConfig;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Locale;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

public class GetNoSupportedLocalesTestPortlet extends GenericPortlet{
	static public String TEST_NAME = "GetNoSupportedLocalesTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException{

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME); 
        PortletConfig portletConfig = getPortletConfig();

		if (portletConfig != null) {
        	//get the resource bundle
			Enumeration<Locale> locales = portletConfig.getSupportedLocales();
			if (!locales.hasMoreElements()){
				resultWriter.setStatus(ResultWriter.PASS); 
			} else{
				resultWriter.setStatus(ResultWriter.FAIL); 
				resultWriter.addDetail("getPortletConfig().getSupportedLocales() or " +
									   " returned no empty enumeration." );
			}
		} else {
            resultWriter.setStatus(ResultWriter.FAIL); 
			resultWriter.addDetail("getPortletConfig() returned null" );
		}
        out.println(resultWriter.toString());
	}

}
