/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.ts.tests.portlet.api.javax_portlet.PortletPreferences;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletURL;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletSession;
import javax.portlet.PortletException;
import javax.portlet.ValidatorException;
import java.io.IOException;
import java.io.PrintWriter;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;

/**
 *  This class will test the store() method when a Validator class is 
 *  associated with the portlet preference entity. The Validator class
 *  throws a ValidatorException() which is caught by this portlet.
 */

public class StoreWithValidatorExceptionTestPortlet extends GenericPortlet {

    public static String TEST_NAME="StoreWithValidatorExceptionTest";

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
            out.println(request.getPortletSession(true).getAttribute("resultStoreWithValidatorExceptionTest",PortletSession.PORTLET_SCOPE));
         }
    }

    public void processAction(ActionRequest request, ActionResponse response ) throws PortletException, IOException {

		ResultWriter resultWriter = new ResultWriter(TEST_NAME);

		PortletPreferences preferences = request.getPreferences();

        try {
            // Set the value to XML
            preferences.setValue("preferredLanguage", "XML");

            preferences.store();

			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("The test should have thrown a "
                                 + "ValidatorException()."); 
            resultWriter.addDetail("Since validator implemenation class "
                                 + "is always throwing exception.");
        } catch (ValidatorException v) {
			resultWriter.setStatus(ResultWriter.PASS);
        }
        request.getPortletSession(true).setAttribute("resultStoreWithValidatorExceptionTest", resultWriter.toString(), PortletSession.PORTLET_SCOPE);
    }

    protected String getPortletURL(RenderResponse response ) throws PortletException {
        PortletURL portletURL = response.createActionURL();
        return portletURL.toString();
     }
}
