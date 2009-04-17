/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.common.client.tags;

import java.io.IOException;
import java.util.ArrayList;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.common.webclient.http.HttpResponse;
import com.sun.ts.tests.portlet.common.util.tags.CustomTag;


/**
 * This client side class creates and provides methods to extract content 
 * enclosed in the custom tags from the HTTP response body.
 * @see CustomTag
 */
public class CustomClientTag extends CustomTag {
    public CustomClientTag() {
        super();
    }

    /**
     * Creates a new custom tag with the given name and empty tag content.
     *
     * @param tagName name of the custom tag.
     * @exception IllegalArgumentException if tagName is <code>null</code>.
     */
    public CustomClientTag(String tagName) {
        super(tagName);
    }

    /**
     * Creates a nested custom tag with the names and empty tag content.
     * The order of nesting is based on position of the tag in the array.
     * The tags with lower index are inside the tags with higher index.
     *
     * @param tagNames names of the nested custom tags.
     * @exception IllegalArgumentException if ant tagname is <code>null</code>.
     */
    public CustomClientTag(String[] tagNames) {
        super(tagNames);
    }

    public CustomClientTag(String startTag, String endTag) {
        super(startTag, endTag);
    }


    /**
     * Extracts all instances of tag contents with this custom tag's
     * name from the HTTP response body.
     *
     * @param response the HTTP response.
     * @return a String array.
     * @exception Fault if no custom tag is read from the HTTP response body.
     */
    public String[] extractTags(HttpResponse response) throws Fault {
        try {
            String responseBody = response.getResponseBodyAsString();
            String[] tags = extractTagsFromString(responseBody);
            if ( tags == null || tags.length <=0) {
                throw new Fault("Failed to find a tag in the HTTP response"
                    + " body starting with the tag :" +  getStartTag() + "\n"
                    + " and ending with the tag :" +  getEndTag()
                    + ".\n");
            }
            return tags;
        } catch (IOException e) {
            throw new Fault("Can't read the HTTP response.", e);
        }

    }
    /**
     * Extracts all instances of tag contents with this custom tag's
     * name from a given string.
     *
     * @param responseBody string to be parsed.
     * @return a String array.
     */
    public String[] extractTagsFromString(String responseBody) {
        int startTagLength = getStartTag().length();
        int endTagLength = getEndTag().length();
        ArrayList tags = new ArrayList();
        int start = responseBody.indexOf(getStartTag());

        while (start != -1) {
            int endOfStartTag = start + startTagLength;
            int end = responseBody.indexOf(getEndTag(), endOfStartTag);

            if (end == -1) {
                start = -1;
            } else {
                tags.add(responseBody.substring(endOfStartTag, end));
                start = responseBody.indexOf(getStartTag(), end + endTagLength);
            }
        }

        if (tags.isEmpty()) {
            TestUtil.logTrace("No tag is found in the given string.");
        }

        return (String[])tags.toArray(new String[tags.size()]);
    }

    /**
     * Extracts the first instance of tag content with this custom
     * tag's name from the HTTP response body.
     *
     * @param response the HTTP response.
     * @return the content of the first tag with this custom tag's name.
     * @exception Fault if no custom tag is read from the HTTP response body.
     */
    public String extractTag(HttpResponse response) throws Fault {
        String[] tags = extractTags(response);
        return tags[0];
    }
}
