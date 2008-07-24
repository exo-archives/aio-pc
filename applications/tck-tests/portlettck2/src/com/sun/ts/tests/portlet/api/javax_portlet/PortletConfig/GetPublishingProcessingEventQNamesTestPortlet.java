/**
 * Copyright 2007 IBM Corporation.
 */
package com.sun.ts.tests.portlet.api.javax_portlet.PortletConfig;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.xml.namespace.QName;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

public class GetPublishingProcessingEventQNamesTestPortlet extends GenericPortlet {
	static public String TEST_NAME = "GetPublishingProcessingEventQNamesTest";
	
	static private String[] PUBLISHING_QNAMES = {"{http://www.event.de}GetPublishingEventQNamesFirst",
												 "{http://www.event.de}GetPublishingEventQNamesSecond",
												 "{http://www.event.de}GetPublishingEventQNamesThird"};
	
	static private String[] PROCESSING_QNAMES = {"{http://www.event.de}GetProcessingEventQNamesFirst",
											     "{http://www.event.de}GetProcessingEventQNamesSecond",
											     "{http://www.event.de}GetProcessingEventQNamesThird"};
	
    private PortletConfig portletConfig;
    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException{

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        portletConfig = getPortletConfig();

		if (portletConfig != null) {
        	//get the resource bundle
			
			if (checkPublishingEvents() && checkProcessingEvents()){
				resultWriter.setStatus(ResultWriter.PASS); 
			} else{
				resultWriter.setStatus(ResultWriter.FAIL); 
				resultWriter.addDetail("getPortletConfig().getPublishingEventQNames() or " +
									   "getPortletConfig().getProcessingEventQNames()" +
									   " returned" );
			}
		} else {
            resultWriter.setStatus(ResultWriter.FAIL); 
			resultWriter.addDetail("getPortletConfig() returned null" );
		}
        out.println(resultWriter.toString());
	}
    public boolean checkPublishingEvents(){
    	Enumeration<QName> publishingEvents = portletConfig.getPublishingEventQNames();
    	for (int i = 0; i < 3;i++){
    		if (publishingEvents.hasMoreElements()){
    			QName qname = publishingEvents.nextElement();
    			if (!(qname.toString().equals(PUBLISHING_QNAMES[0]) || qname.toString().equals(PUBLISHING_QNAMES[1]) || qname.toString().equals(PUBLISHING_QNAMES[2])))
    				return false;
    		}
    		else return false;
    	}
    	return true;
    }
    
    public boolean checkProcessingEvents(){
    	Enumeration<QName> processingEvents = portletConfig.getProcessingEventQNames();
    	for (int i = 0; i < 3;i++){
    		if (processingEvents.hasMoreElements()){
    			QName qname = processingEvents.nextElement();
    			if (!(qname.toString().equals(PROCESSING_QNAMES[0]) || qname.toString().equals(PROCESSING_QNAMES[1]) || qname.toString().equals(PROCESSING_QNAMES[2])))
    				return false;
    		}
    		else return false;
    	}
    	return true;
    }
}
