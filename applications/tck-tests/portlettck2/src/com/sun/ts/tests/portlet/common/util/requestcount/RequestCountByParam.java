/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.common.util.requestcount;

import javax.portlet.RenderRequest;
import javax.portlet.PortletSession;
import javax.portlet.RenderResponse;

/**
 * Implementation of IRequestCount interface defined to keep track of the
 * request number. This implementation is used by the portlets to find
 * out what request number they are on. This implementation uses 
 * parameters set on PortletURL to figure out what is the current request 
 * number.
 * Here is brief explanation of how it works.There are two steps: 
 * To figure out the current request number, value of 
 * PortletRequest parameter REQUEST_COUNTER is read. Responsibility to 
 * set the counter for next request is on the Portlet itself, that creates 
 * a PortletURL, with paramater REQUEST_COUNTER having incremented request 
 * number on it.
 * 
 * The only exception to above mechanism is the first request, when there is
 * no paramater set in the request parameter. Absence of 
 * such paramater in the PortletRequest signals the first request and the 
 * methods return the request number for the first request based on this.
 */
public class RequestCountByParam implements IRequestCount {

    private RenderRequest _request;
    private int _requestNumber = 0;
    

    public RequestCountByParam(RenderRequest request, RenderResponse response) {

        _request = request;
        setUpNumberForCurrentRequest();

   }
        
    /**
     * Returns true if it is the first request
     */
    public boolean isFirstRequest() {
        if (_requestNumber == 0) {
            return true;
        }
        else {
            return false;
        }
    }  

    /**
     * Returns the request number 
     */
    public int getRequestNumber() {
        return _requestNumber;
    }

    /**
     * Sets up the request number based on parameter in PortletRequest.
     */
    private void setUpNumberForCurrentRequest() {

        String counterStr = _request.getParameter(REQUEST_COUNTER);

        if ( counterStr == null) {
            counterStr = Integer.toString(0);
        }

        _requestNumber =  Integer.parseInt(counterStr);
    }
}
