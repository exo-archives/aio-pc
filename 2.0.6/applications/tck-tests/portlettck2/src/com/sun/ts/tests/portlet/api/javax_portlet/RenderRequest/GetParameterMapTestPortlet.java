/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.RenderRequest;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletURL;
import java.util.Iterator;
import java.util.Vector;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import com.sun.ts.tests.portlet.common.util.MapCompare;

/**
 * Test for PortletRequest.getParameterMap() method
 * First request to the portlet writes a
 * action portletURL with few paramaters in it, to the output stream.
 * The portlet URL string is extracted and used for second
 * request. In the second request, 
 * portlet uses PortletRequest.getParameterMap()
 * to see if the expected parameter is returned.
 */


import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;

public class GetParameterMapTestPortlet extends GenericPortlet {

    static public String TEST_NAME = "GetParameterMapTest";

    public void render(RenderRequest request, RenderResponse response ) throws PortletException, java.io.IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();


        RequestCount reqCount = new RequestCount(request, response,
                                        RequestCount.MANAGED_VIA_SESSION);

        if (reqCount.isFirstRequest()) {

            //write a portlet url to the outputstream
            PortletURLTag customTag = new PortletURLTag();
            customTag.setTagContent(getPortletURL(response));
            out.println(customTag.toString());
        } else {
            computeResults(request, response);
            out.println(request.getPortletSession(true).getAttribute("resultGetParameterMap"));
        }
    }

    protected String getPortletURL(RenderResponse response ) {

        PortletURL portletURL = response.createRenderURL();
        portletURL.setParameter("BestLanguage", 
                            new String[] {"Java", "XML" });
        portletURL.setParameter("BestJSP", "Java2");
        return portletURL.toString(); 
    }

    public void computeResults(RenderRequest request, RenderResponse response ) throws PortletException, java.io.IOException {

        ResultWriter resultWriter = new ResultWriter(TEST_NAME);

        Map map = request.getParameterMap();
        HashMap expectedMap = new HashMap();
        expectedMap.put("BestLanguage", new String[] {"Java", "XML"});
        expectedMap.put("BestJSP", new String[] {"Java2"});

        if(map != null ) {
        	
    		try {
    			map.put(TEST_NAME, new String[] {TEST_NAME});
    		} catch (Exception e) {}
    		
            MapCompare compare = new MapCompare(
                                expectedMap,
                                map);
            if (!compare.misMatch()) {
                resultWriter.setStatus(ResultWriter.PASS);
            }
            else {
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail(compare.getMisMatchReason());
            }
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail( "    PortletRequest.getParameterMap() "
                                    + " returned an empty map" );
        }
        request.getPortletSession(true).setAttribute("resultGetParameterMap",
                                resultWriter.toString());
   }

}
