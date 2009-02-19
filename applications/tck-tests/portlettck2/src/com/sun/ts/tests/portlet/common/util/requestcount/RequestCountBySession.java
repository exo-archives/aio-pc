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
 * out what request number they are on. This implementation sets attribute 
 * in PortletRequest & PortletSession to keep track of the Request Number. 
 * Here is brief explanation of how it works.There are two steps: 
 * first read the current request number from the session 
 * and set it in the PortletRequest. Second step is to set up the request
 * number for the next request in the session. 
 * 
 * The only exception to above mechanism is the first request, when there is
 * no attribute set in the brand new session or in the request. Absence of 
 * such attribute in the session signals the first request and the methods 
 * sets the request number for the first request based on this.
 */
public class RequestCountBySession implements IRequestCount {

    public static String REQUEST_COUNTER_ALREADY_INCREMENTED = 
                                        "tckRequestCountAlreadyIncremented";
    private RenderRequest _request;
    private int _requestNumber = 0;
    

    /**
     * @param request Portlet Request object
     * @param response Portlet Response Object ( not used TODO. Take it out.)
     */
    public RequestCountBySession(RenderRequest request, RenderResponse response) {

        _request = request;
        setUpNumberForCurrentRequest();
        setUpNumberForNextRequest();

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
     * Sets up the request number as an attribute in PortletRequest.
     */
    private void setUpNumberForCurrentRequest() {

        /*
         * Request Number within a request should be obtained from the 
         * PortletRequest object. It's value is established by whatever 
         * is found in the PortletSession for attribute
         * REQUEST_COUNTER by the every portlet calling this method for 
         * processing of this request.
         */
        String counterStr = (String) _request.getAttribute(REQUEST_COUNTER);

        if (counterStr == null) {

            /*
             * Establish the value as PortletRequest attribute using 
             * the value of REQUEST_COUNTER in Session
             */
            PortletSession session = _request.getPortletSession();
            counterStr = (String)session.getAttribute(REQUEST_COUNTER, 
                            PortletSession.PORTLET_SCOPE);
            if ( counterStr == null) {
                counterStr = Integer.toString(0);
            }

            // set the request number in the request
            _request.setAttribute(REQUEST_COUNTER, counterStr);
        }

        _requestNumber =  Integer.parseInt(counterStr);
    }


    /**
     * Incrementes the _request counter and sets it as a PortletSession 
     * attribute for NEXT _request. The logic makes sure that  the counter 
     * is incremented only once by a portlet in the request. 
     */

    private void setUpNumberForNextRequest() {

        if ( !isAlreadyIncremented()) {
            PortletSession session = _request.getPortletSession();
            session.setAttribute(REQUEST_COUNTER, 
                            String.valueOf(_requestNumber + 1),
                            PortletSession.PORTLET_SCOPE);
            setAlreadyIncremented();
        }
    }

    /**
    * Request number must be set for session for portle in every application involved.
    * This method checks if value of REQUEST_COUNTER_ALREADY_INCREMENTED has the 
    * current session id.
    */

    private boolean isAlreadyIncremented() {
        String alreadyIncremented = 
            (String) _request.getAttribute(REQUEST_COUNTER_ALREADY_INCREMENTED);

        if (alreadyIncremented == null || (alreadyIncremented.indexOf(
                            _request.getPortletSession().getId())) == -1 ) {
            return false;
        }
        else {
            return true;
        }
    }

    /**
    * Request number must be set for session for every application involved.
    * This method appends the current session if to the attribute 
    * REQUEST_COUNTER_ALREADY_INCREMENTED.
    */
    private void setAlreadyIncremented() {
        String alreadyIncremented = 
            (String) _request.getAttribute(REQUEST_COUNTER_ALREADY_INCREMENTED);
        if ( alreadyIncremented == null) {
            alreadyIncremented = _request.getPortletSession().getId();
        }
        else {
            alreadyIncremented += _request.getPortletSession().getId();
        }
        
        _request.setAttribute(
                        REQUEST_COUNTER_ALREADY_INCREMENTED, alreadyIncremented);
    }
}
