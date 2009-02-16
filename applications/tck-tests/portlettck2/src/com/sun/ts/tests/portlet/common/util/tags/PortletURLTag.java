/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.common.util.tags;

import com.sun.ts.lib.harness.EETest.Fault;

/**
 * This class provides a way to enclose Portlet URL String 
 * in the HTTP response body by portlets in custom tags.
 * On the client side, a corresponding class PortletURLClientTag
 * is used to read the text out.
 */
public class PortletURLTag extends PortletTCKCustomTag {
    

    /**
     * Creates a new Portlet TCK custom tag with no name and empty tag content.
     */
    public PortletURLTag() {
        super("portlet-url");

        // make it a real hyperlink
        _startTag = _startTag + "<a href=\"";
        _endTag = "\">PortletURL</a>" + _endTag;
    }

}
