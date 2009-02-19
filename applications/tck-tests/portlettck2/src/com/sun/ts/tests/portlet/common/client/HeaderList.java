/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.common.client;

import java.util.ArrayList;
import java.util.*;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileInputStream;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.common.webclient.TestFailureException;



 /**
  * This class is responsible for reading Http headers 
  * that might be set by the vendors in the bin/headerList.properties
  * file.  These headers are sent out  
  * in every request that goes out to the server.
  * The syntax in the file should follow the java Properties.
  * Framework writes some of the headers e.g. Host, User-Agent
  * Any such headers specified in this file will be overwritten 
  * by the framework.
  * Donot specify any Cookie headers in header file.
  */

 public class HeaderList{

    static final String TS_HOME = "ts_home";
    public static String HEADER_FILE = TestUtil.getProperty(TS_HOME)
                                 + File.separator
                                 + "bin"
                                 + File.separator
                                 + "headerList.properties";


    public static Properties readHeaderProperties() 
                                throws TestFailureException {
        Properties headers = new Properties();
        try {
            headers.load( new FileInputStream(HEADER_FILE));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new TestFailureException(
                "File containing vendor headers " 
                + HEADER_FILE 
                +  " not found.");
        } catch (IOException e) {
            e.printStackTrace();
            throw new TestFailureException(
                "Error reading file containing headers " 
                + HEADER_FILE );
        }
        return headers;
    }

}
