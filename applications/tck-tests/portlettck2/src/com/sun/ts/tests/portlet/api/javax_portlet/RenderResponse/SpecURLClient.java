/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.RenderResponse;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.common.webclient.http.HttpResponse;
import com.sun.ts.tests.portlet.common.client.BasePortletUrlClient;
import com.sun.ts.tests.portlet.common.client.TSPortletInfo;
import com.sun.ts.tests.portlet.common.client.tags.PortletURLClientTag;
import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * URLClient and SpecURLClient are used as HTTP test clients to test
 * the Portlet API and specification assertions.  Each assertion test
 * is performed by executing a method in the *URLClient class that has
 * the @testName, @assertion_ids, @test_Strategy, and @assertion tags
 * in its javadoc comments.
 * <p>
 * In each test, at least one HTTP request is made to retrieve the
 * portal page that interacts with portlets that participate in the
 * corresponding test.  These test portlets/servlets, bundled in WAR
 * files, are assumed to be already deployed on the portal server
 * before the test is run.  The initial URL of the portal page is
 * obtained, using either declarative or programmatic configuration,
 * by the base class method <code>getPortalURL</code>, and is set for
 * the test using the <code>setRequestProperty</code> method.  See the
 * Technology Compatibility Kit Requirements chapter of the Portlet
 * Specification for details.  Subsequent requests for the test are
 * done using URLs, generated by PortletURL, that are part of the
 * returned portal pages.
 * <p>
 * The test sets the test success criteria using the
 * <code>setCriteriaProperty</code> method to look for either expected
 * or unexpected substrings in the portal page returned to decide
 * whether a test has passed or failed.
 * <p>
 * Finally, the <code>invoke()</code> method makes the HTTP request
 * and validates the output for the configured success criteria.  In
 * case of failure, this method throws a <code>Fault</code> exception
 * that is caught by the framework to report a failure for the test. 
 */
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
        return "portlet_jp_RenderResponse_web";
    }


    /*
     * @class.setup_props: ts_home;
     */                    

    /*
     * @testName: GetNullContentTypeTest
     * @assertion_ids: PORTLET:SPEC:78;
     * @test_Strategy: Checks that getContentType() returns null
     *                 without setting the content type.
     * @assertion: Otherwise, the getContentType method must return null.
     */
    public void GetNullContentTypeTest() throws Fault {
        /*
         * Sets the GET request to be sent out to the server.
         */
        TSPortletInfo portletInfo = new TSPortletInfo(
                                        getDefaultPortletApp(),
                                        "GetNullContentTypeTestPortlet");

        String request = getPortalURL(portletInfo);
        setRequestProperty(REQUEST, request);

        /*
         * Sets the test criteria.
         */
        setCriteriaProperty(SEARCH_STRING,
            ResultWriter.getPassedString(getTestName()));

        /*
         * Invokes the test.
         */
        invoke();  
    }

    /*
     * @testName: ValidIdentifierTest
     * @assertion_ids: PORTLET:SPEC:88;
     * @test_Strategy: Checks if RenderResponse.getNamespace() returns
     *                 a valid Java identifier.
     * @assertion: The getNamespace method must return a valid
     *             identifier as defined in the 3.8 Identifier Section
     *             of the Java Language Specification Second Edition.
     */
    public void ValidIdentifierTest() throws Fault {
        /*
         * Sets the GET request to be sent out to the server.
         */
        TSPortletInfo portletInfo = new TSPortletInfo(
                                        getDefaultPortletApp(),
                                        "ValidIdentifierTestPortlet");

        String request = getPortalURL(portletInfo);
        setRequestProperty(REQUEST, request);

        /*
         * Sets the test criteria.
         */
        setCriteriaProperty(SEARCH_STRING,
            ResultWriter.getPassedString(getTestName()));

        /*
         * Invokes the test.
         */
        invoke();
    }
    
    /*
	 * @testName: GetNamespaceSameValueTest
	 * @assertion_ids: PORTLET:SPEC:101
	 * @test_Strategy: In the first call, the render method generate
	 *                 a renderURL and set the result of the getNamespace()
	 *                 method as a papameter on it and write it to the
	 *                 output stream.
	 *                 The url is extracted and used for a second request.
	 *                 In the second render call, a new value of
	 *                 getNamespace() method requested and compared with
	 *                 the old value.
	 * @assertion: The getNamespace method must return the same value
	 *             for the lifetime of the portlet window.
	 */
    public void GetNamespaceSameValueTest() throws Fault {

		TSPortletInfo portletInfo = new TSPortletInfo(
										getDefaultPortletApp(),
										"GetNamespaceSameValueTestPortlet");

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
}
