/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.common.util.tags;

import java.io.IOException;
import java.util.ArrayList;


/**
 * This class provides a way to enclose text put in the HTTP response body 
 * by portlets or servlets in custom tags.
 * On the client side, a corresponding class CustomClientTag
 * is used to read the text out.
 */
public class CustomTag {
    protected String _startTag = "";
    protected String _endTag = "";
    private StringBuffer _tagContent = new StringBuffer();

    /**
     * Creates a new custom tag with no name and empty tag content.
     */
    public CustomTag() {
        this("");
    }

    /**
     * Creates a new custom tag with the given name and empty tag content.
     *
     * @param tagName name of the custom tag.
     * @exception IllegalArgumentException if tagName is <code>null</code>.
     */
    public CustomTag(String tagName) {
        /*
         * Note: an empty tag name is actually allowed, in case you
         * can't come up with a good name.  :-)  In this case, all
         * you have is <>stuffs between the empty tags</>.  If you
         * have only one tag in your HTTP response body, this may not
         * be a bad idea...
         */
        if (tagName == null) {
            throw new IllegalArgumentException("tagName can't be null");
        }

        _startTag = "<" + tagName + ">";
        _endTag = "</" + tagName + ">";
    }


    /**
     * Creates a nested custom tag with the names and empty tag content.
     * The order of nesting is based on position of the tag in the array.
     * The tags with lower index are inside the tags with higher index.
     *
     * @param tagNames names of the nested custom tags.
     * @exception IllegalArgumentException if ant tagname is <code>null</code>.
     */
    public CustomTag(String[] tagNames) {
        if (tagNames == null) {
            throw new IllegalArgumentException("tagNames can't be null");
        }

        for ( int i = 0; i < tagNames.length; i++) {
            if (tagNames[i] != null) {
                _startTag = "<" + tagNames[i] + ">" + _startTag;
                _endTag = _endTag + "</" + tagNames[i] + ">";
            }
            else {
                throw new IllegalArgumentException("tagNames can't be null");
            }
        }
    }


    /**
     * Creates a custom tag with explicitly provided start and end tag.
     * @param startTag name of the start tag.
     * @param endTag name of the end tag.
     */

    public CustomTag(String startTag, String endTag) {
        _startTag = startTag;
        _endTag = endTag;
    }

    /*
     * Sets the tag content.
     *
     * @param content the tag content to be set to.  It will be set
     *                   literally to the string "null" if content
     *                   is <code>null</code>.
     */
    public void setTagContent(String content) {
        _tagContent = new StringBuffer((content == null) ? "null" : content);
    }

    /*
     * Appends the tag content.
     *
     * @param content the tag content to be set to.  It will be set
     *                   literally to the string "null" if content
     *                   is <code>null</code>.
     */
    public void appendTagContent(String content) {
        _tagContent.append((content == null) ? "null" : content);
    }

    /**
     * Returns a String that can be written out to the HTTP response.
     */
    public String toString() {
        return _startTag + _tagContent + _endTag;
    }

    public String getEndTag() {
        return _endTag;
    }

    public String getStartTag() {
        return _startTag;
    }

    static public String makeStartTag(String tag) {
        return "<" + tag + ">";
    }
    static public String makeEndTag(String tag) {
        return "</" + tag + ">";
    }

}
