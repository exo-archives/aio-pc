/*
 * Copyright 2007 IBM Corporation
 */

/**
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 */

package com.sun.ts.tests.portlet.api.javax_portlet.EventResponse;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.common.webclient.http.HttpResponse;
import com.sun.ts.tests.portlet.common.client.BasePortletUrlClient;
import com.sun.ts.tests.portlet.common.client.TSPortletInfo;
import com.sun.ts.tests.portlet.common.client.tags.PortletURLClientTag;
import com.sun.ts.tests.portlet.common.util.ResultWriter;

public class SpecURLClient extends BasePortletUrlClient {
	public static void main(String[] args) {
		SpecURLClient theTests = new SpecURLClient();
		Status status = theTests.run(args, System.out, System.err);
		status.exit();
	}

	/**
	 * Returns the name of the default portlet app.
	 */
	public String getDefaultPortletApp() {
		return "portlet_jp_EventResponse_web";
	}

	/*
	 * @class.setup_props: ts_home;
	 */

	/*
	 * @testName: SetRenderParametersTest
	 * @assertion_ids: PORTLET:SPEC:77;
	 * @test_Strategy: First request to the portlet writes a actionURL to the
	 *                 output stream. The portlet URL string is extracted and used for second
	 *                 request. In the second request, processAction() will trigger an event. In
	 *                 processEvent() few render parameters set on EventResponse. The portlet
	 *                 will check in render() if the parameters available.
	 * @assertion: If a portlet receives a render request following an action or event request as
	 *             part of the same client request, the parameters received with render
	 *             request must be the render parameters set during the action or event
	 *             request.
	 */
	public void SetRenderParametersTest() throws Fault {

		TSPortletInfo portletInfo = new TSPortletInfo(getDefaultPortletApp(),
				"SetRenderParametersTestPortlet");

		/***********************************************************************
		 * FIRST TRIP:: To get a portlet url string written by the portlet
		 **********************************************************************/

		/*
		 * Sets the GET request to be sent out to the server.
		 */

		String firstRequest = getPortalURL(portletInfo);
		setRequestProperty(REQUEST, firstRequest);

		/*
		 * Invokes the test.
		 */
		HttpResponse firstResponse = invoke();

		/***********************************************************************
		 * SECOND TRIP:: To send a request using PortletURL returned in previous
		 * request.
		 **********************************************************************/

		/*
		 * Extract the PortletURL string from the content of HttpResponse of the
		 * first pass
		 */
		String portletURLStr = PortletURLClientTag
				.extractContent(firstResponse);

		/*
		 * Sets the second GET request to be sent out to the server.
		 */
		String secondRequest = getPortalReturnURL(portletURLStr);
		setRequestProperty(REQUEST, secondRequest);

		/*
		 * Sets the test criteria.
		 */
		setCriteriaProperty(SEARCH_STRING, ResultWriter
				.getPassedString(getTestName()));

		/*
		 * Invokes the test.
		 */

		invoke(firstResponse);
	}
	
    /*
	 * @testName: CheckSendEventTest
	 * @assertion_ids: PORTLET:SPEC:135
	 * @test_Strategy: In the first call, the render method generate
	 *                 an actionURL and write it to the output stream.
	 *                 The url is extracted and used for a second request.
	 *                 In the processAction method is triggert an
	 *                 event.
	 *                 In the processEvent method is checked this event and
	 *                 triggert an other event. Then is checked that the new
	 *                 event is received.
	 * @assertion: The portlet can publish events via the StateAwareResponse.setEvent
	 *             method.
	 */
    public void CheckSendEventTest() throws Fault {

		TSPortletInfo portletInfo = new TSPortletInfo(
										getDefaultPortletApp(),
										"CheckSendEventTestPortlet");

		/*******************************************************************
		 * FIRST TRIP:: To get a portlet url string written by the portlet 
		 ******************************************************************/

		/*
		 * Sets the GET request to be sent out to the server.
		 */

		String firstRequest = getPortalURL(portletInfo);
		setRequestProperty(REQUEST, firstRequest);

		/*
		 * Invokes the test.
		 */
		HttpResponse firstResponse = invoke();

		/*******************************************************************
		 * SECOND TRIP:: To send a request using PortletURL returned 
		 * in previous request. 
		 ******************************************************************/

		/*
		 * Extract the PortletURL string from the content of 
		 * HttpResponse of the first pass
		 */
		String portletURLStr = PortletURLClientTag
				.extractContent(firstResponse);

		/*
		 * Sets the second GET request to be sent out to the server.
		 */
		String secondRequest = getPortalReturnURL(portletURLStr);
		setRequestProperty(REQUEST, secondRequest);

		/*
		 * Sets the test criteria.
		 */
		setCriteriaProperty(SEARCH_STRING, ResultWriter
				.getPassedString(getTestName()));

		/*
		 * Invokes the test.
		 */

		invoke(firstResponse);
	}
    
