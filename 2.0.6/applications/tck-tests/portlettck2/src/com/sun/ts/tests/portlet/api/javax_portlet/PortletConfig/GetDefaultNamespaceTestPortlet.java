/**
 * Copyright 2007 IBM Corporation.
 */
package com.sun.ts.tests.portlet.api.javax_portlet.PortletConfig;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
/**
 *	This class uses getDefaultNamespace method to read the Default Event Namespace.
 */
public class GetDefaultNamespaceTestPortlet extends GenericPortlet{
	static public String TEST_NAME = "GetDefaultNamespaceTest";
	static private String DEFAULT_NAMESPACE = "http://default/namespace";
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
			if (defaultNamespace.equals(DEFAULT_NAMESPACE)){
				resultWriter.setStatus(ResultWriter.PASS); 
			} else{
				resultWriter.setStatus(ResultWriter.FAIL); 
				resultWriter.addDetail("getPortletConfig().getDefaultNamespace() returned wrong DefaultEventNamespace" );
			}
		} else {
            resultWriter.setStatus(ResultWriter.FAIL); 
			resultWriter.addDetail("getPortletConfig() returned null" );
		}
        out.println(resultWriter.toString());
	}
}
