/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.common.client.tags;

import com.sun.ts.tests.portlet.common.util.tags.PortletTCKCustomTag;

/**
 * This class provides a way to create and extract the 
 * content enclosed in Portlet custom tags from the HTTP response body.
 */
public class PortletTCKCustomClientTag extends CustomClientTag {

    private static PortletTCKCustomClientTag privateTag = 
                            new PortletTCKCustomClientTag();

    /**
     * Creates a new Portlet TCK custom tag with no name and empty tag content.
     */
    public PortletTCKCustomClientTag() {
        super(PortletTCKCustomTag.TCK_PORTLET_TAG);
    }

    /**
     * Creates a new Portlet TCK custom tag with the given name and
     * empty tag content.
     *
     * @param tagName name of the custom tag.
     * @exception IllegalArgumentException if tagName is <code>null</code>.
     */
    public PortletTCKCustomClientTag(String tagName) {
        super(new String[] { tagName, PortletTCKCustomTag.TCK_PORTLET_TAG} );
    }
    public PortletTCKCustomClientTag(String startTag, String endTag) {
        super(checkRequiredStartTag(startTag),
             checkRequiredEndTag(endTag));
    }

    public static String[] extractPortletTCKCustomContent(String str){        
        return privateTag.extractTagsFromString(str);
    }

    static private String checkRequiredStartTag(String tag) {
        if ( tag.indexOf(PortletTCKCustomTag.TCK_PORTLET_TAG) == -1) {
            return makeStartTag(PortletTCKCustomTag.TCK_PORTLET_TAG) + tag;
        }
        else {
            return tag;
        }
    }
    static private String checkRequiredEndTag(String tag) {
        if ( tag.indexOf(PortletTCKCustomTag.TCK_PORTLET_TAG) == -1) {
            return tag + makeEndTag(PortletTCKCustomTag.TCK_PORTLET_TAG) ;
        }
        else {
            return tag;
        }
    }
        

}