    /*
	 * @testName: SetMultipleEventsProcessActionTest
	 * @assertion_ids: PORTLET:SPEC:136
	 * @test_Strategy: In the first call, the render method generate
	 *                 an actionURL and write it to the output stream.
	 *                 The url is extracted and used for a second request.
	 *                 In the processAction method is triggert few
	 *                 events. In the processEvent method is checked
	 *                 that the events received.
	 * @assertion: It is also valid to call StateAwareResponse.setEvent
	 *             multiple times in the current processAction or
	 *             processEvent method.
	 */
    public void SetMultipleEventsProcessActionTest() throws Fault {

		TSPortletInfo portletInfo = new TSPortletInfo(
										getDefaultPortletApp(),
										"SetMultipleEventsProcessActionTestPortlet");

		/*******************************************************************
		 * FIRST TRIP:: To get a portlet url string written by the portlet 
		 ******************************************************************/

		/*
		 * Sets the GET request to be sent out to the server.
		 */

		String firstRequest = getPortalURL(portletInfo);
		setRequestProperty(REQUEST, firstRequest);

		/*
		 * Invokes the test.
		 */
		HttpResponse firstResponse = invoke();

		/*******************************************************************
		 * SECOND TRIP:: To send a request using PortletURL returned 
		 * in previous request. 
		 ******************************************************************/

		/*
		 * Extract the PortletURL string from the content of 
		 * HttpResponse of the first pass
		 */
		String portletURLStr = PortletURLClientTag
				.extractContent(firstResponse);

		/*
		 * Sets the second GET request to be sent out to the server.
		 */
		String secondRequest = getPortalReturnURL(portletURLStr);
		setRequestProperty(REQUEST, secondRequest);

		/*
		 * Sets the test criteria.
		 */
		setCriteriaProperty(SEARCH_STRING, ResultWriter
				.getPassedString(getTestName()));

		/*
		 * Invokes the test.
		 */

		invoke(firstResponse);
	}
    
    /*
	 * @testName: SetEventLocalPartTest
	 * @assertion_ids: PORTLET:SPEC:137
	 * @test_Strategy: In the first call, the render method generate
	 *                 an actionURL and write it to the output stream.
	 *                 The url is extracted and used for a second request.
	 *                 In the processAction method is set one event
	 *                 only by the local part of the name.
	 *                 In the processEvent method is checked that the events
	 *                 received.
	 * @assertion: If only the local part is specified the namespace must be
	 *             the default namespace defined in the portlet deployment
	 *             descriptor with the default-namespace element.
	 */
    public void SetEventLocalPartTest() throws Fault {

		TSPortletInfo portletInfo = new TSPortletInfo(
										getDefaultPortletApp(),
										"SetEventLocalPartTestPortlet");

		/*******************************************************************
		 * FIRST TRIP:: To get a portlet url string written by the portlet 
		 ******************************************************************/

		/*
		 * Sets the GET request to be sent out to the server.
		 */

		String firstRequest = getPortalURL(portletInfo);
		setRequestProperty(REQUEST, firstRequest);

		/*
		 * Invokes the test.
		 */
		HttpResponse firstResponse = invoke();

		/*******************************************************************
		 * SECOND TRIP:: To send a request using PortletURL returned 
		 * in previous request. 
		 ******************************************************************/

		/*
		 * Extract the PortletURL string from the content of 
		 * HttpResponse of the first pass
		 */
		String portletURLStr = PortletURLClientTag
				.extractContent(firstResponse);

		/*
		 * Sets the second GET request to be sent out to the server.
		 */
		String secondRequest = getPortalReturnURL(portletURLStr);
		setRequestProperty(REQUEST, secondRequest);

		/*
		 * Sets the test criteria.
		 */
		setCriteriaProperty(SEARCH_STRING, ResultWriter
				.getPassedString(getTestName()));

		/*
		 * Invokes the test.
		 */

		invoke(firstResponse);
	}
    
