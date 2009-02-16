/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletRequestDispatcher;

public class SameThreadTestThreadLocalizer {
	
    private static ThreadLocal<String> sameThreadTestThreadLocal = new ThreadLocal<String>();

    private SameThreadTestThreadLocalizer() {
	// nothing, cannot be called
    }

    public static String get() {
        return sameThreadTestThreadLocal.get();
    }

    
    public static void set(String value) {
    	sameThreadTestThreadLocal.set(value);
    }
}
