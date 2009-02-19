/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.common.client.portalURLFetcher;

import com.sun.ts.tests.common.webclient.WebTestCase;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.lib.util.TestUtil;

import com.sun.ts.lib.porting.TSPortletContainerURLInterface;
import com.sun.ts.tests.portlet.common.client.TSPortletInfo;

/**
 * An implementation of IPortalURLFetcher, that lets a vendor hook an 
 * implementation of interface TSPortletContainerURLInterface. The implementation
 * class is specified as a property 
 * <code>porting.ts.portletContainerURL.class</code>. This interface
 * will return the URL for portal page for the given test by creating instance 
 * of this class and calling its method. ( TODO:: there is no need to have this
 * class. Vendors should be able to implement IPortalURLFetcher itself.
 */

public class CustomClassURLFetcher implements IPortalURLFetcher {


    static private String PORTING_TS_PORTLET_CONTAINER_URL_CLASS_KEY = 
                        "porting.ts.portletContainerURL.class";


    private static CustomClassURLFetcher _instance = null;
    private TSPortletContainerURLInterface _tsPortletContainerURL ;


    /**
    * Method to get static instance of the class
    */

    public static CustomClassURLFetcher getInstance() throws Fault{

        // Put it in synchronized block
        if (_instance == null) {
        _instance = new CustomClassURLFetcher();
        }
        return _instance;
    }

    /**
    * Private constructor
    */

    CustomClassURLFetcher() throws Fault{

        try
        {
            // create and initialize a new instance of 
            // porting.ts.portletContainerURL.class

            Class c = Class.forName(TestUtil.getProperty(
                    PORTING_TS_PORTLET_CONTAINER_URL_CLASS_KEY));
            _tsPortletContainerURL = 
                (TSPortletContainerURLInterface)c.newInstance();

        } catch(Exception e) {
            e.printStackTrace();
            throw new Fault("[CustomClassURLFetcher] " 
                           // + _testName 
                            + "failed! " + e.toString()); 
        }
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
    public String getPortalURL(String testName, TSPortletInfo[] portletInfos) 
                                                            throws Fault{
        String url = _tsPortletContainerURL.createURL(portletInfos);
        return url;
    }
    
}
