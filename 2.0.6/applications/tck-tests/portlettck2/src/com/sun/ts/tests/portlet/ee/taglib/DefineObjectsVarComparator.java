/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.ee.taglib;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.http.HttpServletRequest;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * Helper class that checks variables for their expected values.
 * Uses a ResultWriter for result output.
 * 
 * @author Oliver Spindler <olisp_suhl@yahoo.com>
 *
 */
public class DefineObjectsVarComparator {

	private ResultWriter resultWriter = null;
	
	
	public DefineObjectsVarComparator(String testName) {
		
		super();
		
		resultWriter = new ResultWriter(testName);
		
		resultWriter.setStatus(ResultWriter.PASS);
	}
	
	
	public String getResult(){
		return resultWriter.toString();
	}
	
	
	public void checkRenderRequest(HttpServletRequest servletRequest, 
			Object result){

		Object expectedResult = 
			servletRequest.getAttribute("javax.portlet.request");

		if ((result == null) || !(result instanceof RenderRequest)
				|| !result.equals(expectedResult)) {

			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("renderRequest:");
			resultWriter.addDetail("Expected result = " + expectedResult);
			resultWriter.addDetail("Actual result = " + result);
		}
	}

	
	public void checkRenderResponse(HttpServletRequest servletRequest, 
			Object result){

		Object expectedResult = 
			servletRequest.getAttribute("javax.portlet.response");

		if ((result == null) || !(result instanceof RenderResponse)
				|| !result.equals(expectedResult)) {

			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("renderResponse:");
			resultWriter.addDetail("Expected result = " + expectedResult);
			resultWriter.addDetail("Actual result = " + result);
		}
	}

	
	public void checkActionRequest(HttpServletRequest servletRequest, 
			Object result){

		Object expectedResult = 
			servletRequest.getAttribute("javax.portlet.request");

		if ((result == null) || !(result instanceof ActionRequest)
				|| !result.equals(expectedResult)) {

			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("actionRequest:");
			resultWriter.addDetail("Expected result = " + expectedResult);
			resultWriter.addDetail("Actual result = " + result);
		}
	}

	
	public void checkActionResponse(HttpServletRequest servletRequest, 
			Object result){

		Object expectedResult = 
			servletRequest.getAttribute("javax.portlet.response");

		if ((result == null) || !(result instanceof ActionResponse)
				|| !result.equals(expectedResult)) {

			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("actionResponse:");
			resultWriter.addDetail("Expected result = " + expectedResult);
			resultWriter.addDetail("Actual result = " + result);
		}
	}

	
	public void checkResourceRequest(HttpServletRequest servletRequest, 
			Object result){

		Object expectedResult = 
			servletRequest.getAttribute("javax.portlet.request");

		if ((result == null) || !(result instanceof ResourceRequest)
				|| !result.equals(expectedResult)) {

			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("resourceRequest:");
			resultWriter.addDetail("Expected result = " + expectedResult);
			resultWriter.addDetail("Actual result = " + result);
		}
	}

	
	public void checkResourceResponse(HttpServletRequest servletRequest, 
			Object result){

		Object expectedResult = 
			servletRequest.getAttribute("javax.portlet.response");

		if ((result == null) || !(result instanceof ResourceResponse)
				|| !result.equals(expectedResult)) {

			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("resourceResponse:");
			resultWriter.addDetail("Expected result = " + expectedResult);
			resultWriter.addDetail("Actual result = " + result);
		}
	}

	
	public void checkEventRequest(HttpServletRequest servletRequest, 
			Object result){

		Object expectedResult = 
			servletRequest.getAttribute("javax.portlet.request");

		if ((result == null) || !(result instanceof EventRequest)
				|| !result.equals(expectedResult)) {

			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("eventRequest:");
			resultWriter.addDetail("Expected result = " + expectedResult);
			resultWriter.addDetail("Actual result = " + result);
		}
	}

	
	public void checkEventResponse(HttpServletRequest servletRequest, 
			Object result){

		Object expectedResult = 
			servletRequest.getAttribute("javax.portlet.response");

		if ((result == null) || !(result instanceof EventResponse)
				|| !result.equals(expectedResult)) {

			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("eventResponse:");
			resultWriter.addDetail("Expected result = " + expectedResult);
			resultWriter.addDetail("Actual result = " + result);
		}
	}

	
	public void checkPortletSession(HttpServletRequest servletRequest, 
			Object result){

		PortletRequest portletRequest = 
			(PortletRequest)servletRequest.getAttribute("javax.portlet.request");
		
		Object expectedResult = portletRequest.getPortletSession(false);
		
		if((expectedResult == null) && (result != null)){
			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("portletSession:");
			resultWriter.addDetail("Expected result = null");
			resultWriter.addDetail("Actual result = " + result);
		}
		else if((expectedResult != null) && 
				((result == null) || 
						!(result instanceof PortletSession)|| 
						!result.equals(expectedResult))) {

			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("portletSession:");
			resultWriter.addDetail("Expected result = " + expectedResult);
			resultWriter.addDetail("Actual result = " + result);
		}
	}

	
	public void checkPortletSessionScope(HttpServletRequest servletRequest, 
			Object result){

		PortletRequest portletRequest = 
			(PortletRequest)servletRequest.getAttribute("javax.portlet.request");
		
		PortletSession portletSession = 
			portletRequest.getPortletSession(false);
		
		Object expectedResult = null;
		
		if(portletSession != null){
			expectedResult = portletSession.getAttributeMap();
		}
		else{
			expectedResult = new HashMap<String, Object>();
		}
		
		if((expectedResult == null) && (result != null)){
			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("portletSessionScope:");
			resultWriter.addDetail("Expected result = null");
			resultWriter.addDetail("Actual result = " + result);
		}
		else if((expectedResult != null) && 
				((result == null) || 
						!(result instanceof Map) || 
						!result.equals(expectedResult))) {

			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("portletSessionScope:");
			resultWriter.addDetail("Expected result = " + expectedResult);
			resultWriter.addDetail("Actual result = " + result);
		}
	}
	
	
	public void checkPortletPreferences(HttpServletRequest servletRequest, 
			Object result){

		PortletRequest portletRequest = 
			(PortletRequest)servletRequest.getAttribute("javax.portlet.request");
		
		Object expectedResult = portletRequest.getPreferences();
		
		if((expectedResult == null) && (result != null)){
			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("portletSessionScope:");
			resultWriter.addDetail("Expected result = null");
			resultWriter.addDetail("Actual result = " + result);
		}
		else if((expectedResult != null) && 
				((result == null) || 
						!(result instanceof PortletPreferences)|| 
						!result.equals(expectedResult))) {

			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("portletPreferences:");
			resultWriter.addDetail("Expected result = " + expectedResult);
			resultWriter.addDetail("Actual result = " + result);
		}
	}
	
	
	public void checkPortletPreferencesValues(HttpServletRequest servletRequest, 
			Object result){

		PortletRequest portletRequest = 
			(PortletRequest)servletRequest.getAttribute("javax.portlet.request");
		
		PortletPreferences portletPreferences = 
			portletRequest.getPreferences();
		
		Object expectedResult = null;
		
		if(portletPreferences != null){
			expectedResult = portletPreferences.getMap();
		}
		else{
			expectedResult = new HashMap<String, String[]>();
		}
		
		if((expectedResult == null) && (result != null)){
			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("portletPreferences:");
			resultWriter.addDetail("Expected result = null");
			resultWriter.addDetail("Actual result = " + result);
		}
		else if((expectedResult != null) && 
				((result == null) || 
						!(result instanceof Map)|| 
						!result.equals(expectedResult))) {

			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("portletPreferences:");
			resultWriter.addDetail("Expected result = " + expectedResult);
			resultWriter.addDetail("Actual result = " + result);
		}
	}
	
	
	public void checkPortletConfig(HttpServletRequest servletRequest, 
			Object result){

		Object expectedResult = 
			servletRequest.getAttribute("javax.portlet.config");
				
		if((expectedResult == null) && (result != null)){
			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("portletConfig:");
			resultWriter.addDetail("Expected result = null");
			resultWriter.addDetail("Actual result = " + result);
		}
		else if((expectedResult != null) && 
				((result == null) || 
						!(result instanceof PortletConfig)|| 
						!result.equals(expectedResult))) {

			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("portletConfig:");
			resultWriter.addDetail("Expected result = " + expectedResult);
			resultWriter.addDetail("Actual result = " + result);
		}
	}

}
