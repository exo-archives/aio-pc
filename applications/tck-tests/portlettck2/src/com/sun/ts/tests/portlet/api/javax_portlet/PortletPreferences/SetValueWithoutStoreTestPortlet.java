/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.ts.tests.portlet.api.javax_portlet.PortletPreferences;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletSession;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletException;
import javax.portlet.ValidatorException;
import javax.portlet.ReadOnlyException;
import java.io.IOException;
import java.io.PrintWriter;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

/**
 *  This class will test the setValue() method without invoking the store()
 *  method.
 */

public class SetValueWithoutStoreTestPortlet extends GenericPortlet {

    public static String TEST_NAME="SetValueWithoutStoreTest";
    private static String failMessage = "";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        RequestCount reqCount = new RequestCount(request, response,
                                    RequestCount.MANAGED_VIA_SESSION);

		PortletPreferences preferences = request.getPreferences();
            
        if (reqCount.isFirstRequest()) {
            // First request will just invoke the set without the store
            PortletURLTag urlTag = new PortletURLTag();
            urlTag.setTagContent(response.createActionURL().toString());        
            out.println(urlTag.toString());
        	
        	
            
        } else {
            // Second request should return the original preferrence value
        	if (!failMessage.equals("")){
        		//setValue in processAction failed.
        		resultWriter.setStatus(ResultWriter.FAIL);
        		resultWriter.addDetail(failMessage);
        	}
        	else if (preferences != null) {
                String preferredLanguage = 
                            preferences.getValue("preferredLanguage", "Java");
                if (preferredLanguage != null) {
                    if (preferredLanguage.equals("Java")) {
			           resultWriter.setStatus(ResultWriter.PASS);
                    } else {
			          resultWriter.setStatus(ResultWriter.FAIL);
			          resultWriter.addDetail("Expected PreferredLanguage:Java");
			          resultWriter.addDetail("Actual PreferredLanguage : " 
                                            + preferredLanguage);
                    }
                } else {
		            resultWriter.setStatus(ResultWriter.FAIL);
		            resultWriter.addDetail("The string preferredLanguage "
                                         + "returned null");
                }
            } else {
		        resultWriter.setStatus(ResultWriter.FAIL);
		        resultWriter.addDetail("The method request.getPreferences() "
                                     + "returned a null value");
            }
        }
        out.println(resultWriter.toString());
    }

	@Override
	public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {
		PortletPreferences preferences = request.getPreferences();
		if (preferences != null) {
            String preferredLanguage = 
                        preferences.getValue("preferredLanguage", "Java");
            if (preferredLanguage != null) {
                if (preferredLanguage.equals("Java")) {
                    try {
                        preferences.setValue("preferredLanguage", "Oracle");
                    } catch (javax.portlet.ReadOnlyException e) {
                    	failMessage = TEST_NAME + "throws javax.portlet.ReadOnlyException while preferences.setValue";
                    }
                }
            }
        }
	}
    
}
