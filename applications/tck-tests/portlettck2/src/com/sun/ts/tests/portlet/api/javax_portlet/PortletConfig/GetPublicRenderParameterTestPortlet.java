/**
 * Copyright 2007 IBM Corporation.
 */
package com.sun.ts.tests.portlet.api.javax_portlet.PortletConfig;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

public class GetPublicRenderParameterTestPortlet extends GenericPortlet {
	static public String TEST_NAME = "GetPublicRenderParameterNamesTest";
	

    /**
     *	Gets the Public Render Parameter Names for the portlet definition
     *  
     */
    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException{

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME); 
        PortletConfig portletConfig = getPortletConfig();
        List<String> namesForCompare = new ArrayList<String>(3);
        namesForCompare.add("PublicRender1");
        namesForCompare.add("PublicRender2");
        namesForCompare.add("PublicRender3");

		if (portletConfig != null) {
        	//get the resource bundle
			Enumeration<String> names;
			names = portletConfig.getPublicRenderParameterNames();
			if (equalsNoOrder(names, namesForCompare)){
				resultWriter.setStatus(ResultWriter.PASS);
			}else{
				resultWriter.setStatus(ResultWriter.FAIL); 
				resultWriter.addDetail("GetPublicRenderParameterNames returned wrong size for Public RenderParameter or different names" );
			}
		} else {
            resultWriter.setStatus(ResultWriter.FAIL); 
			resultWriter.addDetail("getPortletConfig() returned null" );
		}
        out.println(resultWriter.toString());
	}
    protected boolean equalsNoOrder(Enumeration<String> names1,List<String> listNames2){
    	List<String> listNames1 = new ArrayList<String>();
    	while(names1.hasMoreElements()){
    		listNames1.add(names1.nextElement());
    	}
    	for (String string : listNames2) {
			if (!listNames1.remove(string)){
				return false;
			}
		}
    	if (listNames1.size() != 0){
    		return false;
    	}
    	return true;
    }
}
