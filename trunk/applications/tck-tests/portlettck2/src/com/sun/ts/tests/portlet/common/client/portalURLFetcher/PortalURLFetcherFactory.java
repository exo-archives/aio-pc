/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.common.client.portalURLFetcher;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.portlet.common.client.TSPortletInfo;


/**
 * Since aggregation of portlets in a portal page and the 
 * URLs used to interact with the portlets are vendor specific, 
 * TCK provides couple of alternative mechanisms in the framework
 * to get the URLs to portal pages for the test cases, e.g. 
 * declarative configuration or programmatic configuration. 
 * A vendor must support at least one of these mechanisms to
 * run the conformance tests. Based on the option chosen by the vendor
 * as specified in ts.jte file, this class returns an instance of
 * IPortalURLFetcher.
 */
public class PortalURLFetcherFactory {

    /**
     * Name of the property deciding the mechanism used to retrieve
     * a vendor specific URL for a portal page.
     */
    public static String PORTAL_URL_FETCHER_MODE = "portalURLFetcherMode";

    /**
     * Declarative method
     */
    public static String XML_BASED_URL_FETCHER="0";

    /**
     * Programmatic method
     */
    public static String PROGRAMMATIC_URL_FETCHER = "1";

    /**
     * Interface method
     */
    public static String CUSTOM_CLASS_URL_FETCHER="2";


    /**
     * Based on the value of property 
     * <code>portalURLFetcherMode</code> specified in ts.jte file,
     * this method returns an implementation of IPortalURLFetcher interface.
     * 
     * @exception com.sun.ts.lib.harness.EETest.Fault
     */
    public static IPortalURLFetcher getPortalURLFetcher() throws Fault{
        String portalURLFetcherMode = TestUtil.getProperty(
                            PORTAL_URL_FETCHER_MODE);

        if (portalURLFetcherMode.equals(CUSTOM_CLASS_URL_FETCHER)) {
            return CustomClassURLFetcher.getInstance();
        }
        else if (portalURLFetcherMode.equals(XML_BASED_URL_FETCHER)) {
            return XMLBasedURLFetcher.getInstance();
        }
        else if (portalURLFetcherMode.equals(PROGRAMMATIC_URL_FETCHER)) {
            return ProgrammaticURLFetcher.getInstance();
        }
        else {
            throw new Fault("Value of configuration property " 
            + PORTAL_URL_FETCHER_MODE 
            + " needs to be 0, 1 or 2");
        }
    }

}
