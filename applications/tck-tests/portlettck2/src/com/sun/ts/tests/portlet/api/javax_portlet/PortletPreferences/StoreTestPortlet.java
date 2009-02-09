/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.ts.tests.portlet.api.javax_portlet.PortletPreferences;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletURL;
import javax.portlet.PortletSession;
import javax.portlet.PortletException;
import javax.portlet.ValidatorException;
import java.io.IOException;
import java.io.PrintWriter;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;


/**
 *  This class will test the store() method.
 */

public class StoreTestPortlet extends GenericPortlet {

    public static String TEST_NAME="StoreTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        RequestCount reqCount = new RequestCount(request, response,
                                         RequestCount.MANAGED_VIA_SESSION);

        if (reqCount.isFirstRequest()) {
            //write a portlet url to the outputstream
            PortletURLTag customTag = new PortletURLTag();
            customTag.setTagContent(getPortletURL(response));
            out.println(customTag.toString());
         } 
         else { 
            out.println(request.getPortletSession(true).getAttribute("resultStoreTest", PortletSession.PORTLET_SCOPE));
         }
    }

    public void processAction(ActionRequest request, ActionResponse response ) throws PortletException, IOException {
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        resultWriter.setStatus(ResultWriter.PASS);
        PortletPreferences preferences = request.getPreferences();

        runStoreTest(resultWriter, preferences);

        request.getPortletSession(true).setAttribute("resultStoreTest", 
                        resultWriter.toString(), PortletSession.PORTLET_SCOPE);
    }

    protected String getPortletURL(RenderResponse response ) throws PortletException {
        PortletURL portletURL = response.createActionURL();
        return portletURL.toString();
     }

    protected void runStoreTest(ResultWriter resultWriter, PortletPreferences preferences) throws IOException, ValidatorException {

        if (preferences != null) {

            // Get the default and check that default is obtained. If not, then
            // may be the previous clean up operation using reset was not 
            // successful.
            String preferredLanguage = 
                            preferences.getValue("preferredLanguage", "Java");

            if(!checkExpectedValue(resultWriter, preferredLanguage, "Java")) {
                if(preferredLanguage != null) {
                    resultWriter.setStatus(ResultWriter.FAIL);
                    resultWriter.addDetail("Previous clean up operation not "
                                         + "done properly. Manually reset the "
                                         + "preferences");
                }
                return;
            }

            // Start the test for store assertion
            try {
                // Set the value to XML
                preferences.setValue("preferredLanguage", "XML");

                // store it
                preferences.store();

                // read it again
                preferredLanguage = 
                            preferences.getValue("preferredLanguage", "Java");

                if (!checkExpectedValue(resultWriter, preferredLanguage, "XML"))
                {
                    resultWriter.setStatus(ResultWriter.FAIL);
                    resultWriter.addDetail("store() operation was supposed "
                                      + "to set the value of preferredLanguage "
                                      + " to XML.");
                    return;
                }

                // reset the preference value so that this test can be executed
                // again.
                preferences.reset("preferredLanguage");

                // Make the resetting persistent
                preferences.store();

                // Check that resetting was successful
                preferredLanguage = 
                            preferences.getValue("preferredLanguage", "Java");

                if (!checkExpectedValue(resultWriter,preferredLanguage,"Java"))
                {
                    resultWriter.setStatus(ResultWriter.FAIL);
                    resultWriter.addDetail("store() operation after reset "
                                         + "was supposed to set the value of "
                                         + "preferredLanguage to Java.");
                    return;
                }
            } catch (javax.portlet.ReadOnlyException e) {
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail("Unexpected ReadOnlyException() "  
                                     + "thrown for preference key "
                                     + "preferredLanguage");
                return;
            }
        } else {
           resultWriter.setStatus(ResultWriter.FAIL);
           resultWriter.addDetail("Request.getPreferences() returned null");
        }
        return;
    }

    private boolean checkExpectedValue(ResultWriter resultWriter, 
                                    String actual, 
                                    String expected) {

        boolean errorFlag = false;

        if(actual == null) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("Expected value for preferredLanguage:" 
                                   + expected);
            resultWriter.addDetail("Actual value for preferredLanguage "
                                 + "returned null");
        } else if (!actual.equals(expected)) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("Expected value for preferredLanguage:" 
                                    + expected);
            resultWriter.addDetail("Actual value for preferredLanguage:" 
                                    + actual);
        } 
        else {
            errorFlag = true;
        }
        return errorFlag;
    }
}
