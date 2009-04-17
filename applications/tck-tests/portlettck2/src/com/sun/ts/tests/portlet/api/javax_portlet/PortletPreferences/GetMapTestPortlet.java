/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.ts.tests.portlet.api.javax_portlet.PortletPreferences;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.HashMap;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.MapCompare;

/**
 *  This class will test the getMap() method.
 */

public class GetMapTestPortlet extends GenericPortlet {

    public static String TEST_NAME="GetMapTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);

		Map expectedMap = new HashMap(3);

		//populate the expected values in the Map
		expectedMap.put("preferredSystem", new String[] {"UltraSparc"});
		expectedMap.put("preferredOS", new String[] {"Solaris"});
		expectedMap.put("preferredLanguage", new String[] {"Java"});

		PortletPreferences preferences = request.getPreferences();

		if (preferences != null) {
            Map actualMap = preferences.getMap();
            MapCompare mapCompare = new MapCompare(expectedMap,
                                              	   actualMap);
            if (!mapCompare.misMatch()) {
                resultWriter.setStatus(ResultWriter.PASS);
            } else {
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail("PortletPreference.getMap() did not"
                                     + "return expected results");
                resultWriter.addDetail(mapCompare.getMisMatchReason());
            }
		} else {
			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("The method request.getPreferences() "
                                 + "returned a null value");
		}
		out.println(resultWriter.toString());
    }
}