    /*
	 * @testName: CheckOpIgnoredTest
	 * @assertion_ids: PORTLET:SPEC:146
	 * @test_Strategy: In the first call, the render method generate
	 *                 an actionURL and write it to the output stream.
	 *                 The url is extracted and used for a second request.
	 *                 In the processAction method triggert an event.
	 *                 In processEvent are set some operations to
	 *                 EventResponse and an PortletException is thrown.
	 *                 In render method is checked that the operation are
	 *                 ignored.
	 * @assertion: If a portlet throws an exception in the processEvent
	 *             method, all operations on the EventResponse must be ignored.
	 */
    public void CheckOpIgnoredTest() throws Fault {

		TSPortletInfo portletInfo = new TSPortletInfo(
										getDefaultPortletApp(),
										"CheckOpIgnoredTestPortlet");

		/*******************************************************************
		 * FIRST TRIP:: To get a portlet url string written by the portlet 
		 ******************************************************************/

		/*
		 * Sets the GET request to be sent out to the server.
		 */

		String firstRequest = getPortalURL(portletInfo);
		setRequestProperty(REQUEST, firstRequest);

		/*
		 * Invokes the test.
		 */
		HttpResponse firstResponse = invoke();

		/*******************************************************************
		 * SECOND TRIP:: To send a request using PortletURL returned 
		 * in previous request. 
		 ******************************************************************/

		/*
		 * Extract the PortletURL string from the content of 
		 * HttpResponse of the first pass
		 */
		String portletURLStr = PortletURLClientTag
				.extractContent(firstResponse);

		/*
		 * Sets the second GET request to be sent out to the server.
		 */
		String secondRequest = getPortalReturnURL(portletURLStr);
		setRequestProperty(REQUEST, secondRequest);

		/*
		 * Sets the test criteria.
		 */
		setCriteriaProperty(UNEXPECTED_RESPONSE_MATCH, ResultWriter
				.getFailedString(getTestName()));

		/*
		 * Invokes the test.
		 */

		invoke(firstResponse);
	}
    
	/*
	 * @testName: SetRenderParametersReplaceTest
	 * @assertion_ids: PORTLET:SPEC:103
	 * @test_Strategy: First request to the portlet writes a actionURL to the
	 *                 output stream. The portlet URL string is extracted and used for second
	 *                 request. In the second request, processAction() will trigger an event. In
	 *                 processEvent() a renderparameter is set. And another render parameter
	 *                 with the same key is set on EventResponse. The portlet
	 *                 will check in render() if the second parameter available and the
	 *                 first is  replaced.
	 * @assertion: A call to any of the setRenderParameter methods must replace any
	 *             parameter with the same name previously set.
	 */
	public void SetRenderParametersReplaceTest() throws Fault {

		TSPortletInfo portletInfo = new TSPortletInfo(getDefaultPortletApp(),
											"SetRenderParametersReplaceTestPortlet");

		/***********************************************************************
		 * FIRST TRIP:: To get a portlet url string written by the portlet
		 **********************************************************************/

		/*
		 * Sets the GET request to be sent out to the server.
		 */

		String firstRequest = getPortalURL(portletInfo);
		setRequestProperty(REQUEST, firstRequest);

		/*
		 * Invokes the test.
		 */
		HttpResponse firstResponse = invoke();

		/***********************************************************************
		 * SECOND TRIP:: To send a request using PortletURL returned in previous
		 * request.
		 **********************************************************************/

		/*
		 * Extract the PortletURL string from the content of HttpResponse of the
		 * first pass
		 */
		String portletURLStr = PortletURLClientTag
				.extractContent(firstResponse);

		/*
		 * Sets the second GET request to be sent out to the server.
		 */
		String secondRequest = getPortalReturnURL(portletURLStr);
		setRequestProperty(REQUEST, secondRequest);

		/*
		 * Sets the test criteria.
		 */
		setCriteriaProperty(SEARCH_STRING, ResultWriter
				.getPassedString(getTestName()));

		/*
		 * Invokes the test.
		 */

		invoke(firstResponse);
	}
	
    /*
     * @testName: GetNamespaceSameValueTest
     * @assertion_ids: PORTLET:SPEC:101
     * @test_Strategy: Makes a first request to the portlet to get an
     *                 action PortletURL and set the return value
     *                 from the getNamespace method as parameter
     *                 on it In processAction trigger an event.
     *                 Then check in processEvent if
     *                 a new getNamespace call will return the same value.
     * @assertion: The getNamespace method must return the same value
     *             for the lifetime of the portlet window.
     */
	public void GetNamespaceSameValueTest() throws Fault {

		TSPortletInfo portletInfo = new TSPortletInfo(getDefaultPortletApp(),
											"GetNamespaceSameValueTestPortlet");

		/***********************************************************************
		 * FIRST TRIP:: To get a portlet url string written by the portlet
		 **********************************************************************/

		/*
		 * Sets the GET request to be sent out to the server.
		 */

		String firstRequest = getPortalURL(portletInfo);
		setRequestProperty(REQUEST, firstRequest);

		/*
		 * Invokes the test.
		 */
		HttpResponse firstResponse = invoke();

		/***********************************************************************
		 * SECOND TRIP:: To send a request using PortletURL returned in previous
		 * request.
		 **********************************************************************/

		/*
		 * Extract the PortletURL string from the content of HttpResponse of the
		 * first pass
		 */
		String portletURLStr = PortletURLClientTag
				.extractContent(firstResponse);

		/*
		 * Sets the second GET request to be sent out to the server.
		 */
		String secondRequest = getPortalReturnURL(portletURLStr);
		setRequestProperty(REQUEST, secondRequest);

		/*
		 * Sets the test criteria.
		 */
		setCriteriaProperty(SEARCH_STRING, ResultWriter
				.getPassedString(getTestName()));

		/*
		 * Invokes the test.
		 */

		invoke(firstResponse);
	}
	
