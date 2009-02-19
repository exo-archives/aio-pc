/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.common.util.requestcount;
import javax.portlet.RenderRequest;
import javax.portlet.PortletSession;
import javax.portlet.RenderResponse;
import com.sun.ts.tests.portlet.common.util.requestcount.*;

/**
 * Implementation of IRequestCount interface defined to keep track of the
 * request number. This implementation is used by the portlets to find
 * out what request number they are on. This implementation delegates the logic to 
 * RequestCountBySession or RequestCountByParam class, based on
 * a paramater passed through the constructor.
 */
public class RequestCount implements IRequestCount {

    public static int MANAGED_VIA_SESSION = 0;
    public static int MANAGED_VIA_PARAM = 1;

    IRequestCount _impl = null; 

    /**
     * Constructor
     * 
     * @param request portlet request
     * @param response portlet response
     * @param type can be either MANAGED_VIA_SESSION or MANAGED_VIA_PARAM
     */
    public RequestCount(RenderRequest request, RenderResponse response, int type) {

        if ( type != MANAGED_VIA_SESSION && type != MANAGED_VIA_PARAM) {
            throw new IllegalArgumentException(
                    "Type must be RequestCount.MANAGED_BY_SESSION" +
                    " or RequestCount.MANAGED_BY_PARAM");
        }

        if (type == MANAGED_VIA_SESSION) {
            _impl = new RequestCountBySession(request, response);
        }
        else if ( type == MANAGED_VIA_PARAM) {
            _impl = new RequestCountByParam(request, response);
        }
   }
        
    /**
     * Returns true if it is the first request
     */
    public boolean isFirstRequest() {
        return _impl.isFirstRequest();
    }  

    /**
     * Returns the request number 
     */
    public int getRequestNumber() {
        return _impl.getRequestNumber(); 
    }

}
