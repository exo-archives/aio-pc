/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.common.client.tags;

public class URLDecoder {

     private static final String AMP = "&";
     private static final String AMP1 = "&#38;";
     private static final String AMP2 = "&amp;";

     private static String decode(String url,String from,String to) {
         int i;
         while ((i=url.indexOf(from))>=0) {
             url = url.substring(0,i)+to+url.substring(i+from.length());
         }
         return url;
     }

     public static String decode(String url) {
         url = decode(url,AMP1,AMP);
         url = decode(url,AMP2,AMP);
         return url;
     }

}
