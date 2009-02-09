/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.common.client;



import java.net.URL;
import java.net.MalformedURLException;
import java.util.Properties;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.io.IOException;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.portlet.common.client.TSPortletInfo;
import com.sun.ts.tests.common.webclient.http.HttpRequest;
import com.sun.ts.tests.common.webclient.http.HttpResponse;
import com.sun.ts.tests.common.webclient.WebTestCase;
import com.sun.ts.tests.common.webclient.TestFailureException;
import com.sun.ts.tests.portlet.common.client.portalURLFetcher.IPortalURLFetcher;
import com.sun.ts.tests.portlet.common.client.portalURLFetcher.PortalURLFetcherFactory;
import com.sun.ts.tests.portlet.common.client.portalURLFetcher.PortletTCKTestCases;

import com.sun.javatest.Status;

import org.apache.commons.httpclient.HttpState;



/** 
 * <p>
 * Base class for all Portlet test clients, that sends Http request
 * to portlet containers where TCK test portlets are deployed for
 * compliance testing.This class contains the common implementation of the
 * lifecycle public methods -- called by the test harness.
 * It provides base test initialization and runtime
 * logic into a common class.
 *
 *
 * Lifecycle Method::First method called is setup(), defined in this class.
 *
 * Lifecycle Method::Then the harness calls the test methods using reflection, 
 * defined in the subclasses(URLClient, SpecURLClient) based on 
 * the javadoc tag testName.
 * This class defines the common utilities used by these actual test methods 
 * in derived classes. The main utility methods are::
 *
 *      getPortalURL      :: Used by test methods to obtain the first URL to 
 *                           a portal page that invokes the portlets
 *                           participating in the test. See the
 *                           Technology Compatibility Kit Requirements 
 *                           chapter of the Portlet Specification for details.
 *          
 *      setRequestProperty:: Used by test methods to set http request 
 *                           info like URL, headers etc.
 *
 *      setCritriaProperty:: Used by test methods to set the test success 
 *                           criteria e.g. either expected or unexpected 
 *                           substrings in the portal page returned to 
 *                           decide whether a test has passed or failed.
 *
 *      invoke            :: Used by test methods to run the test, by 
 *                           creating an http request using
 *                           properties set in setRequestProperty and 
 *                           validating against the criteria set by 
 *                           setCriteriaProperty.In case of failure, 
 *                           this method throws a <code>Fault</code> exception
 *                           that is caught by the framework to report a 
 *                           failure for the test.
 *
 * Lifecycle Method:: In the end, harness calls cleanup() method, 
 * defined in this class.
 */

 public abstract class BasePortletUrlClient extends EETest {


    //*******************************************************************
    // Http Status code  and related constants
    //*******************************************************************

    /** 404 - not found
    */    
    protected static final String NOT_FOUND             = "404";
    
    /** 200 - ok
     */    
    protected static final String OK                    = "200";
    
    /** 201 - created
     */    
    protected static final String CREATED               = "201";
    
    /** 500 - internal server error
     */    
    protected static final String INTERNAL_SERVER_ERROR = "500";
    
    /** 503 - service unavailable
     */    
    protected static final String SERVICE_UNAVAILABLE   = "503";
    
    /** 100 - continue
     */    
    protected static final String CONTINUE              = "100";
    
    /** 302 - moved temporarily
     */    
    protected static final String MOVED_TEMPORARY       = "302";

    /** 410 - GONE
     */
    protected static final String GONE                  = "410";
    
    /** Default request method
     */
    protected static final String GET = "GET";
    
    /** HTTP 1.0 
     */
    protected static final String HTTP10 = " HTTP/1.0";
    
    /** HTTP 1.1
     */
    protected static final String HTTP11 = " HTTP/1.1";
    
    //*******************************************************************
    // Properties that can be set in the test to define the pass/fail 
    // criteria for the test 
    //*****************************************************************

    /** StatusCode property
     */    
    protected static final String STATUS_CODE          = "status-code";
    
    /** Reason-Phrase property
     */    
    protected static final String REASON_PHRASE        = "reason-phrase";
    
    /** Expected headers property
     */    
    protected static final String EXPECTED_HEADERS     = "expected_headers";
    
    /** Unexpected header property
     */    
    protected static final String UNEXPECTED_HEADERS   = "unexpected_headers";
    
    /** Expect response body property
     */    
    protected static final String EXPECT_RESPONSE_BODY = "expect_response_body";

    /** Search string property
     */    
    protected static final String SEARCH_STRING        = "search_string";

    /** Exact match property
     */    
    protected static final String EXACT_MATCH          = "exact_match";
    
    /** Byte checking property
     */    
    protected static final String BYTECHECK       = "bytecheck";

    /** Unexpected response match property
     */
    protected static final String UNEXPECTED_RESPONSE_MATCH = "unexpected_response_match";

    /** Test strategy used for portlet tcks
     */
    protected static final String PORTLET_TEST_VALIDATION_STRATEGY_CLASS =
            "com.sun.ts.tests.portlet.common.client.PortletWebValidator";
    
    //*******************************************************************
    // Properties that can be set in the test to consturct a request
    //*****************************************************************

    /** Request property
     */    
    protected static final String REQUEST              = "request";

    /** Request Method property
     */    
    protected static final String REQUEST_METHOD        = "request_method";
    
    /** Request headers property
     */    
    protected static final String REQUEST_HEADERS      = "request_headers";
    
    /** Content property for content used for request
     */    
    protected static final String CONTENT              = "content";
    
    //*******************************************************************
    // Properties set by the javadoc tags for each method.
    //******************************************************************

    /** Test name property
     */    
    protected static final String TEST_NAME            = "testname";
    
    /** TS home property
     */    
    protected static final String TSHOME         = "ts_home";

    
    /** Current test name
     */    
    protected String _testName = null;
    
    /** Properties set by the test to create a http request 
     */    
    protected Properties _requestProperties       = new Properties();

    /** Properties set by the test to define pass/fail criteria
     */    
    protected Properties _criteriaProperties       = new Properties();


    /**
     * Lot of tests span through multiple requests, it is
     * helpful to print out what request number we are on.
     */

    int requestNumber = 0;
    
    

/* 
 * ========================================================================
 * Common implementation of lifecycle public methods -- 
 * called by the test harness.
 * First method called by the harness is setup(), 
 * then the test methods using reflection defined in the
 * subclasses based on the javadoc tag testName.
 * In the end, harness calls cleanup() method.
 * ========================================================================
 */     
    /**
     * <code>setup</code> is called by the test harness to initialize
     * the tests.
     * 
     * @param args a <code>String[]</code> value
     * @param p a <code>Properties</code> value
     * @exception Fault if an error occurs
     * @exception com.sun.ts.lib.harness.EETest.Fault
     */
    public void setup(String[] args, Properties p) throws Fault {
        boolean pass = true;

        _testName = p.getProperty("testName");
        TestUtil.logMsg("[BasePortletUrlClient] Test setup OK");

    }

    /**
     * <code>cleanup</code> is called by the test harness
     * to cleanup after text execution
     *
     * @exception Fault if an error occurs
     */
    public void cleanup() throws Fault {
        TestUtil.logMsg("[BasePortletUrlClient] Test cleanup OK");
    }

    

/**
 * Returns properties set by the test methods specifying 
 * request information like URL, headers, content etc.
 * 
 * @return Returns request properties
 */
/*
* ========================================================================
* These helper methods are called within the test methods to code a test 
* ========================================================================
*/
    
    public Properties getRequestProperties() {
        return _requestProperties;
    }

    /**
     * Returns properties set by the test methods specifying 
     * the success/failure criteria for the test.
     * 
     * @return Returns success/failure properties
     */
    public Properties getCriteriaProperties() {
        return _criteriaProperties;
    }


    /**
     * get name of the current test
     */
    public String getTestName() {
       return _testName;
    }

    /**
     * get unique name of the test in this whole test harness
     */
    public String getUniqueTestName() {
        String className = this.getClass().getName();

        String urlClientJavaName = className.replace('.', '/');

        return  urlClientJavaName + ".java" + "#" + _testName;
    }


    /**
     * Used by the test method to specify a success/failure criteria.
     * 
     * @param name Name of the property
     * @param value value of the property
     */
    protected void setCriteriaProperty(String name, String value) {
        _criteriaProperties.setProperty(name, value);
    }

    /**
     * Used by the test methods to specify the request properties 
     * like url, headers, contents
     * 
     * @param name Name of the property
     * @param value Value of the property
     */
    protected void setRequestProperty(String name, String value) {
        _requestProperties.setProperty(name, value);
    }

    /** 
     * Invokes a test based on the request and test criteria properties
     *
     * @throws Fault If an error occurs during the test run
     */    

    protected HttpResponse invoke() throws Fault {
        return invoke(null);
    }

    /**
     * Invokes a test based on the request, test criteria properties
     * If previousResponse argument is not null, the method uses the
     * state from it to set cookies etc.
     * Also it consults the host, port information from the 
     * previous request to resolve relative or absolute request 
     * urls for the current request.
     * @throws Fault If an error occurs during the test run
     * 
     * @param previousResponse Response from the previous request to the portlet container.
     * @exception com.sun.ts.lib.harness.EETest.Fault
     */    
    protected HttpResponse invoke(HttpResponse previousResponse) 
                                                    throws Fault {
        try {

            /*
             * record request number in the DetailBuffer that is 
             * storing info to be printed out in case of error
             */
             DetailBuffer.recordRequestNumber(++requestNumber);

            /*
             * Create the request object using proerties set and state 
             * in previous response if any
             */

            HttpRequest req = createHttpRequest(getRequestProperties(), 
                                                    previousResponse);



            /*
             * Create the test case using the test criteria properties set 
             * for the test and HttpRequest object
             */
            WebTestCase testCase = createWebTestCase(getCriteriaProperties(), 
                                                        req);

            /*
             * Execute the test
             */

            testCase.execute();

            /*
             * Capture the relevant strings from HttpResponse
             * in the DetailBuffer that is 
             * storing info to be printed out in case of error
             */
             DetailBuffer.recordResponseInfo(testCase.getResponse());


            /*
             * Return the response
             */

            return testCase.getResponse();

        } catch (TestFailureException tfe) {
            throw new Fault("[BasePortletUrlClient] " + _testName + " failed! " + 
                                    tfe.getMessage(), tfe);
        }
        catch(IOException ex) {
            throw new Fault("[BasePortletUrlClient] " + _testName + " failed! " + 
                                    ex.getMessage(), ex);
        }
        finally {
            clearTestProperties();
        }
    }

    

    /**
     * Used by test methods to obtain the first URL to 
     * a portal page that invokes the portlet specified as
     * the parameter participating in the current test.
     * 
     * @param portletInfo The information about portlet participating in the current test.
     * @return A URL string
     * @exception com.sun.ts.lib.harness.EETest.Fault
     */
    protected String getPortalURL(TSPortletInfo portletInfo) throws Fault{

        TSPortletInfo[] portletInfos = new TSPortletInfo[1];
        portletInfos[0] = portletInfo;

        return getPortalURL(portletInfos);

    }

    /**
     * Used by test methods to obtain the first URL to 
     * a portal page that invokes the portlets specified as
     * the parameter participating in the current test.
     * 
     * @param portletInfos List of portlets participating in the test
     * @exception com.sun.ts.lib.harness.EETest.Fault
     */
    protected String getPortalURL(TSPortletInfo[] portletInfos) throws Fault{

        /*
         * Get the URL for the test using configured PortalURLFetcher class 
         */

        IPortalURLFetcher urlFetcher = 
                            PortalURLFetcherFactory.getPortalURLFetcher();

        String uniqueTestName = getUniqueTestName(); 

        String url = urlFetcher.getPortalURL(
                            uniqueTestName, portletInfos);


        // No-op in production, else hook to generate or check the
        // consistency of xml file.
        PortletTCKTestCases.xmlFileHouseKeeping(getUniqueTestName(),
                                                portletInfos);

        return  url;

    }



    public String getPortalReturnURL( String portletURL) throws Fault{
        return portletURL;
    }



    /**
     * Abstract method that must be implemented 
     * by all derived classes.
     * 
     * @return name of the Portlet Application, to which most of the portlets
     * used by tests defined in this class, belong to.
     */
    public abstract String getDefaultPortletApp(); 

/*
* ========================================================================
* Private methods
* ========================================================================
*/

    /**
     * Create an instance of WebTestCase using the test criteria properties 
     * and the request.( WebTestCase class is what actually makes the http
     * request call, checks the response for pass/fail criteria .)
     * 
     * @param testCaseProperties Criteria properties
     * @param request HttpRequest object containing all the request information.
     */

    private WebTestCase createWebTestCase(Properties testCaseProperties, 
                                    HttpRequest request){



        WebTestCase testCase = new WebTestCase();

        /*
         * Set portlet strategy class
         * 
         */
        testCase.setStrategy(PORTLET_TEST_VALIDATION_STRATEGY_CLASS);


        /*
         * Set the http request for the test case
         */

        testCase.setRequest(request);
        

        /*
         * Set the criterias for test 
         */
        String key = null;
        for (Enumeration e = testCaseProperties.propertyNames(); 
                                                e.hasMoreElements();) {
            key = (String) e.nextElement();
            if (key.equals(STATUS_CODE)) {
                testCase.setExpectedStatusCode(
                            testCaseProperties.getProperty(key));
            } else if (key.equals(REASON_PHRASE)) {
                testCase.setExpectedReasonPhrase(
                            testCaseProperties.getProperty(key));
            } else if (key.equals(EXPECTED_HEADERS)) {
                testCase.addExpectedHeader(
                            testCaseProperties.getProperty(key));
            } else if (key.equals(UNEXPECTED_HEADERS)) {
                testCase.addUnexpectedHeader(
                            testCaseProperties.getProperty(key));
            } else if (key.equals(SEARCH_STRING)) {
                testCase.setResponseSearchString(
                            testCaseProperties.getProperty(key));
            } else if (key.equals(UNEXPECTED_RESPONSE_MATCH)) {
                testCase.setUnexpectedResponseSearchString(
                            testCaseProperties.getProperty(key));
            }
        }

        return testCase;
    }
  
  
    
    /**
     * Create an instance of HttpRequest using the request related properties 
     * and any state from previous response. Previous response is also used 
     * to create the new request url to resolve the relative urls.
     * 
     * @param requestProperties Request properties like url, headers
     * @param previousResponse HttpResponse object from the previous request.
     * @exception com.sun.ts.tests.common.webclient.TestFailureException
     */
    private HttpRequest createHttpRequest(Properties requestProperties, 
                HttpResponse previousResponse) throws TestFailureException{
        try {

            HttpState previousState = null;
            URL previousURL = null;

            /*
             *  If state is there, first get the URL used to process 
             *  previous request
             */
            if ( previousResponse != null) {
                previousState = previousResponse.getState();
                previousURL = new URL(
                    previousResponse.getProtocol(),
                    previousResponse.getHost(),
                    previousResponse.getPort() == -1 ? 
                                80: previousResponse.getPort(),
                    previousResponse.getPath());
                //TestUtil.logTrace("previousURL:" + previousURL);
            }

            /*
             * Create the new resolved url 
             */
            String requestStr = requestProperties.getProperty(REQUEST);
            URL currentURL = new URL(previousURL, requestStr);


            /*
             * Find out if it is supposed to be a GET ( default or POST)
             */

            String httpRequestMethod = 
                    requestProperties.getProperty(REQUEST_METHOD);
            if ( httpRequestMethod == null) {
                httpRequestMethod = GET;
            }
            

            /*
             * Create instance of HttpRequest using components of currentURL
             */
            String currentHost = currentURL.getHost();
            int currentPort = 
                currentURL.getPort() == -1 ? 80: currentURL.getPort();
            String currentURI = 
                httpRequestMethod + " " + currentURL.getFile() + HTTP10;

            HttpRequest request = new HttpRequest(currentURI, 
                                                currentHost, 
                                                currentPort);
            if (previousState != null) {
                request.setState(previousState);
            }


            /*
             * Capture the relevant info from HttpRequest
             * in the DetailBuffer that is 
             * storing info to be printed out in case of error
             */
            DetailBuffer.recordRequestInfo(
                    currentHost, currentPort, currentURI, currentURL);


            /*
             *
             *  
             */

            /*
            List vendorHeaders = HeaderList.readHeaderList();
            Iterator headersIter = vendorHeaders.iterator();
            while ( headersIter.hasNext()) {
                String header = (String)headersIter.next();
                request.addRequestHeader(header);
            }
            */

            String key = null;
            Properties vendorHeaders = HeaderList.readHeaderProperties();
            for (Enumeration e = vendorHeaders.propertyNames(); 
                                        e.hasMoreElements();) {
               key = (String) e.nextElement();

               request.addRequestHeader(
                        key, vendorHeaders.getProperty(key));
            }


            /*
             * Set properties for the request object
             */

            for (Enumeration e = _requestProperties.propertyNames(); 
                                        e.hasMoreElements();) {
                key = (String) e.nextElement();
                if (key.equals(CONTENT)) {
                    request.setContent(
                           requestProperties.getProperty(key));
                }
                else if (key.equals(REQUEST_HEADERS)) {
                   request.addRequestHeader(
                            requestProperties.getProperty(key));
                }
            }
            request.setFollowRedirects(true);


            /*
             * Find out if there are any authentication 
             * needs for this test. If there are
             * add header or cookie based on the 
             * configuration in ts.jte
             */

             AuthenticationUtil.setupAuth(request, 
                                        currentHost,
                                        currentPort,
                                        getUniqueTestName(), 
                                        isFirstRequestBeingProcessed());


            return request;
        }
        catch ( MalformedURLException ex) {
            ex.printStackTrace();
            throw new TestFailureException(ex.getMessage());
        }
    }


    /**
     * Clears the contents of TEST_PROPS
     * Not needed. TODO
     */    
    private void clearTestProperties() {
        _requestProperties.clear();
        _criteriaProperties.clear();
    }

    
    /**
     * overwritten method from base class, that dumps all the information
     * in case of failure. 
     */
    protected void setTestStatus (Status s, Throwable t) {

        if ((t != null) && (t instanceof Fault)) {
            TestUtil.logErr(DetailBuffer.toStr());
        }

        super.setTestStatus(s, t);
    }

    /**
     * Returns true if the first request is being sent
     * for this session. 
     * It is used by logic that might need
     * want to set few cookies like authentication etc.
     * to be set only on the first request.
     */

    private boolean isFirstRequestBeingProcessed() {
        if ( requestNumber == 1) {
            return true;
        }
        else {
            return false;
        }
    }

}
