/*
 * Copyright 2007 IBM Corporation
 */
package com.sun.ts.tests.portlet.api.javax_portlet.PortletConfigSecond;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.xml.XMLConstants;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
/**
 *	This class uses getDefaultEventNamespace method to read the Default Event Namespace.
 */
public class GetDefaultXMLNamespaceTestPortlet extends GenericPortlet{
	static public String TEST_NAME = "GetDefaultXMLNamespaceTest";
    /**
     *	Gets the Default Event Namespace from the descriptor file.
     *  
     */
    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException{

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME); 
        PortletConfig portletConfig = getPortletConfig();

		if (portletConfig != null) {
        	//get the resource bundle
			String defaultNamespace = portletConfig.getDefaultNamespace();
			if (defaultNamespace.equals(XMLConstants.NULL_NS_URI)){
				resultWriter.setStatus(ResultWriter.PASS); 
			} else{
				resultWriter.setStatus(ResultWriter.FAIL); 
				resultWriter.addDetail("getPortletConfig() returned wrong DefaultNamespace" );
			}
		} else {
            resultWriter.setStatus(ResultWriter.FAIL); 
			resultWriter.addDetail("getPortletConfig() returned null" );
		}
        out.println(resultWriter.toString());
	}
}
