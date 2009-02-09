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

public class GetSupportedLocalesTestPortlet extends GenericPortlet{
	static public String TEST_NAME = "GetSupportedLocalesTest";
	static private PortletConfig portletConfig;
    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException{

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME); 
        portletConfig = getPortletConfig();

		if (portletConfig != null) {
        	//get the resource bundle
			if (checkLocales()){
				resultWriter.setStatus(ResultWriter.PASS); 
			} else{
				resultWriter.setStatus(ResultWriter.FAIL); 
				resultWriter.addDetail("getPortletConfig().getSupportedLocales() or " +
									   " returned not the right enumeration." );
			}
		} else {
            resultWriter.setStatus(ResultWriter.FAIL); 
			resultWriter.addDetail("getPortletConfig() returned null" );
		}
        out.println(resultWriter.toString());
	}
    private boolean checkLocales(){
    	Enumeration<Locale> locals = portletConfig.getSupportedLocales();
    	for (int i=0;i<3;i++){
    		if (!locals.hasMoreElements())
    			return false;
    		Locale local = locals.nextElement();
    		if (!(local.equals(Locale.GERMAN) || local.equals(Locale.ENGLISH) || local.equals(Locale.FRENCH)))
    				return false;
    	}
    	return true;
    }
}
