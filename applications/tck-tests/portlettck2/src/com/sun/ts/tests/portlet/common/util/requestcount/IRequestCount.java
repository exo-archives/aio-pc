/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.common.util.requestcount;


/**
 * There are lot of tests in the portlet TCK, that span through
 * more than one request. Test Portlets involved in these kind 
 * of tests need to know what request number they are on, for 
 * executing desired logic. This is an interface defined 
 * to keep track of request number.
 */
public interface IRequestCount {

    public static String REQUEST_COUNTER = "tckRequestCount";
        
    /**
     * Returns true if it is the first request
     */
    public boolean isFirstRequest() ; 

    /**
     * Returns the request number
     */
    public int getRequestNumber() ; 

}
