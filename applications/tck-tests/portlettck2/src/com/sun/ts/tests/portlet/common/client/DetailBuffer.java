/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.common.client; 

import java.io.IOException;
import java.net.URL;

import com.sun.ts.tests.common.webclient.http.HttpRequest;
import com.sun.ts.tests.common.webclient.http.HttpResponse;
import com.sun.ts.tests.portlet.common.client.tags.PortletTCKCustomClientTag;
import com.sun.ts.lib.util.TestUtil;



/**
 * This class uses a thread local object to store 
 * information to be printed out to the user. 
 * This is available everywhere and can be used to push details about the 
 * test run that would be printed out when a test fails.
 */
public class DetailBuffer{

    public static String RESPONSE_CONTENT_OUTPUT_LEVEL = "outputResponseContentLevel";
    public static String RESPONSE_CONTENT_OUTPUT_LEVEL_TCK = "0";
    public static String RESPONSE_CONTENT_OUTPUT_LEVEL_ALL = "1";

    private static ThreadLocal detailBuffer= new ThreadLocal();  

    private DetailBuffer() {
	// nothing, cannot be called
    }

    public static StringBuffer getDetailBuffer() {
      StringBuffer buf = (StringBuffer)detailBuffer.get();
      if ( buf == null) {
        buf = new StringBuffer();
        buf.append("\n*********************************************************");
	    detailBuffer.set(buf);
      }
      return (StringBuffer)detailBuffer.get();

    }
    public static void addSeparator1() {
        StringBuffer buf = getDetailBuffer();
        buf.append("\n*********************************************************");
    }

    public static void addSeparator2() {
        StringBuffer buf = getDetailBuffer();
        buf.append("\n----------------------------------------------------------");
    }
    public static void addSeparator3() {
        StringBuffer buf = getDetailBuffer();
        buf.append("\n%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
    }

    public static void addDetail(String str) {
        StringBuffer buf = getDetailBuffer();
        buf.append(str);
    }
    public static String toStr() {
        StringBuffer buf = getDetailBuffer();
        return buf.toString();
    }

    public static void recordRequestNumber(int requestNumber) {
        addDetail("\nRequest number:" + requestNumber);
    }


    public static void recordRequestInfo(String currentHost,
                                        int currentPort,
                                        String currentURI,
                                        URL currentURL) {
        addSeparator2();
        addDetail("\nRequest Host:" + currentHost);
        addSeparator2();
        addDetail("\nRequest Port:" + currentPort);
        addSeparator2();
        addDetail("\nRequest URI:" + currentURI);
        addSeparator2();
        addDetail("\nRequest Complete URL:" + currentURL);
    }

    public static void recordAuthenticationInfo(String authType) {
        addSeparator2();
        addDetail("\nAuthentication for the test:" + authType);
        addSeparator2();
    }

    public static void recordCookieAuthenticationInfo(String cookie) {
        addSeparator2();
        addDetail("\nAuthentication Cookie to be sent:" + cookie);
        addSeparator2();
    }
                                                

    public static void recordResponseInfo(HttpResponse response)
                                                throws IOException {
        addSeparator2();

        String responseBody = response.getResponseBodyAsString();

        String responseLevel = 
                TestUtil.getProperty(RESPONSE_CONTENT_OUTPUT_LEVEL);

        if (responseLevel == null) {
            responseLevel = RESPONSE_CONTENT_OUTPUT_LEVEL_TCK;
        }

        String responseStringsForOutput = null;
        if ( responseLevel.equals(RESPONSE_CONTENT_OUTPUT_LEVEL_TCK)) {
            addDetail("\nTCK specific strings from server response:\n");
            addSeparator2();

            StringBuffer buf = new StringBuffer();
            String[] content = 
              PortletTCKCustomClientTag.extractPortletTCKCustomContent(responseBody); 
            for ( int i = 0; content != null && i < content.length; i++) {
                buf.append("\n" + content[i]);
            }
            responseStringsForOutput = buf.toString();
        }
        else {
            addDetail("\n Complete server response:\n");
            addSeparator2();
            addDetail("\n");
            responseStringsForOutput = responseBody; 
        }

        addDetail(responseStringsForOutput);
        addSeparator2();


    }
            
}
