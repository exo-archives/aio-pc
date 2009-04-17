/*
 * Copyright 2007 IBM Corporation
 */

/**
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 */


package com.sun.ts.tests.portlet.api.javax_portlet.ResourceRequest;

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
        return "portlet_jp_ResourceRequest_web";
    }
    
    /*
     * @class.setup_props: ts_home;
     */            
    
   /*
    * @testName: GetParameterValuesTest
    * @assertion_ids: PORTLET:SPEC:69;
    * @test_Strategy: First request to the portlet writes a resource portletURL
    *                 with a paramater with multiple values in it, to the
    *                 output stream. The portlet URL string is extracted and
    *                 used for second request. In the second request, in
    *                 serveResource, portlet uses ResourceRequest.getParameterValues()
    *                 to see if the expected parameter values are returned.
    * @assertion: The value returned from the getParameter method must be the
    *             first value in the array of String objects returned by
    *             getParameterValues. 
    */
    public void GetParameterValuesTest() throws Fault {
        TSPortletInfo portletInfo = new TSPortletInfo(
                                        getDefaultPortletApp(),
                                        "GetParameterValuesTestPortlet");
        
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
     * @testName: CheckSingleParameterValuesTest
     * @assertion_ids: PORTLET:SPEC:70;
     * @test_Strategy: Test for ResourceRequest.getParameterValues() method.
     *                 First request to the portlet writes a resourceURL with
     *                 a parameter having 1 value. The portlet URL string is
     *                 extracted and used for second request. The portlet uses
     *                 ResourcetRequest.getParameterValues() to check the returned
     *                 array.
     * @assertion: If there is a single parameter value associated with a parameter
     *             name the method returns must return an array of size one
     *             containing the parameter value.
     */
     public void CheckSingleParameterValuesTest() throws Fault {
         TSPortletInfo portletInfo = new TSPortletInfo(
                                         getDefaultPortletApp(),
                                         "CheckSingleParameterValuesTestPortlet");
         
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
      * @testName: GetParameterMapTest
      * @assertion_ids: PORTLET:SPEC:71;
      * @test_Strategy: First request to the portlet writes a action resourceURL
      *                 with few paramaters in it, to the output stream. The
      *                 portlet URL string is extracted and used for second
      *                 request. In the second request, portlet uses
      *                 ResourceRequest.getParameterMap() to see if the expected
      *                 parameter map is returned and unmodifiable.
      * @assertion: The getParameterMap method must return an unmodifiable
      *             Map object.
      */
     public void GetParameterMapTest() throws Fault {

         TSPortletInfo portletInfo = new TSPortletInfo(
                                         getDefaultPortletApp(),
                                         "GetParameterMapTestPortlet");

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
          String portletURLStr = PortletURLClientTag.extractContent(
                                         firstResponse);

          /*
           * Sets the second GET request to be sent out to the server.
           */
         String secondRequest = getPortalReturnURL(portletURLStr);
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
      * @testName: GetParameterMapEmptyTest
      * @assertion_ids: PORTLET:SPEC:72;
      * @test_Strategy: First request to the portlet writes a resourceURL
      *                 with no paramaters in it, to the output stream.
      *                 The portlet URL string is extracted and used for second
      *                 request. In the second request, portlet uses
      *                 ResourceRequest.getParameterMap() to see if the empty map is returned.
      * @assertion: If the request does not have any parameter, the
      *             getParameterMap must return an empty Map object.
      */
     public void GetParameterMapEmptyTest() throws Fault {

         TSPortletInfo portletInfo = new TSPortletInfo(
                                         getDefaultPortletApp(),
                                         "GetParameterMapEmptyTestPortlet");

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
          String portletURLStr = PortletURLClientTag.extractContent(
                                         firstResponse);

          /*
           * Sets the second GET request to be sent out to the server.
           */
         String secondRequest = getPortalReturnURL(portletURLStr);
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
      * @testName: GetLifecyclePhaseAttributeTest
      * @assertion_ids: PORTLET:SPEC:90;
      * @test_Strategy: In the first call, the render method generate
      *                 an resourceURL and write it to the output stream.
      *                 The url is extracted and used for a second request.
      *                 In the serveResource method is checked that the
      *                 getAttribute methode returned the right lifecycle
      *                 phase string.
      * @assertion: This attribute value must be ACTION_PHASE if the current
      *             request is of type ActionRequest, EVENT_PHASE if the
      *             current request is of type EventRequest, RENDER_PHASE
      *             if the current request is of type RenderRequest,
      *             and RESOURCE_SERVING_PHASE if the current request
      *             is of type ResourceRequest. 
      */
     public void GetLifecyclePhaseAttributeTest() throws Fault {

         TSPortletInfo portletInfo = new TSPortletInfo(
                                         getDefaultPortletApp(),
                                         "GetLifecyclePhaseAttributeTestPortlet");

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
          String portletURLStr = PortletURLClientTag.extractContent(
                                         firstResponse);

          /*
           * Sets the second GET request to be sent out to the server.
           */
         String secondRequest = getPortalReturnURL(portletURLStr);
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
      * @testName: GetContextPathSyntaxTest
      * @assertion_ids: PORTLET:SPEC:91;PORTLET:SPEC:92;
      * @test_Strategy: In the first call, the render method generate
      *                 an resourceURL and write it to the output stream.
      *                 The url is extracted and used for a second request.
      *                 In the serveResource method is checked that the
      *                 syntax for path returned by GetContextPath() method
      *                 is correct.
      * @assertion: If the portlet application is rooted at the base of the
      *             web server URL namespace (also known as "default" context),
      *             this path must be an empty string.cii Otherwise, it must
      *             be the path the portlet application is rooted to, the
      *             path must start with a '/' and it must not end with
      *             a '/' character.
      */
     public void GetContextPathSyntaxTest() throws Fault {

         TSPortletInfo portletInfo = new TSPortletInfo(
                                         getDefaultPortletApp(),
                                         "GetContextPathSyntaxTestPortlet");

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
          String portletURLStr = PortletURLClientTag.extractContent(
                                         firstResponse);

          /*
           * Sets the second GET request to be sent out to the server.
           */
         String secondRequest = getPortalReturnURL(portletURLStr);
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
      * @testName: GetAuthTypeWithoutProtectionTest
      * @assertion_ids: PORTLET:SPEC:93;
      * @test_Strategy: In the first call, the render method generate
      *                 an resourceURL and write it to the output stream.
      *                 The url is extracted and used for a second request.
      *                 In the serveResource method is checked that the
      *                 getRemoteUser() returns 
      *                 null if the user has not been authenticated.
      * @assertion: If the user is not authenticated the getAuthType
      *             method must return null.
      */
     public void GetAuthTypeWithoutProtectionTest() throws Fault {

         TSPortletInfo portletInfo = new TSPortletInfo(
                                         getDefaultPortletApp(),
                                         "GetAuthTypeWithoutProtectionTestPortlet");

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
          String portletURLStr = PortletURLClientTag.extractContent(
                                         firstResponse);

          /*
           * Sets the second GET request to be sent out to the server.
           */
         String secondRequest = getPortalReturnURL(portletURLStr);
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
