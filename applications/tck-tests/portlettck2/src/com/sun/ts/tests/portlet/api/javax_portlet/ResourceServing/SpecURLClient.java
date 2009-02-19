/*
 * Copyright 2006 IBM Corporation
 */

/**
 * @author Kay Schieck <schieck@inf.uni-jena.de>
 */

package com.sun.ts.tests.portlet.api.javax_portlet.ResourceServing;

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
        return "portlet_jp_ResourceServing_web";
    }
    
    /*
     * @class.setup_props: ts_home;
     */            
    
    
    /*
     * @testName: GetMethodTest
     * @assertion_ids: PORTLET:SPEC:122;
     * @test_Strategy: First request is to a portlet that writes a resource URL 
     *                 to the response object. In the second request in the 
     *                 serveResource() method the getMethod() method is invoked.
     *                 Test passes if getMethod() returns the GET.
     * @assertion: The portlet must be able to get the
     * 			   HTTP method with which this request
     * 			   was made, for example, GET, POST,
     * 			   or PUT, via the getMethod call
     * 			   on the ResourceRequest.
     */
    public void GetMethodTest()throws Fault {
    	TSPortletInfo portletInfo = new TSPortletInfo(
                getDefaultPortletApp(),
                "GetMethodTestPortlet");

		/*****************************************************************
		* FIRST TRIP: To get a ResourceURL string created by
		*             RenderResponse.createResourceURL().
		*****************************************************************/
		
		/*
		* Sets the GET request to be sent out to the server.
		*/
		String firstRequest = getPortalURL(portletInfo);
		setRequestProperty(REQUEST, firstRequest);
		
		/*
		* Sets the test criteria.
		*/
		setCriteriaProperty(UNEXPECTED_RESPONSE_MATCH,
		ResultWriter.getFailedString(getTestName()));
		
		/*
		* Invokes the test.
		*/
		HttpResponse firstResponse = invoke();
		
		/*****************************************************************
		* SECOND TRIP: To send the second request to call
		* serveResource()
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
     * @testName: GetResourceIDTest
     * @assertion_ids: PORTLET:SPEC:123;
     * @test_Strategy: First request is to a portlet that writes a resource URL 
     *                 to the response object and sets a resourceID. In the
     *                 second request in the serveResource() method the 
     *                 getResourceID() method is invoked.
     *                 Test passes if getResourceId() returns the the value,
     *                 which is set in the request before.
     * @assertion: The portlet must be able to get the resource ID that was
     * 			   set on the resource URL with the setResouceID method via
     * 			   the getResourceID method from the resource request.
     */
     public void GetResourceIDTest() throws Fault {
    	 TSPortletInfo portletInfo = new TSPortletInfo(
                 getDefaultPortletApp(),
                 "GetResourceIDTestPortlet");
    	 /*****************************************************************
          * FIRST TRIP: To get a ResourceURL string created by
          *             RenderResponse.createResourceURL().
          *****************************************************************/

         /*
          * Sets the GET request to be sent out to the server.
          */
         String firstRequest = getPortalURL(portletInfo);
         setRequestProperty(REQUEST, firstRequest);

         /*
          * Sets the test criteria.
          */
         setCriteriaProperty(UNEXPECTED_RESPONSE_MATCH,
             ResultWriter.getFailedString(getTestName()));

         /*
          * Invokes the test.
          */
         HttpResponse firstResponse = invoke();

         /*****************************************************************
          * SECOND TRIP: To send the second request to call
          * serveResource()
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
      * @testName: GetNoResourceIDTest
      * @assertion_ids: PORTLET:SPEC:124;
      * @test_Strategy: First request is to a portlet that writes a resource URL 
      *                 to the response object and sets no resourceID. In the
      *                 second request in the serveResource() method the 
      *                 getResourceID() method is invoked.
      *                 Test passes if getResourceId() returns null.
      * @assertion: If no resource ID was set on the resource URL the
      * 			getResourceID method must return null.
      */
      public void GetNoResourceIDTest() throws Fault {
     	 TSPortletInfo portletInfo = new TSPortletInfo(
                  getDefaultPortletApp(),
                  "GetNoResourceIDTestPortlet");
     	 /*****************************************************************
           * FIRST TRIP: To get a ResourceURL string created by
           *             RenderResponse.createResourceURL().
           *****************************************************************/

          /*
           * Sets the GET request to be sent out to the server.
           */
          String firstRequest = getPortalURL(portletInfo);
          setRequestProperty(REQUEST, firstRequest);

          /*
           * Sets the test criteria.
           */
          setCriteriaProperty(UNEXPECTED_RESPONSE_MATCH,
              ResultWriter.getFailedString(getTestName()));

          /*
           * Invokes the test.
           */
          HttpResponse firstResponse = invoke();

          /*****************************************************************
           * SECOND TRIP: To send the second request to call
           * serveResource()
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
    * @testName: GetCreateResourceURLTest
    * @assertion_ids: PORTLET:SPEC:125;
    * @test_Strategy: First request is to a portlet that writes a resource URL 
    *                 to the response object. In the second request in the
    *                 serveResource() method the test PassString is set.
    * @assertion: When an end user invokes such a resource URL the
    * 			  portlet container must call the serveResource
    * 			  method of the portlet or return a valid cached
    * 			  result for this resource URL.
    */
    public void GetCreateResourceURLTest() throws Fault {
        TSPortletInfo portletInfo = new TSPortletInfo(
                                        getDefaultPortletApp(),
                                        "GetCreateResourceURLTestPortlet");
        
        /*****************************************************************
         * FIRST TRIP: To get a ResourceURL string created by
         *             RenderResponse.createResourceURL().
         *****************************************************************/

        /*
         * Sets the GET request to be sent out to the server.
         */
        String firstRequest = getPortalURL(portletInfo);
        setRequestProperty(REQUEST, firstRequest);

        /*
         * Sets the test criteria.
         */
        setCriteriaProperty(UNEXPECTED_RESPONSE_MATCH,
            ResultWriter.getFailedString(getTestName()));

        /*
         * Invokes the test.
         */
        HttpResponse firstResponse = invoke();

        /*****************************************************************
         * SECOND TRIP: To send the second request to call
         * serveResource()
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
     * @testName: CheckProcessActionNotCalledForResourceURLTest
     * @assertion_ids: PORTLET:SPEC:126;
     * @test_Strategy: First request is to a portlet that writes a resource URL 
     *                 to the response object. In the second request in the
     *                 serveResource() method the test PassString is set, when
     *                 processAction isn't called.
     * @assertion: The portlet container must not call the
     *             processAction or processEvent method.
     */    
    public void CheckProcessActionNotCalledForResourceURLTest() throws Fault {
        TSPortletInfo portletInfo = new TSPortletInfo(
                                        getDefaultPortletApp(),
                                        "CheckProcessActionNotCalledForResourceURLTestPortlet");
        
        /*****************************************************************
         * FIRST TRIP: To get a ResourceURL string created by
         *             RenderResponse.createResourceURL().
         *****************************************************************/

        /*
         * Sets the GET request to be sent out to the server.
         */
        String firstRequest = getPortalURL(portletInfo);
        setRequestProperty(REQUEST, firstRequest);

        /*
         * Sets the test criteria.
         */
        setCriteriaProperty(UNEXPECTED_RESPONSE_MATCH,
            ResultWriter.getFailedString(getTestName()));

        /*
         * Invokes the test.
         */
        HttpResponse firstResponse = invoke();

        /*****************************************************************
         * SECOND TRIP: To send the second request to call
         * serveResource()
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
     * @testName: CheckResourceURLNotChangePortletModeTest
     * @assertion_ids: PORTLET:SPEC:127;Portlet:SPEC:128;
     * @test_Strategy: First request is to a portlet that writes a render URL 
     *                 to the response object. Second request is to a portlet
     *                 that writes a resource URL. In the third request we show
     *                 if the changes for request are made. If so test fails.
     * @assertion: ResourceURLs cannot change the current
     * 			   portlet mode, window state or render parameters.
     * 			   If a parameter is set that has the same name as
     * 			   a render parameter that this resource URL contains,
     * 			   the render parameter values must be the last
     * 			   entries in the parameter value array.
     */   
    public void CheckResourceURLNotChangePortletModeTest() throws Fault {
        TSPortletInfo portletInfo = new TSPortletInfo(
                					getDefaultPortletApp(),
                					"CheckResourceURLNotChangePortletModeTestPortlet");
        
        /*****************************************************************
         * FIRST TRIP: To get a ResourceURL string created by
         *             RenderResponse.createResourceURL()
         *****************************************************************/
        
        /*
         * Sets the GET request to be sent out to the server.
         */
        String firstRequest = getPortalURL(portletInfo);
        setRequestProperty(REQUEST, firstRequest);

        /*
         * Sets the test criteria.
         */
        setCriteriaProperty(UNEXPECTED_RESPONSE_MATCH,
            ResultWriter.getFailedString(getTestName()));

        /*
         * Invokes the test.
         */
        HttpResponse firstResponse = invoke();

        /*****************************************************************
         * SECOND TRIP: To send the second request to call
         * render()
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
        /*
         * Sets the test criteria.
         */
        setCriteriaProperty(UNEXPECTED_RESPONSE_MATCH,
            ResultWriter.getFailedString(getTestName()));

        /*
         * Invokes the test.
         */
        HttpResponse secondResponse = invoke(firstResponse);
        /*****************************************************************
         * SECOND TRIP: To send the second request to call
         * serveResource()
         *****************************************************************/

        /*
         * Extracts the PortletURL string from the content of the
         * HttpResponse of the first pass.
         */
         portletURL = PortletURLClientTag.extractContent(secondResponse);

        /*
         * Sets the second GET request to be sent out to the server.
         */
        String thirdRequest = getPortalReturnURL(portletURL);
        setRequestProperty(REQUEST, thirdRequest);

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
     * @testName: CheckNoFULLCachabilityTest
     * @assertion_ids: PORTLET:SPEC:130;PORTLET:SPEC:131;
     * @test_Strategy: First request is to a portlet that writes a resource URL
     * 				   with FULL cachability to the response object. Second
     * 				   request is to a portlet and checks in serveResource if
     * 				   setting a cachability different from FULL throws an
     * 				   IllegalStateException.
     * @assertion: Setting a cachability different from FULL
     * 			   must result in an IllegalStateException.
     */
    public void CheckNoFULLCachabilityTest() throws Fault {
        TSPortletInfo portletInfo = new TSPortletInfo(
                                        getDefaultPortletApp(),
                                        "CheckNoFULLCachabilityTestPortlet");
        
        /*****************************************************************
         * FIRST TRIP: To get a ResourceURL string created by
         *             RenderResponse.createResourceURL().
         *****************************************************************/

        /*
         * Sets the GET request to be sent out to the server.
         */
        String firstRequest = getPortalURL(portletInfo);
        setRequestProperty(REQUEST, firstRequest);

        /*
         * Sets the test criteria.
         */
        setCriteriaProperty(UNEXPECTED_RESPONSE_MATCH,
            ResultWriter.getFailedString(getTestName()));

        /*
         * Invokes the test.
         */
        HttpResponse firstResponse = invoke();

        /*****************************************************************
         * SECOND TRIP: To send the second request to call
         * serveResource()
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
     * @testName: CheckNoFULLResourceURLTest
     * @assertion_ids: PORTLET:SPEC:131;SPEC:132;
     * @test_Strategy: 
     * @assertion: Attempts to create URLs that are
     * 			   not of type FULL or are not resource
     * 			   URLs in the current or a downstream
     * 			   response must result in an IllegalStateException.
     */
    public void CheckNoFULLResourceURLTest() throws Fault {
        TSPortletInfo portletInfo = new TSPortletInfo(
                                        getDefaultPortletApp(),
                                        "CheckNoFULLResourceURLTestPortlet");
        
        /*****************************************************************
         * FIRST TRIP: To get a ResourceURL string created by
         *             RenderResponse.createResourceURL().
         *****************************************************************/

        /*
         * Sets the GET request to be sent out to the server.
         */
        String firstRequest = getPortalURL(portletInfo);
        setRequestProperty(REQUEST, firstRequest);

        /*
         * Sets the test criteria.
         */
        setCriteriaProperty(UNEXPECTED_RESPONSE_MATCH,
            ResultWriter.getFailedString(getTestName()));

        /*
         * Invokes the test.
         */
        HttpResponse firstResponse = invoke();

        /*****************************************************************
         * SECOND TRIP: To send the second request to call
         * serveResource()
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
     * @testName: CheckNoPORTLETResourceURLTest
     * @assertion_ids: PORTLET:SPEC:132;
     * @test_Strategy: 
     * @assertion: Creating other URLs, e.g. resource URLs of
     * 			   type PAGE or action or render URLs, must
     * 			   result in an IllegalStateException.
     */
    public void CheckNoPORTLETResourceURLTest() throws Fault {
        TSPortletInfo portletInfo = new TSPortletInfo(
                                        getDefaultPortletApp(),
                                        "CheckNoPORTLETResourceURLTestPortlet");
        
        /*****************************************************************
         * FIRST TRIP: To get a ResourceURL string created by
         *             RenderResponse.createResourceURL().
         *****************************************************************/

        /*
         * Sets the GET request to be sent out to the server.
         */
        String firstRequest = getPortalURL(portletInfo);
        setRequestProperty(REQUEST, firstRequest);

        /*
         * Sets the test criteria.
         */
        setCriteriaProperty(UNEXPECTED_RESPONSE_MATCH,
            ResultWriter.getFailedString(getTestName()));

        /*
         * Invokes the test.
         */
        HttpResponse firstResponse = invoke();

        /*****************************************************************
         * SECOND TRIP: To send the second request to call
         * serveResource()
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
