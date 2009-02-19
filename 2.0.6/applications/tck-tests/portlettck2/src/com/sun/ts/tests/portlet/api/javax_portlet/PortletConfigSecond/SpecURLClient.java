/**
 * Copyright 2007 IBM Corporation.
 */
package com.sun.ts.tests.portlet.api.javax_portlet.PortletConfigSecond;

import com.sun.javatest.Status;
import com.sun.ts.tests.portlet.common.client.BasePortletUrlClient;
import com.sun.ts.tests.portlet.common.client.TSPortletInfo;
import com.sun.ts.tests.portlet.common.util.ResultWriter;

public class SpecURLClient extends BasePortletUrlClient{
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
        return "portlet_jp_PortletConfigSecond_web";
    }
    /*
     *   @testName:  GetDefaultXMLNamespaceTest
     *	 @assertion_ids: PORTLET:SPEC:28;
     *   @test_Strategy: Check that if the PortletConfig contains the right
     *   				 Default Event Namespace in the render-phase
     *   				 
     *   @assertion: The getDefaultNamespace method of the PortletConfig
     *   			 interface returns the default namespace for events
     *   			 and public render parameters set in the portlet
     *   			 deployment descriptor with the default-namespace
     *   			 element, or the XML default namespace
     *   			 XMLConstants.NULL_NS_URI if no default namespace
     *   			 is provided in the portlet deployment descriptor.
     */

    public void GetDefaultXMLNamespaceTest() throws Fault {
        /*
         * Sets the GET request to be sent out to the server.
         */
        TSPortletInfo portletInfo = new TSPortletInfo(
                                     getDefaultPortletApp(),
                                     "GetDefaultXMLNamespaceTestPortlet");

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
