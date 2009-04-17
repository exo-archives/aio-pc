/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.ts.tests.portlet.api.javax_portlet.PortletMode;

import com.sun.javatest.Status;
import com.sun.ts.tests.portlet.common.client.TSPortletInfo;
import com.sun.ts.tests.portlet.common.client.BasePortletUrlClient;
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
public class URLClient extends BasePortletUrlClient {
    public static void main( String[] args ) {
        URLClient theTests = new URLClient();
        Status s = theTests.run( args, System.out, System.err );
        s.exit();
    }

	/**
	* Return the name of the default portlet app
	*/
	public String getDefaultPortletApp() {
		return "portlet_jp_PortletMode_web";
	}


    /*
     * @class.setup_props: ts_home;
     */                    

    /*
     *   @testName:  EqualsTest
     *   @assertion_ids: PORTLET:JAVADOC:137;
	 *   @test_Strategy: Compare the values of the newly created portlet mode 
	 *					object with the corresponding static value. Test 
     *                  passes if the values are the same.
	 *	 @assertion: Compares the specified object with this portlet mode for 
     *              equality. Returns true if the Strings equals method for 
     *              the String representing the two portlet modes returns 
     *              true.
     *
     */

    public void EqualsTest() throws Fault {
        /*
         * Sets the GET request to be sent out to the server.
         */
        TSPortletInfo portletInfo = new TSPortletInfo(getDefaultPortletApp(),
                                                      "EqualsTestPortlet");

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
     *   @testName:  HashCodeTest
     *   @assertion_ids: PORTLET:JAVADOC:138;
	 *   @test_Strategy: Compare the hash code values of newly created portlet
	 *					 mode object with the corresponding static value. Test
     *                   passes if the values are the same.
	 *	 @assertion: Returns the hash code value for this portlet mode. The 
     *              hash code is constructed by producing the hash value of 
     *              the String value of this mode. 
     *
     */

    public void HashCodeTest() throws Fault {
        /*
         * Sets the GET request to be sent out to the server.
         */
        TSPortletInfo portletInfo = new TSPortletInfo(getDefaultPortletApp(), 
                                                      "HashCodeTestPortlet");

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
     *   @testName:  ToStringTest
     *   @assertion_ids: PORTLET:JAVADOC:140;
	 *   @test_Strategy: Compare the string value of newly created portlet 
     *                  mode object with the corresponding static value. Test 
     *                  passes if the values are the same.
	 *	 @assertion: Returns a String representation of this portlet mode.
     *              Portlet mode names are always upper case names.
     *
     */

    public void ToStringTest() throws Fault {
        /*
         * Sets the GET request to be sent out to the server.
         */
        TSPortletInfo portletInfo = new TSPortletInfo(getDefaultPortletApp(),
                                                      "ToStringTestPortlet");

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
     *   @testName: CheckPortletModeEditTest
     *   @assertion_ids: PORTLET:JAVADOC:139;
	 *   @test_Strategy: Create a portlet object with EDIT porlet mode. Test 
     *                  passes if the the object created equals 
     *                  PortletMode.EDIT
	 *	 @assertion: Creates a new portlet mode with the given name. Lower 
     *              case letters in the name are converted to upper case 
     *              letters.
     *
     */

    public void CheckPortletModeEditTest() throws Fault {
        /*
         * Sets the GET request to be sent out to the server.
         */
        TSPortletInfo portletInfo = new TSPortletInfo(
                                       getDefaultPortletApp(),
                                       "CheckPortletModeEditTestPortlet");

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
     *   @testName: CheckPortletModeViewTest
     *   @assertion_ids:PORTLET:JAVADOC:139; 
	 *   @test_Strategy: Create a portlet object with VIEW porlet mode. Test 
     *                  passes if the the object created equals 
     *                  PortletMode.VIEW
	 *	 @assertion: Creates a new portlet mode with the given name. Lower case
     *              letters in the name are converted to upper case letters
     *
     */

    public void CheckPortletModeViewTest() throws Fault {
        /*
         * Sets the GET request to be sent out to the server.
         */
        TSPortletInfo portletInfo = new TSPortletInfo(
                                       getDefaultPortletApp(),
                                       "CheckPortletModeViewTestPortlet");

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
     *   @testName: CheckPortletModeHelpTest
     *   @assertion_ids:PORTLET:JAVADOC:139; 
	 *   @test_Strategy: Create a portlet object with HELP porlet mode. Test 
     *                  passes if the the object created equals 
     *                  PortletMode.HELP
	 *	 @assertion: Creates a new portlet mode with the given name. Lower case
     *              letters in the name are converted to upper case letters
     *
     */

    public void CheckPortletModeHelpTest() throws Fault {
        /*
         * Sets the GET request to be sent out to the server.
         */
        TSPortletInfo portletInfo = new TSPortletInfo(
                                       getDefaultPortletApp(),
                                       "CheckPortletModeHelpTestPortlet");

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
