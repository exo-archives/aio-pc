/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.EventResponse;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Event;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletException;
import javax.xml.namespace.QName;

import com.sun.ts.tests.portlet.common.util.ListCompare;
import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class calls multipliy times the setEvent method in the
 * processAction method. Then is checked that the events are received.
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 *
 */

public class SetMultipleEventsProcessActionTestPortlet extends GeneralEventing {
	
	public static final String TEST_NAME =
		"SetMultipleEventsProcessActionTest";
	
	private static final String LIST_NAME = "list";
	
	private Map<QName, Serializable> getMap() {
		
		Map<QName, Serializable> map = new HashMap<QName, Serializable>(2);
		map.put(qname, TEST_NAME);
		map.put(qnameAddress, getAddress());
		
		return map;
	}

	@Override
	protected String getTestName() {
		return TEST_NAME;
	}

	@Override
	public void processAction(ActionRequest request, ActionResponse response)
			throws PortletException, IOException {
		
		Map<QName, Serializable> map = getMap();
		
		for (QName key : map.keySet())
			response.setEvent(key, map.get(key));
	}

	@Override
	public void processEvent(EventRequest request, EventResponse response)
			throws PortletException, IOException {
		
		ResultWriter resultWriter = checkEvents(request);	
		
		if (resultWriter != null)
			request.getPortletSession(true).setAttribute(RESULT,
					resultWriter.toString());

	}
	
	private ResultWriter checkEvents(EventRequest request) {
		
		ArrayList<String> receivedEvents = (ArrayList<String>)
									request.getPortletSession(true)
									.getAttribute(LIST_NAME);

		if (receivedEvents == null)
			receivedEvents = new ArrayList<String>();
		
		Event evt = request.getEvent();
		
		receivedEvents.add(evt.getQName().toString());
		request.getPortletSession(true).setAttribute(LIST_NAME, receivedEvents);
		
		if (receivedEvents.size() == getMap().size()) {
		
			ListCompare listCompare = new ListCompare(map2array(getMap()),
							receivedEvents.toArray(new String[] {}),
							null,
							ListCompare.ALL_ELEMENTS_MATCH);				
			
			ResultWriter resultWriter = new ResultWriter(TEST_NAME);
			if (listCompare.misMatch()) {
				
				resultWriter.setStatus(ResultWriter.FAIL);
				resultWriter.addDetail(listCompare.getMisMatchReason());
			
			} else
				resultWriter.setStatus(ResultWriter.PASS);
			
			return resultWriter;
			
		} else
			return null;
	}
	
	private String[] map2array(Map<QName, Serializable> map) {
		
		ArrayList<String> list = new ArrayList<String>(map.size());
		
		for (QName key : map.keySet())
			list.add(key.toString());
		
		return list.toArray(new String[]{});		
	}
}
