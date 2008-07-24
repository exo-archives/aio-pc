package com.sun.ts.tests.portlet.common.client;

import com.sun.ts.tests.common.webclient.http.HttpResponse;
import com.sun.ts.tests.portlet.common.client.tags.PortletURLClientTag;
import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * TODO: docu
 * Extended class for Portlet test clients. 
 * Defines some often used test sequences.
 */
public abstract class ExtendedPortletUrlClient extends BasePortletUrlClient {


	/**
	 * Makes a single request to the portlet and 
	 * checks the result for the passed string.
	 * 
	 * @param portletInfo
	 * @throws Fault
	 */
	protected void runStdTestForRender(TSPortletInfo portletInfo) 
			throws Fault{
		
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
	
	/**
	 * Makes a first request to the portlet to get
	 * a render PortletURL (e.g. with parameters set). 
	 * Extracts the URL string and makes a second request 
	 * with it. Checks the result for the passed string.
	 * 
	 * @param portletInfo
	 * @throws Fault
	 */
	protected void runStdTestForRenderWithParameters(TSPortletInfo portletInfo) 
			throws Fault{
		
		/*****************************************************************
         * FIRST TRIP: To get the PortletURL string.
         *****************************************************************/
        
        String request = getPortalURL(portletInfo);
        setRequestProperty(REQUEST, request);

        /*
         * Sets the test criteria.
         */
        setCriteriaProperty(UNEXPECTED_RESPONSE_MATCH,
            ResultWriter.getFailedString(getTestName()));

        /*
         * Invokes the test.
         */
        HttpResponse firstResponse = invoke();
                
        /*
         * Extracts the PortletURL for the render() method from the
         * content of the HttpResponse of the first pass.
         */
        String renderUrl = PortletURLClientTag.extractContent(firstResponse);
        
        /*****************************************************************
         * SECOND TRIP: To send the second request to the render() method. 
         *****************************************************************/
                  
         /*
          * Sets the second request to be sent out to the server.
          */
        String secondRequest = getPortalReturnURL(renderUrl);
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
	
	/**
	 * Makes a first request to the portlet to get
	 * an action PortletURL. Extracts the URL string 
	 * and makes a second request with it. 
	 * Checks the result for the passed string.
	 * 
	 * @param portletInfo
	 * @throws Fault
	 */
	protected void runStdTestForProcessAction(TSPortletInfo portletInfo) 
			throws Fault{
		
		/*****************************************************************
         * FIRST TRIP: To get the PortletURL string.
         *****************************************************************/
        
        String request = getPortalURL(portletInfo);
        setRequestProperty(REQUEST, request);

        /*
         * Sets the test criteria.
         */
        setCriteriaProperty(UNEXPECTED_RESPONSE_MATCH,
            ResultWriter.getFailedString(getTestName()));

        /*
         * Invokes the test.
         */
        HttpResponse firstResponse = invoke();
                
        /*
         * Extracts the PortletURL for the processAction() method from the
         * content of the HttpResponse of the first pass.
         */
        String actionUrl = PortletURLClientTag.extractContent(firstResponse);
        
        /*****************************************************************
         * SECOND TRIP: To send the second request to the processAction() method. 
         *****************************************************************/
                  
         /*
          * Sets the second request to be sent out to the server.
          */
        String secondRequest = getPortalReturnURL(actionUrl);
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
	
	/**
	 * Makes a first request to the portlet to get
	 * an action PortletURL. Extracts the URL string 
	 * and makes a second request with it. 
	 * In this second request, processAction()
	 * will set an event.
	 * Checks the result for the passed string.
	 * 
	 * @param portletInfo
	 * @throws Fault
	 */
	protected void runStdTestForProcessEvent(TSPortletInfo portletInfo) 
			throws Fault{
		
		 /*****************************************************************
         * FIRST TRIP: To get the PortletURL string.
         *****************************************************************/
        
        String request = getPortalURL(portletInfo);
        setRequestProperty(REQUEST, request);

        /*
         * Sets the test criteria.
         */
        setCriteriaProperty(UNEXPECTED_RESPONSE_MATCH,
            ResultWriter.getFailedString(getTestName()));

        /*
         * Invokes the test.
         */
        HttpResponse firstResponse = invoke();
                
        /*
         * Extracts the PortletURL for the processAction() method from the
         * content of the HttpResponse of the first pass.
         */
        String actionUrl = PortletURLClientTag.extractContent(firstResponse);
        
        /*****************************************************************
         * SECOND TRIP: To send the second request to the 
         *              processAction() method which sets an event.
         *****************************************************************/
                  
         /*
          * Sets the second request to be sent out to the server.
          */
        String secondRequest = getPortalReturnURL(actionUrl);
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
	
	/**
	 * Makes a first request to the portlet to get
	 * an ResourceURL. Extracts the URL string 
	 * and makes a second request with it. 
	 * Checks the result for the passed string.
	 * 
	 * @param portletInfo
	 * @throws Fault
	 */
	protected void runStdTestForServeResource(TSPortletInfo portletInfo) 
			throws Fault{
		
		 /*****************************************************************
	     * FIRST TRIP: To get the ResourceURL string.
	     *****************************************************************/
	    
	    String request = getPortalURL(portletInfo);
	    setRequestProperty(REQUEST, request);
	
	    /*
	     * Sets the test criteria.
	     */
	    setCriteriaProperty(UNEXPECTED_RESPONSE_MATCH,
	        ResultWriter.getFailedString(getTestName()));
	
	    /*
	     * Invokes the test.
	     */
	    HttpResponse firstResponse = invoke();
	            
	    /*
	     * Extracts the ResourceURL for the serveResource() method from the
	     * content of the HttpResponse of the first pass.
	     */
	    String resourceUrl = PortletURLClientTag.extractContent(firstResponse);
	    
	    /*****************************************************************
	     * SECOND TRIP: To send the second request to the 
	     *              serveResource() method.
	     *****************************************************************/
	              
	     /*
	      * Sets the second request to be sent out to the server.
	      */
	    String secondRequest = getPortalReturnURL(resourceUrl);
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
