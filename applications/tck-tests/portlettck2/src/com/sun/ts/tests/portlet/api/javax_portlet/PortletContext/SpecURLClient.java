/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletContext;

import com.sun.javatest.Status;
import com.sun.ts.tests.portlet.common.client.TSPortletInfo;
import com.sun.ts.tests.portlet.common.client.BasePortletUrlClient;
import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.client.tags.PortletTCKCustomClientTag;
import com.sun.ts.tests.portlet.common.client.tags.PortletURLClientTag;
import com.sun.ts.tests.common.webclient.http.HttpResponse;
import org.apache.commons.httpclient.HttpState;


import com.sun.ts.tests.common.webclient.http.HttpResponse;
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


    /*
    * Constructor called by the Test Harness
    */

    public static void main( String[] args ) {
        SpecURLClient theTests = new SpecURLClient();
        Status s = theTests.run( args, System.out, System.err );
        s.exit();
    }


    /*
     * @class.setup_props: ts_home;
     */                    


    /**
    * Return the name of the default portlet app
    */

    public String getDefaultPortletApp() {
        return "portlet_jp_PortletContext_web";
    }


    /* Run test */


    /*
     *   @testName: CompareInitParamsInPortletAndServletTest
     *   @assertion_ids: PORTLET:SPEC:61;
     *   @test_Strategy: Request issued to a portlet which will read the init
     *                   params through the portlet context. It will then put
     *                   the init params iterator as a session attribute and
     *                   will invoke the servlet through request dispatcher. 
     *                   The servlet in turn will read the init params through 
     *                   servlet context. Test passes if the init params list 
     *                   generated by the portlet context and the servlet 
     *                   context are the same.
     *   @assertion: The initialization parameters accessible through the 
     *               PortletContext must be same that are accessible through 
     *               the ServletContext of the portlet application.
     *
     */
    public void CompareInitParamsInPortletAndServletTest() throws Fault {

        /*
         * Sets the GET request to be sent out to the server.
         */
        TSPortletInfo portletInfo = new TSPortletInfo(getDefaultPortletApp(),
                        "CompareInitParamsInPortletAndServletTestPortlet");

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
     *   @testName: CompareContextAttrInPortletAndServletTest
     *   @assertion_ids: PORTLET:SPEC:62;
     *   @test_Strategy: Request issued to a portlet which will set an 
     *                   attribute in the portlet context. The portlet will
     *                   then invoke the servlet through request dispatcher. 
     *                   The servlet in turn will read the context attribute
     *                   set thru servlet context. Test passes if the attribute
     *                   is the same that was set in the portlet context.
     *   @assertion: Context attributes set using the PortletContext must be 
     *              stored in the ServletContext of the portlet application. A 
     *              direct consequence of this is that data stored in the 
     *              ServletContext by servlets or JSPs is accessible to 
     *              portlets through the PortletContext and vice versa.
     */

    public void CompareContextAttrInPortletAndServletTest() throws Fault {
        /*
         * Sets the GET request to be sent out to the server.
         */
        TSPortletInfo portletInfo = new TSPortletInfo(getDefaultPortletApp(),
                        "CompareContextAttrInPortletAndServletTestPortlet");

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
     * @testName: CompareContextAttrInServletAndPortletTest
     * @assertion_ids: PORTLET:SPEC:62;
     * @test_Strategy: Invokes PortletContext.getRequestDispatcher()
     *                 to get a PortletRequestDispatcher that includes
     *                 a servlet.  In the service() method of this
     *                 servlet, sets an attribute in the ServletContext.
     *                 Checks for this attribute in PortletContext When
     *                 PortletRequestDispatcher.include() returns.
     * @assertion: Context attributes set using the PortletContext
     *             must be stored in the ServletContext of the portlet
     *             application.  A direct consequence of this is that
     *             data stored in the ServletContext by servlets or
     *             JSPs is accessible to portlets through the
     *             PortletContext and vice versa.
     */
    public void CompareContextAttrInServletAndPortletTest() throws Fault {
        /*
         * Sets the GET request to be sent out to the server.
         */
        TSPortletInfo portletInfo = new TSPortletInfo(
                                        getDefaultPortletApp(),
                                        "CompareContextAttrInServletAndPortletTestPortlet");

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
     *   @testName: GetContextAttrForTempDirTest
     *   @assertion_ids: PORTLET:SPEC:64;
     *   @test_Strategy: Request issued to a portlet which will read the value
     *                   of the attribute "javax.servlet.context.tempdir". A
     *                   new file object is created with the value returned.
     *                   Test passes if the object created is an instance of
     *                   File object.
     *   @assertion: The PortletContext must handle the same temporary working 
     *              directory the ServletContext handles. It must be accessible 
     *              as a context attribute using the same constant defined in 
     *              the Servlet Specification 2.3 SVR 3 Servlet Context Chapter,
     *              javax.servlet.context.tempdir.
     */

    public void GetContextAttrForTempDirTest() throws Fault {
        /*
         * Sets the GET request to be sent out to the server.
         */
        TSPortletInfo portletInfo = new TSPortletInfo(getDefaultPortletApp(),
                                        "GetContextAttrForTempDirTestPortlet");

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
}
