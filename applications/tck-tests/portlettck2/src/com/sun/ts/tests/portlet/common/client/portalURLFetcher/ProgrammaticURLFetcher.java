/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.common.client.portalURLFetcher;

import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.portlet.common.client.TSPortletInfo;


/**
 * An implementation of IPortalURLFetcher, for programmatic configuration of 
 * vendor URL. The a part of that URL is specified as a configuration parameter
 * to the TCK in ts.jte file. This interface
 * will return this URL with a set of parameters indicating the set of portlets 
 * that must appear in a portal page for the given test. Upon receiving 
 * this request, the vendor provided URL should dynamically create a portal
 * page with the required portlets.  The parameter names on the URL are 
 * multiple occurrences of "portletName". Values of this
 * paramater are string consisting of application name and portlet name 
 * delimited by /.
 */
public class ProgrammaticURLFetcher implements IPortalURLFetcher {


    /**
     * Property name in ts.jte
     */
    static private String VENDOR_PORTAL_URL = 
                        "vendorPortalURL";


    /**
     * static instance
     */
    private static ProgrammaticURLFetcher _instance = null;


    /**
    * Method to get static instance of the class
    */

    public static ProgrammaticURLFetcher getInstance() throws Fault{

        // Put it in synchronized block
        if (_instance == null) {
            _instance = new ProgrammaticURLFetcher();
        }
        return _instance;
    }


   
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
     
    public String getPortalURL(String testName, TSPortletInfo[] portletInfos) throws Fault{

        StringBuffer buf = new StringBuffer(getVendorPortalURL());
        String separator = "";

        String getPortalURL = getVendorPortalURL();

        /*
         * Get the ? and & right based on the url
         */
        if ( getPortalURL.indexOf("?") == -1) {
            buf.append("?");
        }
        else {
            //if there is a & already and it is not on the end, then separator is &.

            if (!getPortalURL.endsWith("?")
                && !getPortalURL.endsWith("&")) {
                separator="&";
            }
                    
        }

        // Now append the portlet names
        for ( int i = 0; portletInfos != null && i < portletInfos.length; i++){
            buf.append(separator 
                    + "portletName=" 
                    + portletInfos[i].getPortletApplication()
                    + "/" 
                    + portletInfos[i].getPortletName());

            separator = "&";
        }
        System.out.println(buf);
        return buf.toString();

    }

    /**
     * Returns the value of property <code>vendorPortalURL</code>
     * from the ts.jte.
     * 
     * @return the url specified in ts.jte
     * @exception com.sun.ts.lib.harness.EETest.Fault
     */
    protected String getVendorPortalURL() throws Fault {
            String vendorPortalURL = TestUtil.getProperty(VENDOR_PORTAL_URL);

            if (vendorPortalURL == null || vendorPortalURL.length() == 0) {
                throw new Fault("[ProgrammaticURLFetcher] Value for configuration property is null or zero length.");
            }

            return vendorPortalURL;

    }
}
