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


public class PortletTCKTestCases {

    public static String XML_FILE_OPER="masterXMLFileOper";
    public static String XML_FILE_OPER_WRITE="0";
    public static String XML_FILE_OPER_CHECK_CONSISTENT="1";

    private static String TS_HOME = "ts_home";

    //*****************************************************
    // Namespace constants 
    //******************************************************
    public static String PORTLET_TCK_NAMESPACE =
        "http://java.sun.com/xml/ns/portlet/portletTCK_1_0.xsd";

    public static String PORTLET_TCK_SCHEMA_LOCATION =
        "http://java.sun.com/xml/ns/portlet/portletTCK_1_0.xsd " 
            + TestUtil.getProperty(TS_HOME)
            + File.separator
            + "bin"
            + File.separator
            + "portletTCK_1_0.xsd";


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
    private static String TEST_CASE_PORTLETS="test_portlet";
    private static String PORTLET_NAME="portlet_name";
    private static String APP_NAME="app_name";

    //*****************************************************
    // Location of Master xml file 
    //******************************************************
    private static String PORTLET_TCK_TEST_CASES_FILE = 
        "bin" + File.separator + "portletTCKTestCases.xml";

    //******************************************************
    // Static member variables
    //******************************************************
    private static PortletTCKTestCases 
                _instance = null;

    private static Namespace _namespace = 
        Namespace.getNamespace(PORTLET_TCK_NAMESPACE);

    private static Map _testsToPortletsMap; 


    /**
     * Public method to get an instance of this class
     */
    public static PortletTCKTestCases getInstance() throws Fault{

        // Put it in synchronized block
        if (_instance == null) {
            _instance = new PortletTCKTestCases();
        }
        return _instance;
    }

    public static void xmlFileHouseKeeping(
                                String uniqueTestName,
                                TSPortletInfo[] portletInfos) throws Fault{

        if (XML_FILE_OPER_WRITE.equals(TestUtil.getProperty(XML_FILE_OPER))) {
            /*
             * Turn this on only when generating the master xml list.
             * Turned off for regular mode
             */

             getInstance().writePortletsToXMLFile(uniqueTestName, 
                                portletInfos) ;
        }
        else if (XML_FILE_OPER_CHECK_CONSISTENT.equals(
                        TestUtil.getProperty(XML_FILE_OPER))) {
            /*
             * Checking that published XML file does has a entry for this 
             * test and
             * the portlets are same as provided here.
             * Turned off for regular mode
             */
             getInstance().checkPortletsFromXMLFile(
                uniqueTestName, portletInfos);
        }

    }
    public TSPortletInfo[] getPortletInfoList(String testName) {
        List list =  (List) _testsToPortletsMap.get(testName);
        if ( list != null) {
            TSPortletInfo[] infos = new TSPortletInfo[list.size()];
            list.toArray(infos);
            return infos;
        }
        return null;
    }
    public Map getTestsToPortletsMap() {
        return _testsToPortletsMap;
    }


    public String toString() {

        StringBuffer strbuf = new StringBuffer();

        Iterator keyIter = getTestsToPortletsMap().keySet().iterator();
        while (keyIter.hasNext()) {
            String testName = (String) keyIter.next();
            strbuf.append("\nTest Case:" + testName);
            List portletInfoList = (List)getTestsToPortletsMap().get(testName);
            Iterator portletInfoListIter = portletInfoList.iterator();
            while (portletInfoListIter.hasNext()) {
                TSPortletInfo portletInfo =
                        (TSPortletInfo) portletInfoListIter.next();
                        strbuf.append("\n\t\t portletInfo=[" 
                        + portletInfo.getPortletApplication() 
                        + ":" 
                        + portletInfo.getPortletName() 
                        + "] ");
            }
        }
        return strbuf.toString();
    }


    //---------------------------------------------------------------
    // PRIVATE METHODS
    //
    //--------------------------------------------------------------


    /**
    * Internal Constructor
    */

