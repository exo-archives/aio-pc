/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.common.client.tags;

import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.portlet.common.client.tags.PortletTCKCustomClientTag;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;
import com.sun.ts.tests.common.webclient.http.HttpResponse;

/**
 * This client side class creates and provides methods 
 * to extract the portlet url string  enclosed in PortletTCKTags
 * from the HTTP response body.
 */
public class PortletURLClientTag extends PortletTCKCustomClientTag {
    

    private static PortletURLTag privateTag = new PortletURLTag();
    private static PortletURLClientTag privateClientTag = new PortletURLClientTag();
    /**
     * Creates a new Portlet TCK custom tag with no name and empty tag content.
     */
    public PortletURLClientTag() {
        super(privateTag.getStartTag(), privateTag.getEndTag());
    }
    
    /**
     * Extract the PortletURL string from the HttpResponse.
     */
     
    public static String extractContent(HttpResponse response) throws Fault{        
        return URLDecoder.decode(privateClientTag.extractTag(response));
    }

}