    /*
     * @testName: SetUndeclaredPortletModeTest
     * @assertion_ids: PORTLET:SPEC:104
     * @test_Strategy: First request to the portlet writes a actionURL to the
	 *                 output stream. The portlet URL string is extracted and used for second
	 *                 request. In the second request, processAction() will trigger an event. In
	 *                 processEvent() the portlet mode is set to EDIT which was not declared in
     *                 the deployment descriptor.  Checks if PortletModeException is thrown.
     * @assertion: If a portlet attempts to set a portlet mode that it
     *             is not allowed to switch to, a PortletModeException
     *             must be thrown.
     */
	public void SetUndeclaredPortletModeTest() throws Fault {

		TSPortletInfo portletInfo = new TSPortletInfo(getDefaultPortletApp(),
											"SetUndeclaredPortletModeTestPortlet");

		/***********************************************************************
		 * FIRST TRIP:: To get a portlet url string written by the portlet
		 **********************************************************************/

		/*
		 * Sets the GET request to be sent out to the server.
		 */

		String firstRequest = getPortalURL(portletInfo);
		setRequestProperty(REQUEST, firstRequest);

		/*
		 * Invokes the test.
		 */
		HttpResponse firstResponse = invoke();

		/***********************************************************************
		 * SECOND TRIP:: To send a request using PortletURL returned in previous
		 * request.
		 **********************************************************************/

		/*
		 * Extract the PortletURL string from the content of HttpResponse of the
		 * first pass
		 */
		String portletURLStr = PortletURLClientTag
				.extractContent(firstResponse);

		/*
		 * Sets the second GET request to be sent out to the server.
		 */
		String secondRequest = getPortalReturnURL(portletURLStr);
		setRequestProperty(REQUEST, secondRequest);

		/*
		 * Sets the test criteria.
		 */
		setCriteriaProperty(SEARCH_STRING, ResultWriter
				.getPassedString(getTestName()));

		/*
		 * Invokes the test.
		 */

		invoke(firstResponse);
	}
	
    /*
     * @testName: SetUndeclaredWindowStateTest
     * @assertion_ids: PORTLET:SPEC:105
     * @test_Strategy: First request to the portlet writes a actionURL to the
	 *                 output stream. The portlet URL string is extracted and used for second
	 *                 request. In the second request, processAction() will trigger an event. In
	 *                 processEvent() the window state is set to a window state which was not declared in
     *                 the deployment descriptor.  Checks if WindowStateException is thrown.
     * @assertion: If a portlet attempts to set a window state that it is not allowed to
     *             switch to, a WindowStateException must be thrown.
     */
	public void SetUndeclaredWindowStateTest() throws Fault {

		TSPortletInfo portletInfo = new TSPortletInfo(getDefaultPortletApp(),
											"SetUndeclaredWindowStateTestPortlet");

		/***********************************************************************
		 * FIRST TRIP:: To get a portlet url string written by the portlet
		 **********************************************************************/

		/*
		 * Sets the GET request to be sent out to the server.
		 */

		String firstRequest = getPortalURL(portletInfo);
		setRequestProperty(REQUEST, firstRequest);

		/*
		 * Invokes the test.
		 */
		HttpResponse firstResponse = invoke();

		/***********************************************************************
		 * SECOND TRIP:: To send a request using PortletURL returned in previous
		 * request.
		 **********************************************************************/

		/*
		 * Extract the PortletURL string from the content of HttpResponse of the
		 * first pass
		 */
		String portletURLStr = PortletURLClientTag
				.extractContent(firstResponse);

		/*
		 * Sets the second GET request to be sent out to the server.
		 */
		String secondRequest = getPortalReturnURL(portletURLStr);
		setRequestProperty(REQUEST, secondRequest);

		/*
		 * Sets the test criteria.
		 */
		setCriteriaProperty(SEARCH_STRING, ResultWriter
				.getPassedString(getTestName()));

		/*
		 * Invokes the test.
		 */

		invoke(firstResponse);
	}
}