    private PortletTCKTestCases() throws Fault {
        _testsToPortletsMap = new HashMap();

        try {

            /*
             * Get a SAX builder with validation
             */
            SAXBuilder builder = getSAXBuilder(true);

            /*
             * Read the xml file and create an xml doc
             */

            File masterFile = new File(TestUtil.getProperty(TS_HOME),
                            PORTLET_TCK_TEST_CASES_FILE);

            FileInputStream inputStream = 
                    new FileInputStream(masterFile);

            Document doc = builder.build(inputStream);

            /*
             * Iterate through the test_case list
             */

            Iterator testCasesIter = 
                        doc.getRootElement().getChildren().iterator();
            while (testCasesIter.hasNext()) {
                Element testCaseElement = (Element) testCasesIter.next();

                // Get the name of the test

                String testName = testCaseElement.getChildText(TEST_CASE_NAME,
                                                _namespace);

                // Get the portlet lists

                List portletInfo = getPortletInfoList(testCaseElement);

                // Add it to as a map entry

                _testsToPortletsMap.put(testName, portletInfo);
            }
        }
        catch(JDOMException ex) {
            ex.printStackTrace();
            throw new Fault("Error parsing XML file provided by TCK ", ex);
        }
        catch(FileNotFoundException ex) {
            ex.printStackTrace();
            throw new Fault("Error looking for XML file provided by TCK", ex);
        }
    }

    private List getPortletInfoList(Element testCaseElement) {
        ArrayList portletList = new ArrayList();

        Iterator portletIter = 
            testCaseElement.getChildren(TEST_CASE_PORTLETS, _namespace).iterator();
        while (portletIter.hasNext()) {
            Element portletElement = (Element)portletIter.next();
            String appName = portletElement.getChildTextTrim(APP_NAME, 
                                                            _namespace);
            String portletName = portletElement.getChildTextTrim(PORTLET_NAME,
                                                            _namespace);
            TSPortletInfo portletInfo = new TSPortletInfo(
                            appName,
                            portletName,
                            portletName);
            portletList.add(portletInfo);
        }
        return portletList;
    }

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

    private void checkPortletsFromXMLFile(String uniqueTestName, 
                            TSPortletInfo[] portletInfos) throws Fault {
        TSPortletInfo[] fromXMLFile = 
                        getPortletInfoList(uniqueTestName);
        if ( fromXMLFile == null ) {
            throw new Fault(
                " Entry not found in master XML file for testCase:" + 
                uniqueTestName);
        }
            
        if ( fromXMLFile.length != portletInfos.length) {
            throw new Fault(
                "Portlets in master xml file inconsistent with portlets " + 
                "in source for test:" +
                uniqueTestName);
        }
        for ( int i = 0; i < portletInfos.length; i++) {
            if ((!portletInfos[i].getPortletName().equals(
                                fromXMLFile[i].getPortletName()))
             || (!portletInfos[i].getPortletApplication().equals(
                                fromXMLFile[i].getPortletApplication()))) {
                throw new Fault(
                    "Portlets in master xml file inconsistent " + 
                    " with portlets in source for test:" +
                    uniqueTestName);
            }
        }
    }

        
    private void writePortletsToXMLFile(String uniqueTestName, 
                            TSPortletInfo[] portletInfos) throws Fault {
        
        try {
            FileWriter file = new FileWriter("/tmp/masterXMLFile.xml", true);
            StringBuffer buf = new StringBuffer();
            buf.append("   <test_case>\n");
            buf.append("       <test_name>" + uniqueTestName + "</test_name>\n");
            for ( int i = 0; i < portletInfos.length; i++) {
                buf.append("       <test_portlet>\n");
                buf.append("           <portlet_name>" + portletInfos[i].getPortletName() + "</portlet_name>\n");
                buf.append("           <app_name>" + portletInfos[i].getPortletApplication() + "</app_name>\n");
                buf.append("       </test_portlet>\n");
            }
            buf.append("   </test_case>\n");
            file.write(buf.toString());
            file.close();
        }
        catch(IOException ex) {
            throw new Fault(ex.getMessage(), ex);
        }
    }



    public static void main(String args[]) throws Exception {
        PortletTCKTestCases x = PortletTCKTestCases.getInstance();
        System.out.println(x.toString());
    }
        
}
