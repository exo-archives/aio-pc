/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.common.util.tags;

import com.sun.ts.tests.portlet.common.util.tags.CustomTag;

/**
 * This class provides a way to enclose text put in the HTTP response body 
 * by portlets or servlets in portlet custom tags.
 * All content is delimited by <tck-portlet> and </tck-portlet>
 * On the client side, a corresponding class PortletTCKCustomClientTag
 * is used to read the content out.
 */
public class PortletTCKCustomTag extends CustomTag {
    public static final String TCK_PORTLET_TAG = "tck-portlet";

    /**
     * Creates a new Portlet TCK custom tag with no name and empty tag content.
     */
    public PortletTCKCustomTag() {
        super(TCK_PORTLET_TAG);
    }

    /**
     * Creates a new Portlet TCK custom tag with the given name and
     * empty tag content.
     *
     * @param tagName name of the custom tag.
     * @exception IllegalArgumentException if tagName is <code>null</code>.
     */
    public PortletTCKCustomTag(String tagName) {
        super(new String[] { tagName, TCK_PORTLET_TAG} );
    }

}
