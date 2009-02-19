/*
 * Copyright 2007 IBM Corporation
 */

/**
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 */

package com.sun.ts.tests.portlet.api.javax_portlet.ResourceResponse;

import com.sun.javatest.Status;
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
		return "portlet_jp_ResourceResponse_web";
	}

	/*
	 * @class.setup_props: ts_home;
	 */
	
    /*
     * @testName: GetNamespaceSameValueTest
     * @assertion_ids: PORTLET:SPEC:101;
     * @test_Strategy: Makes a first request to the portlet to get an
     *                 resourceURL and set the return value
     *                 from the getNamespace method as parameter
     *                 on it. Then check in serveResource if
     *                 a new getNamespace call will return the same value.
     * @assertion: The getNamespace method must return the same value
     *             for the lifetime of the portlet window.
     */
    public void GetNamespaceSameValueTest() throws Fault {
        TSPortletInfo portletInfo = new TSPortletInfo(
                                        getDefaultPortletApp(),
                                        "GetNamespaceSameValueTestPortlet");

        /*****************************************************************
         * FIRST TRIP: To get a PortletURL string created by
         *             RenderResponse.createActionURL().
         *****************************************************************/

        /*
         * Sets the GET request to be sent out to the server.
         */
        String firstRequest = getPortalURL(portletInfo);
        setRequestProperty(REQUEST, firstRequest);

        /*
         * Invokes the test.
         */
        HttpResponse firstResponse = invoke();

        /*****************************************************************
         * SECOND TRIP: To send the second request.
         *****************************************************************/

        /*
         * Extracts the PortletURL string from the content of the
         * HttpResponse of the first pass.
         */
         String portletURL = PortletURLClientTag.extractContent(firstResponse);

        /*
         * Sets the second GET request to be sent out to the server.
         */
        String secondRequest = getPortalReturnURL(portletURL);
        setRequestProperty(REQUEST, secondRequest);

        /*
         * Sets the test criteria.
         */

        setCriteriaProperty(SEARCH_STRING,
            ResultWriter.getPassedString(getTestName()));

        /*
         * Invokes the test.
         */
        invoke(firstResponse);
    }
    
    /*
     * @testName: GetNullContentTypeTest
     * @assertion_ids: PORTLET:SPEC:113
     * @test_Strategy: Makes a first request to the portlet to get an
     *                 resourceURL and write it to the output stream.
     *                 The URL is exctracted and used for a second request.
     *                 In the serveResource method is checked that
     *                 the result value of getContentType is null.
     * @assertion: If the portlet has set a content type, the
     *             getContentType method must return it. Otherwise, the
     *             getContentType method must return null.
     */
    public void GetNullContentTypeTest() throws Fault {
        TSPortletInfo portletInfo = new TSPortletInfo(
                                        getDefaultPortletApp(),
                                        "GetNullContentTypeTestPortlet");

        /*****************************************************************
         * FIRST TRIP: To get a PortletURL string created by
         *             RenderResponse.createActionURL().
         *****************************************************************/

        /*
         * Sets the GET request to be sent out to the server.
         */
        String firstRequest = getPortalURL(portletInfo);
        setRequestProperty(REQUEST, firstRequest);

        /*
         * Invokes the test.
         */
        HttpResponse firstResponse = invoke();

        /*****************************************************************
         * SECOND TRIP: To send the second request.
         *****************************************************************/

        /*
         * Extracts the PortletURL string from the content of the
         * HttpResponse of the first pass.
         */
         String portletURL = PortletURLClientTag.extractContent(firstResponse);

        /*
         * Sets the second GET request to be sent out to the server.
         */
        String secondRequest = getPortalReturnURL(portletURL);
        setRequestProperty(REQUEST, secondRequest);

        /*
         * Sets the test criteria.
         */

        setCriteriaProperty(SEARCH_STRING,
            ResultWriter.getPassedString(getTestName()));

        /*
         * Invokes the test.
         */
        invoke(firstResponse);
    }
    
    /*
     * @testName: GetContentTypeTest
     * @assertion_ids: PORTLET:SPEC:114
     * @test_Strategy: Makes a first request to the portlet to get an
     *                 resourceURL and write it to the output stream.
     *                 The URL is exctracted and used for a second request.
     *                 In the serveResource method is set the content type
     *                 and checked that the result value of getContentType
     *                 is the same.
     * @assertion: If the portlet has set a content type, the
     *             getContentType method must return it. Otherwise, the
     *             getContentType method must return null.
     */
    public void GetContentTypeTest() throws Fault {
        TSPortletInfo portletInfo = new TSPortletInfo(
                                        getDefaultPortletApp(),
                                        "GetContentTypeTestPortlet");

        /*****************************************************************
         * FIRST TRIP: To get a PortletURL string created by
         *             RenderResponse.createActionURL().
         *****************************************************************/

        /*
         * Sets the GET request to be sent out to the server.
         */
        String firstRequest = getPortalURL(portletInfo);
        setRequestProperty(REQUEST, firstRequest);

        /*
         * Invokes the test.
         */
        HttpResponse firstResponse = invoke();

        /*****************************************************************
         * SECOND TRIP: To send the second request.
         *****************************************************************/

        /*
         * Extracts the PortletURL string from the content of the
         * HttpResponse of the first pass.
         */
         String portletURL = PortletURLClientTag.extractContent(firstResponse);

        /*
         * Sets the second GET request to be sent out to the server.
         */
        String secondRequest = getPortalReturnURL(portletURL);
        setRequestProperty(REQUEST, secondRequest);

        /*
         * Sets the test criteria.
         */

        setCriteriaProperty(SEARCH_STRING,
            ResultWriter.getPassedString(getTestName()));

        /*
         * Invokes the test.
         */
        invoke(firstResponse);
    }
    
    /*
     * @testName: GetWriterAfterGetPortletOutputStreamTest
     * @assertion_ids: PORTLET:SPEC:114
     * @test_Strategy: Makes a first request to the portlet to get an
     *                 resourceURL and write it to the output stream.
     *                 The URL is exctracted and used for a second request.
     *                 Invokes ResourceResponse.getPortletOutputStream()
     *                 followed by ResourceResponse.getWriter().  Checks
     *                 if IllegalStateException is thrown.
     * @assertion: A portlet may generate its content by writing to the
     *             OutputStream or to the Writer of the MimeResponse object.
     *             A portlet must use only one of these objects. The portlet
     *             container must throw an IllegalStateException if a
     *             portlet attempts to use both.
     */
    public void GetWriterAfterGetPortletOutputStreamTest() throws Fault {
        TSPortletInfo portletInfo = new TSPortletInfo(
                                        getDefaultPortletApp(),
                                        "GetWriterAfterGetPortletOutputStreamTestPortlet");

        /*****************************************************************
         * FIRST TRIP: To get a PortletURL string created by
         *             RenderResponse.createActionURL().
         *****************************************************************/

        /*
         * Sets the GET request to be sent out to the server.
         */
        String firstRequest = getPortalURL(portletInfo);
        setRequestProperty(REQUEST, firstRequest);

        /*
         * Invokes the test.
         */
        HttpResponse firstResponse = invoke();

        /*****************************************************************
         * SECOND TRIP: To send the second request.
         *****************************************************************/

        /*
         * Extracts the PortletURL string from the content of the
         * HttpResponse of the first pass.
         */
         String portletURL = PortletURLClientTag.extractContent(firstResponse);

        /*
         * Sets the second GET request to be sent out to the server.
         */
        String secondRequest = getPortalReturnURL(portletURL);
        setRequestProperty(REQUEST, secondRequest);

        /*
         * Sets the test criteria.
         */

        setCriteriaProperty(SEARCH_STRING,
            ResultWriter.getPassedString(getTestName()));

        /*
         * Invokes the test.
         */
        invoke(firstResponse);
    }
    
    /*
     * @testName: GetPortletOutputStreamAfterGetWriterTest
     * @assertion_ids: PORTLET:SPEC:114
     * @test_Strategy: Makes a first request to the portlet to get an
     *                 resourceURL and write it to the output stream.
     *                 The URL is exctracted and used for a second request.
     *                 Invokes ResourceResponse.getWriter()
     *                 followed by ResourceResponse.getPortletOutputStream().
     *                 Checks if IllegalStateException is thrown.
     * @assertion: A portlet may generate its content by writing to the
     *             OutputStream or to the Writer of the MimeResponse object.
     *             A portlet must use only one of these objects. The portlet
     *             container must throw an IllegalStateException if a
     *             portlet attempts to use both.
     */
    public void GetPortletOutputStreamAfterGetWriterTest() throws Fault {
        TSPortletInfo portletInfo = new TSPortletInfo(
                                        getDefaultPortletApp(),
                                        "GetPortletOutputStreamAfterGetWriterTestPortlet");

        /*****************************************************************
         * FIRST TRIP: To get a PortletURL string created by
         *             RenderResponse.createActionURL().
         *****************************************************************/

        /*
         * Sets the GET request to be sent out to the server.
         */
        String firstRequest = getPortalURL(portletInfo);
        setRequestProperty(REQUEST, firstRequest);

        /*
         * Invokes the test.
         */
        HttpResponse firstResponse = invoke();

        /*****************************************************************
         * SECOND TRIP: To send the second request.
         *****************************************************************/

        /*
         * Extracts the PortletURL string from the content of the
         * HttpResponse of the first pass.
         */
         String portletURL = PortletURLClientTag.extractContent(firstResponse);

        /*
         * Sets the second GET request to be sent out to the server.
         */
        String secondRequest = getPortalReturnURL(portletURL);
        setRequestProperty(REQUEST, secondRequest);

        /*
         * Sets the test criteria.
         */

        setCriteriaProperty(SEARCH_STRING,
            ResultWriter.getPassedString(getTestName()));

        /*
         * Invokes the test.
         */
        invoke(firstResponse);
    }
    
    /*
     * @testName: GetBufferSizeTest
     * @assertion_ids: PORTLET:SPEC:115
     * @test_Strategy: Makes a first request to the portlet to get an
     *                 resourceURL and write it to the output stream.
     *                 The URL is exctracted and used for a second request.
     *                 Checks that the value returned by
     *                 ResourceResponse.getBufferSize() is non-negative.
     * @assertion: The getBufferSize method returns the size of the
     *             underlying buffer being used. If no buffering is being
     *             used, this method must return the int value of 0.
     */
    public void GetBufferSizeTest() throws Fault {
        TSPortletInfo portletInfo = new TSPortletInfo(
                                        getDefaultPortletApp(),
                                        "GetBufferSizeTestPortlet");

        /*****************************************************************
         * FIRST TRIP: To get a PortletURL string created by
         *             RenderResponse.createActionURL().
         *****************************************************************/

        /*
         * Sets the GET request to be sent out to the server.
         */
        String firstRequest = getPortalURL(portletInfo);
        setRequestProperty(REQUEST, firstRequest);

        /*
         * Invokes the test.
         */
        HttpResponse firstResponse = invoke();

        /*****************************************************************
         * SECOND TRIP: To send the second request.
         *****************************************************************/

        /*
         * Extracts the PortletURL string from the content of the
         * HttpResponse of the first pass.
         */
         String portletURL = PortletURLClientTag.extractContent(firstResponse);

        /*
         * Sets the second GET request to be sent out to the server.
         */
        String secondRequest = getPortalReturnURL(portletURL);
        setRequestProperty(REQUEST, secondRequest);

        /*
         * Sets the test criteria.
         */

        setCriteriaProperty(SEARCH_STRING,
            ResultWriter.getPassedString(getTestName()));

        /*
         * Invokes the test.
         */
        invoke(firstResponse);
    }
    
    /*
     * @testName: SetBufferSizeTest
     * @assertion_ids: PORTLET:SPEC:116
     * @test_Strategy: Makes a first request to the portlet to get an
     *                 resourceURL and write it to the output stream.
     *                 The URL is exctracted and used for a second request.
     *                 Invokes ResourceResponse.setBufferSize() to set a
     *                 preferred buffer size, then invokes
     *                 ResourceResponse.getBufferSize() to check that
     *                 the new buffer is at least as large as the size
     *                 requested.
     * @assertion: The portlet can request a preferred buffer size by
     *             using the setBufferSize method. The buffer assigned
     *             is not required to be the size requested by the
     *             portlet, but must be at least as large as the size
     *             requested.
     */
    public void SetBufferSizeTest() throws Fault {
        TSPortletInfo portletInfo = new TSPortletInfo(
                                        getDefaultPortletApp(),
                                        "SetBufferSizeTestPortlet");

        /*****************************************************************
         * FIRST TRIP: To get a PortletURL string created by
         *             RenderResponse.createActionURL().
         *****************************************************************/

        /*
         * Sets the GET request to be sent out to the server.
         */
        String firstRequest = getPortalURL(portletInfo);
        setRequestProperty(REQUEST, firstRequest);

        /*
         * Invokes the test.
         */
        HttpResponse firstResponse = invoke();

        /*****************************************************************
         * SECOND TRIP: To send the second request.
         *****************************************************************/

        /*
         * Extracts the PortletURL string from the content of the
         * HttpResponse of the first pass.
         */
         String portletURL = PortletURLClientTag.extractContent(firstResponse);

        /*
         * Sets the second GET request to be sent out to the server.
         */
        String secondRequest = getPortalReturnURL(portletURL);
        setRequestProperty(REQUEST, secondRequest);

        /*
         * Sets the test criteria.
         */

        setCriteriaProperty(SEARCH_STRING,
            ResultWriter.getPassedString(getTestName()));

        /*
         * Invokes the test.
         */
        invoke(firstResponse);
    }

    /*
     * @testName: ResetBufferIllegalStateExceptionTest
     * @assertion_ids: PORTLET:SPEC:118
     * @test_Strategy: Makes a first request to the portlet to get an
     *                 resourceURL and write it to the output stream.
     *                 The URL is exctracted and used for a second request.
     *                 Writes some content to the buffer and commits
     *                 the response.  Invokes RenderResponse.resetBuffer()
     *                 and checks if IllegalStateException is thrown.
     * @assertion: If the response is committed and the reset or
     *             resetBuffer method is called, an IllegalStateException
     *             must be thrown.
     */
    public void ResetBufferIllegalStateExceptionTest() throws Fault {
        TSPortletInfo portletInfo = new TSPortletInfo(
                                        getDefaultPortletApp(),
                                        "ResetBufferIllegalStateExceptionTestPortlet");

        /*****************************************************************
         * FIRST TRIP: To get a PortletURL string created by
         *             RenderResponse.createActionURL().
         *****************************************************************/

        /*
         * Sets the GET request to be sent out to the server.
         */
        String firstRequest = getPortalURL(portletInfo);
        setRequestProperty(REQUEST, firstRequest);

        /*
         * Invokes the test.
         */
        HttpResponse firstResponse = invoke();

        /*****************************************************************
         * SECOND TRIP: To send the second request.
         *****************************************************************/

        /*
         * Extracts the PortletURL string from the content of the
         * HttpResponse of the first pass.
         */
         String portletURL = PortletURLClientTag.extractContent(firstResponse);

        /*
         * Sets the second GET request to be sent out to the server.
         */
        String secondRequest = getPortalReturnURL(portletURL);
        setRequestProperty(REQUEST, secondRequest);

        /*
         * Sets the test criteria.
         */

        setCriteriaProperty(SEARCH_STRING,
            ResultWriter.getPassedString(getTestName()));

        /*
         * Invokes the test.
         */
        invoke(firstResponse);
    }
    
    /*
     * @testName: ResetIllegalStateExceptionTest
     * @assertion_ids: PORTLET:SPEC:118
     * @test_Strategy: Makes a first request to the portlet to get an
     *                 resourceURL and write it to the output stream.
     *                 The URL is exctracted and used for a second request.
     *                 Writes some content to the buffer and commits
     *                 the response.  Invokes RenderResponse.reset()
     *                 and checks if IllegalStateException is thrown.
     * @assertion: If the response is committed and the reset or
     *             resetBuffer method is called, an IllegalStateException
     *             must be thrown.
     */
    public void ResetIllegalStateExceptionTest() throws Fault {
        TSPortletInfo portletInfo = new TSPortletInfo(
                                        getDefaultPortletApp(),
                                        "ResetIllegalStateExceptionTestPortlet");

        /*****************************************************************
         * FIRST TRIP: To get a PortletURL string created by
         *             RenderResponse.createActionURL().
         *****************************************************************/

        /*
         * Sets the GET request to be sent out to the server.
         */
        String firstRequest = getPortalURL(portletInfo);
        setRequestProperty(REQUEST, firstRequest);

        /*
         * Invokes the test.
         */
        HttpResponse firstResponse = invoke();

        /*****************************************************************
         * SECOND TRIP: To send the second request.
         *****************************************************************/

        /*
         * Extracts the PortletURL string from the content of the
         * HttpResponse of the first pass.
         */
         String portletURL = PortletURLClientTag.extractContent(firstResponse);

        /*
         * Sets the second GET request to be sent out to the server.
         */
        String secondRequest = getPortalReturnURL(portletURL);
        setRequestProperty(REQUEST, secondRequest);

        /*
         * Sets the test criteria.
         */

        setCriteriaProperty(SEARCH_STRING,
            ResultWriter.getPassedString(getTestName()));

        /*
         * Invokes the test.
         */
        invoke(firstResponse);
    }
}
