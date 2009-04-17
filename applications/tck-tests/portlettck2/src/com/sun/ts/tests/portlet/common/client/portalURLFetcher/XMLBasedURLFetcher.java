/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.common.client.portalURLFetcher;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.*;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.io.*;

import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.portlet.common.client.TSPortletInfo;
import com.sun.ts.lib.util.TestUtil;


/**
 * Implementation of IPortalURLFetcher, based on declarative configuration 
 * of the portal page for a TCK test. TCK publishes a master XML file containing 
 * the portlets for each test case. Vendors refer to that XML file for 
 * establishing a portal page for every test. Vendors then provide an XML
 * file with a full URL for the portal page for each test. This implementation 
 * retreives this URL for a test from this vendor specified file. The file name
 * is specified using a property in ts.jte.
 */
public class XMLBasedURLFetcher implements IPortalURLFetcher {


    private static String TS_HOME = "ts_home";

    //*****************************************************
    // Namespace constants 
    //******************************************************
    public static String PORTLET_TCK_NAMESPACE =
        "http://java.sun.com/xml/ns/portlet/portletTCKVendor_1_0.xsd";

    public static String PORTLET_TCK_SCHEMA_LOCATION =
                        "http://java.sun.com/xml/ns/portlet/portletTCKVendor_1_0.xsd "
                        +TestUtil.getProperty(TS_HOME)
                        + File.separator
                        + "bin"
                        + File.separator
                        + "portletTCKVendor_1_0.xsd";



    //******************************************************
    // SAXBuilder related constants 
    //*******************************************************

    // Seems the default is working
    public static String BUILDER_SAX_PARSER = 
                            "org.apache.xerces.parsers.SAXParser";

    public static String BUILDER_VALIDATION_FEATURE = 
                "http://apache.org/xml/features/validation/schema";

    public static String BUILDER_SCHEMA_LOC_PROPERTY = 
                "http://apache.org/xml/properties/schema/external-schemaLocation";

    //*****************************************************
    // Element name constants
    //******************************************************
    private static String TEST_CASE_NAME="test_name";
    private static String TEST_CASE_URL="test_url";

    //*****************************************************
    // Key specifying the vendor provided file. 
    //******************************************************
    private static String VENDOR_TEST_URLS_FILE_KEY = 
                                    "vendorTestsToURLMappingFile";

    //******************************************************
    // Static member variables
    //******************************************************
    private static XMLBasedURLFetcher 
                _instance = null;

    private static Namespace _namespace = 
        Namespace.getNamespace(PORTLET_TCK_NAMESPACE);

    private static Map _testsToURLMap; 


    /**
     * Public method to get an instance of this class
     */
    public static XMLBasedURLFetcher getInstance() throws Fault{

        // Put it in synchronized block
        if (_instance == null) {
            _instance = new XMLBasedURLFetcher();
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
    public String getPortalURL(String testName, TSPortletInfo[] portletInfos) 
                                                        throws Fault{
        String url = (String)_testsToURLMap.get(testName);
        if ( url == null || url.length() == 0) {
            throw new Fault("[XMLBasedURLFetcher]" 
                        + testName 
                        + " Failed as there was no url corresponding to test " 
                        + testName 
                        + " found in vendor file.");
        }

        return url;
    }


    /**
     * Returns a map containing URL for each test case, read from 
     * the XML file provided by the vendor.
     * 
     * @return returns a map.
     */
    public Map getTestsToURLMap() {
        return  _testsToURLMap;
    }
    /**
    * Internal Constructor
    */

    private XMLBasedURLFetcher() throws Fault {
        _testsToURLMap = new HashMap();

        try {

            /*
             * Get a SAX builder with validation
             */
            SAXBuilder builder = getSAXBuilder(true);

            /*
             * Read the xml file and create an xml doc
             */

            String vendorTestURLsFileName = 
                        getVendorTestsToURLMappingFileName();

            FileInputStream inputStream = 
                    new FileInputStream(vendorTestURLsFileName);

            Document doc = builder.build(inputStream);

            /*
             * Iterate through the test_case_url list
             */

            Iterator testCasesIter = 
                        doc.getRootElement().getChildren().iterator();
            while (testCasesIter.hasNext()) {
                Element testCaseElement = (Element) testCasesIter.next();

                // Get the name of the test

                String testName = testCaseElement.getChildText(TEST_CASE_NAME,
                                                _namespace);

                // Get the url 

                String url = testCaseElement.getChildText(TEST_CASE_URL,
                                                _namespace);

                // Add it to as a map entry

                _testsToURLMap.put(testName, url);
            }
        }
        catch(JDOMException ex) {
            throw new Fault("File configured for property " +
                            VENDOR_TEST_URLS_FILE_KEY + 
                            " is not a valid file:" + 
                            getVendorTestsToURLMappingFileName(), ex);
        }
        catch(FileNotFoundException ex) {
            throw new Fault("File configured for property " +
                            VENDOR_TEST_URLS_FILE_KEY + 
                            " was not found:" + 
                            getVendorTestsToURLMappingFileName(), ex);
        }
    }
    /**
     * Gets a SAXBuilder.
     * 
     * @param validate boolean flag to get a validating SAX builder.
     */

    private SAXBuilder getSAXBuilder(boolean validate) {
        SAXBuilder builder = null;

        if (!validate) {
            builder = new SAXBuilder(false);
        }
        else {

            builder = new SAXBuilder( BUILDER_SAX_PARSER, true);
            //builder = new SAXBuilder(true);

            builder.setFeature(BUILDER_VALIDATION_FEATURE, true);

            builder.setProperty( BUILDER_SCHEMA_LOC_PROPERTY,
                                PORTLET_TCK_SCHEMA_LOCATION); 
        }
        return builder;

    }

    /**
     * Returns the value of property 
     * <code>vendorTestsToURLMappingFile</code> from ts.jte file.
     * 
     * @exception com.sun.ts.lib.harness.EETest.Fault
     */
    private String getVendorTestsToURLMappingFileName() throws Fault {

        String fileName = TestUtil.getProperty(
                VENDOR_TEST_URLS_FILE_KEY);

        if (fileName == null || fileName.length() == 0) {
            throw new Fault("Configuration property " + 
                VENDOR_TEST_URLS_FILE_KEY + " is empty");
        }

        return fileName;
    }

    public String toString() {

        StringBuffer strbuf = new StringBuffer();

        Iterator keyIter = getTestsToURLMap().keySet().iterator();
        while (keyIter.hasNext()) {
            String testName = (String) keyIter.next();
            strbuf.append("\nTest Case:" + testName);
            String url = (String)getTestsToURLMap().get(testName);
            strbuf.append("\nTest Case URL:" + url);
        }
        return strbuf.toString();
    }
    public static void main(String args[]) throws Exception {
        XMLBasedURLFetcher x = XMLBasedURLFetcher.getInstance();
        System.out.println(x.toString());
    }
        
}
