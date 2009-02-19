/*
 * Copyright 2007 IBM Corporation
 */

/**
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 */

package com.sun.ts.tests.portlet.api.javax_portlet.EventRequest;

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
        return "portlet_jp_EventRequest_web";
    }
    
    /*
     * @class.setup_props: ts_home;
     */            
    
   /*
    * @testName: GetParameterValuesTest
    * @assertion_ids: PORTLET:SPEC:69;
    * @test_Strategy: First request to the portlet writes a
    *                 action portletURL to the output stream.
    *                 The portlet URL string is extracted and
    *                 used for second request. In the second
    *                 request, processAction() will set a parameter
    *                 with multiply values in the response.
    *                 In processEvent uses EventRequest.getParameterValues()
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
         * FIRST TRIP: To get a ActionURL string created by
         *             RenderResponse.createActionURL().
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
         * processAction and trigger an event
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
     * @test_Strategy:  First request to the portlet writes a action portletURL
     *                  to the output stream. The portlet URL string is extracted
     *                  and used for second request. In the second request,
     *                  processAction() will set a parameter with one value in
     *                  the response and send an event. The portlet uses 
     *                  EventRequest.getParameterValues() to check the returned array.
     * @assertion: If there is a single parameter value associated with a parameter
     *             name the method returns must return an array of size one
     *             containing the parameter value. 
     */
     public void CheckSingleParameterValuesTest() throws Fault {
         TSPortletInfo portletInfo = new TSPortletInfo(
                                         getDefaultPortletApp(),
                                         "CheckSingleParameterValuesTestPortlet");
         
         /*****************************************************************
          * FIRST TRIP: To get a ActionURL string created by
          *             RenderResponse.createActionURL().
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
          * processAction and trigger an event
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
      * @testName: CheckEventRequestParameterRenderTest
      * @assertion_ids: PORTLET:SPEC:74;
      * @test_Strategy:  First request to the portlet writes a action portletURL
      *                  to the output stream. The portlet URL string is extracted
      *                  and used for second request. In the second request,
      *                  processAction() will set a parameter. Check in render() the
      *                  parameter is not visible.
      * @assertion: The portlet-container must not propagate parameters received in
      *             an action or event request to subsequent render requests of the
      *             portlet.
      */
      public void CheckEventRequestParameterRenderTest() throws Fault {
          TSPortletInfo portletInfo = new TSPortletInfo(
                                          getDefaultPortletApp(),
                                          "CheckEventRequestParameterRenderTestPortlet");
          
          /*****************************************************************
           * FIRST TRIP: To get a ActionURL string created by
           *             RenderResponse.createActionURL().
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
           * processAction and trigger an event
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
       * @testName: CheckEventRequestParameterEventTest
       * @assertion_ids: PORTLET:SPEC:75;
       * @test_Strategy:  First request to the portlet writes a action portletURL
       *                  with parameters to the output stream. The portlet URL
       *                  string is extracted and used for second request.
       *                  Check in processEvent() the parameter is not visible.
       * @assertion: The portlet-container must not propagate parameters received
       *             in an action to subsequent event requests of the portlet.
       */
       public void CheckEventRequestParameterEventTest() throws Fault {
           TSPortletInfo portletInfo = new TSPortletInfo(
                                           getDefaultPortletApp(),
                                           "CheckEventRequestParameterEventTestPortlet");
           
           /*****************************************************************
            * FIRST TRIP: To get a ActionURL string created by
            *             RenderResponse.createActionURL().
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
            * processAction and trigger an event
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
        * @test_Strategy: First request to the portlet writes a actionURL to the output stream.
        *                 The portlet URL string is extracted and used for second request.
        *                 In the second request, few parameters set on ActionResponse and
        *                 trigger an event. The portlet uses EventRequest.getParameterMap()
        *                 to see if the expected parameter map is returned and unmodifiable.
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
        * @test_Strategy: First request to the portlet writes a actionURL to
        *                 the output stream. The portlet URL string is
        *                 extracted and used for second request. In the
        *                 second request, no parameters set on ActionResponse
        *                 and trigger an event. The portlet uses
        *                 EventRequest.getParameterMap() to see if the
        *                 empty map is returned.
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
        * @testName: GetParameterOnlySeenByTargetTest
        * @assertion_ids: PORTLET:SPEC:80;
        * @test_Strategy: First request to one portlet writes a 
        *                 actionURL to the output stream. 
        *                 The portlet URL string is extracted and used for second 
        *                 request. In processAction some parameters are set and a event
        *                 is triggert. The second portlet checks that the above
        *                 parameter is not available to it in the second request.
        * @assertion: A portlet must not see any parameter targeted to other 
        *             portlets.
        */
       public void GetParameterOnlySeenByTargetTest() throws Fault {

           TSPortletInfo portletInfo[] = new TSPortletInfo[2];

           portletInfo[0] = new TSPortletInfo(getDefaultPortletApp(),
                                      "GetParameterOnlySeenByTargetTestPortlet");

           portletInfo[1] = new TSPortletInfo( getDefaultPortletApp(),
                                     "GetParameterOnlySeenByTargetTest_1_Portlet");

           /*******************************************************************
            * FIRST TRIP:: To get a portlet url string with a parameter created by
            *              PortletRequest.creatPortletURL()
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


           /*
            * Extract the PortletURL string from the content of 
            * HttpResponse of the first pass
            */
            String portletURLStr = PortletURLClientTag.extractContent(firstResponse);

            String secondRequest = getPortalReturnURL( portletURLStr);
           
            setRequestProperty(REQUEST, secondRequest);

           /*
            * Sets the test criteria.
            */
           setCriteriaProperty(SEARCH_STRING,
               ResultWriter.getPassedString(getTestName()));
           setCriteriaProperty(UNEXPECTED_RESPONSE_MATCH,
                   ResultWriter.getFailedString(getTestName()));

           /*
            * Invokes the test.
            */

           invoke(firstResponse);  
       }
       
   	   /*
        * @testName: GetLifecyclePhaseAttributeTest
        * @assertion_ids: PORTLET:SPEC:90;
        * @test_Strategy: In the first call, the render method generate
        *                 an actionURL and write it to the output stream.
        *                 The url is extracted and used for a second request.
        *                 In the processAction method is triggert an
        *                 event.
        *                 In the processEvent method is checked that the
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
        *                 an actionURL and write it to the output stream.
        *                 The url is extracted and used for a second request.
        *                 In the processAction method is triggert an
        *                 event.
        *                 In the processEvent method is checked that the
        *                 syntax for path returned by GetContextPath()
        *                 method is correct.
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
	 *                 an actionURL and write it to the output stream.
	 *                 The url is extracted and used for a second request.
	 *                 In the processAction method is triggert an
	 *                 event.
	 *                 In the processEvent method is checked that the
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
	 * @testName: GetResponseContentTypesTest
	 * @assertion_ids: PORTLET:SPEC:94;
	 * @test_Strategy: In the first call, the render method generate
	 *                 an actionURL and write it to the output stream.
	 *                 The url is extracted and used for a second request.
	 *                 In the processAction method is triggert an
	 *                 event.
	 *                 In the processEvent method is checked that the
	 *                 return value of getResponseContentTypes() method is not null.
	 *                 And the first item in the list is same as returned by
	 *                 getResponseContentType().
	 * @assertion: The returned Enumeration of strings should contain the
	 *             content types the portlet container supports in order
	 *             of preference. The first element of the enumeration
	 *             must be the same content type returned by the
	 *             getResponseContentType method.
	 */
    public void GetResponseContentTypesTest() throws Fault {

		TSPortletInfo portletInfo = new TSPortletInfo(
										getDefaultPortletApp(),
										"GetResponseContentTypesTestPortlet");

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
	 * @testName: GetEventNameTest
	 * @assertion_ids: PORTLET:SPEC:133
	 * @test_Strategy: In the first call, the render method generate
	 *                 an actionURL and write it to the output stream.
	 *                 The url is extracted and used for a second request.
	 *                 In the processAction method is triggert an
	 *                 event with value null.
	 *                 In the processEvent method is checked that
	 *                 the correct event is received. 
	 * @assertion: The event name must always have a name and may
	 * 			   optionally have a value.
	 */
	public void GetEventNameTest () throws Fault {
		
		TSPortletInfo portletInfo = new TSPortletInfo(
										getDefaultPortletApp(),
										"GetEventNameTestPortlet");

		/*****************************************************************
		 * FIRST TRIP: To get a ActionURL string created by
		 *             RenderResponse.createActionURL().
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
		 * processAction()
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
	 * @testName: CheckEventPayloadTypeTest
	 * @assertion_ids: PORTLET:SPEC:134;
	 * @test_Strategy: In the first call, the render method generate
	 *                 an actionURL and write it to the output stream.
	 *                 The url is extracted and used for a second request.
	 *                 In the processAction method is triggert an
	 *                 event with value null.
	 *                 In the processEvent method is checked that
	 *                 the event is correct serilizable type.
	 * @assertion: If the event has a value it must be based on the
	 *             type defined in the deployment descriptor. The
	 *             default XML to Java mapping that every container
	 *             must support is the JAXB mapping.
	 */
	public void CheckEventPayloadTypeTest () throws Fault {
		TSPortletInfo portletInfo = new TSPortletInfo(
										getDefaultPortletApp(),
										"CheckEventPayloadTypeTestPortlet");

		/*****************************************************************
		 * FIRST TRIP: To get a ActionURL string created by
		 *             RenderResponse.createActionURL().
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
		 * processAction()
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
	 * @testName: SetEventLocalPartUseXmlDefaultTest
	 * @assertion_ids: PORTLET:SPEC:138
	 * @test_Strategy: In the first call, the render method generate
	 *                 an actionURL and write it to the output stream.
	 *                 The url is extracted and used for a second request.
	 *                 In the processAction method is triggert an
	 *                 event only by the local part.
	 *                 In the processEvent method is checked that
	 *                 the event is received.
	 * @assertion: If no such element is provided in the portlet deployment
	 *             descriptor the XML default namespace
	 *             XMLConstants.NULL_NS_URI must be assumed.
	 */
	public void SetEventLocalPartUseXmlDefaultTest() throws Fault {
		TSPortletInfo portletInfo = new TSPortletInfo(
										getDefaultPortletApp(),
										"SetEventLocalPartUseXmlDefaultTestPortlet");

		/*****************************************************************
		 * FIRST TRIP: To get a ActionURL string created by
		 *             RenderResponse.createActionURL().
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
		 * processAction()
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
	 * @testName: CheckEventNameInDDTest
	 * @assertion_ids: PORTLET:SPEC:141
	 * @test_Strategy: In the first call, the render method generate
	 *                 an actionURL and write it to the output stream.
	 *                 The url is extracted and used for a second request.
	 *                 In the processAction method is triggert an
	 *                 event.
	 *                 In the processEvent method is checked that
	 *                 the event is received and the event name is the same
	 *                 name as in the deployment descriptor.
	 * @assertion: The portlet container must use the event name entry in
	 *             the portlet deployment descriptor as event name when
	 *             submitting an event to the portlet.
	 */
	public void CheckEventNameInDDTest() throws Fault {
		TSPortletInfo portletInfo = new TSPortletInfo(
										getDefaultPortletApp(),
										"CheckEventNameInDDTestPortlet");

		/*****************************************************************
		 * FIRST TRIP: To get a ActionURL string created by
		 *             RenderResponse.createActionURL().
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
		 * processAction()
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
	 * @testName: CheckProcessOneEventTest
	 * @assertion_ids: PORTLET:SPEC:144
	 * @test_Strategy: In the first call, the render method generate
	 *                 an actionURL and write it to the output stream.
	 *                 The url is extracted and used for a second request.
	 *                 In the processAction method are triggert few events.
	 *                 In the processEvent method is checked that
	 *                 at any time only one event is processed.
	 * @assertion: Event distribution must be serialized for a specific
	 *             portlet window per client request so that at any given
	 *             time a portlet window is only processing one event in the
	 *             processEvent method for the current client request.
	 */
	public void CheckProcessOneEventTest() throws Fault {
		TSPortletInfo portletInfo = new TSPortletInfo(
										getDefaultPortletApp(),
										"CheckProcessOneEventTestPortlet");

		/*****************************************************************
		 * FIRST TRIP: To get a ActionURL string created by
		 *             RenderResponse.createActionURL().
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
		 * processAction()
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
	 * @testName: CheckFinishedBeforRenderTest
	 * @assertion_ids: PORTLET:SPEC:145
	 * @test_Strategy: In the first call, the render method generate
	 *                 an actionURL and write it to the output stream.
	 *                 The url is extracted and used for a second request.
	 *                 In the processAction method is triggert an event.
	 *                 In the processEvent method the thread sleep
	 *                 some time and then the result is stored in an
	 *                 attribute. In render is checked that the attribute
	 *                 is available.
	 * @assertion: Portlet event processing may appear after the processing
	 *             of the action and must be finished before the render phase.
	 */
	public void CheckFinishedBeforRenderTest() throws Fault {
		TSPortletInfo portletInfo = new TSPortletInfo(
										getDefaultPortletApp(),
										"CheckFinishedBeforRenderTestPortlet");

		/*****************************************************************
		 * FIRST TRIP: To get a ActionURL string created by
		 *             RenderResponse.createActionURL().
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
		 * processAction()
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
	 * @testName: CheckDestroyTest
	 * @assertion_ids: PORTLET:SPEC:147
	 * @test_Strategy: In the first call, the render method generate
	 *                 an actionURL and write it to the output stream.
	 *                 The url is extracted and used for a second request.
	 *                 In the processAction method is triggert an event.
	 *                 In the processEvent method is thrown an
	 *                 UnavailableException Exception.
	 *                 In the render phase is checked that the
	 *                 destroy method is called or the portlet
	 *                 is currently unavailable.
	 * @assertion: If a permanent unavailability is indicated by the
	 *             UnavailableException, the portlet container must remove
	 *             the portlet from service immediately, call the portlet's
	 *             destroy method, and release the portlet object.
	 */
	public void CheckDestroyTest () throws Fault {
		TSPortletInfo portletInfo = new TSPortletInfo(
				getDefaultPortletApp(),
				"CheckDestroyTestPortlet");

		/*****************************************************************
		 * FIRST TRIP: To get a ActionURL string created by
		 *             RenderResponse.createActionURL().
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
		 * processAction()
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
		setCriteriaProperty(UNEXPECTED_RESPONSE_MATCH,
				ResultWriter.getFailedString(getTestName()));

		/*
		 * Invokes the test.
		 */
		invoke(firstResponse);
	}
}
