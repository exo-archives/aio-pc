package com.sun.ts.tests.portlet.Signature;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.common.webclient.http.HttpResponse;
import com.sun.ts.tests.portlet.api.javax_portlet.RenderRequest.SpecURLClient;
import com.sun.ts.tests.portlet.common.client.BasePortletUrlClient;
import com.sun.ts.tests.portlet.common.client.TSPortletInfo;
import com.sun.ts.tests.portlet.common.client.tags.PortletURLClientTag;
import com.sun.ts.tests.portlet.common.util.ResultWriter;

/*
 * This class is a simple example of a signature test.
 * The test runs inside.
 */

public class PORTLETSigTest extends BasePortletUrlClient {
	public static void main(String[] args) {
		PORTLETSigTest theTests = new PORTLETSigTest();
        Status status = theTests.run(args, System.out, System.err);
        status.exit();
    }

    /**
    * Returns the name of the default portlet app.
    */
    public String getDefaultPortletApp() {
        return "portlet_jp_sig_web";
    }

     
    /*
     * @class.setup_props: ts_home;
     */
    
    /*
     * @testName: SignatureTest
     * @test_Strategy: A PORTLET platform must implement the required classes and
     *                 and APIs specified in the PORTLET Specification.
     * @assertion: Using reflection, gather the implementation specific classes 
     *             and APIs.  Compare these results with the expected (required)
     *             classes and APIs.
     */
    public void SignatureTest() throws Fault {
    	
        TSPortletInfo portletInfo = new TSPortletInfo(getDefaultPortletApp(),
                          "SignatureTestPortlet");

        /*******************************************************************
         * FIRST TRIP:: 
         ******************************************************************/
         
         /*
          * Sets the GET request to be sent out to the server.
          */

        String firstRequest = getPortalURL(portletInfo);
        setRequestProperty(REQUEST, firstRequest);

        /*
         * Sets the test criteria.
         */
        setCriteriaProperty(SEARCH_STRING,
            ResultWriter.getPassedString(getTestName()));

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
         
        /*
         * Invokes the test.
         */

        HttpResponse secondResponse = invoke(firstResponse);  

        /*
         * Extract the PortletURL string from the content of 
         * HttpResponse of the second pass
         */
         String portletURLStr_second = 
                                PortletURLClientTag.extractContent(secondResponse);

         String thrirdRequest = getPortalReturnURL( portletURLStr_second);
        
         setRequestProperty(REQUEST, thrirdRequest);

        /*
         * Sets the test criteria.
         */
        setCriteriaProperty(SEARCH_STRING,
            ResultWriter.getPassedString(getTestName()));

        /*
         * Invokes the test.
         */

        invoke(secondResponse);  
    }
}
