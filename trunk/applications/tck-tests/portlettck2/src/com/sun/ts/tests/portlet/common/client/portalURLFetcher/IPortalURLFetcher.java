/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.common.client.portalURLFetcher;

import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.portlet.common.client.TSPortletInfo;

 /**
  * An interface for getting the vendor specific URL used to 
  * interact with the portlets participating for a test.
  */

public interface IPortalURLFetcher {

    /**
     * Returns a vendor specific URL string to a portal page that 
     * aggregates content of all portlets specified in the parameter
     * list.
     * 
     * @param testName Unique name of the test
     * @param portletInfos List of portlets participating in the test.
     * @return A URL String
     * @exception com.sun.ts.lib.harness.EETest.Fault
     */
    public String getPortalURL(String testName, TSPortletInfo[] portletInfos) throws Fault;

}
