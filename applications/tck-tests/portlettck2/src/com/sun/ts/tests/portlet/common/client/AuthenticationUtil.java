/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.common.client;

import java.util.ArrayList;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.common.webclient.http.HttpRequest;
import com.sun.ts.lib.porting.TSPortletAuthCookieInterface;
import org.apache.commons.httpclient.HttpException;
import com.sun.ts.tests.common.webclient.TestFailureException;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.cookie.CookieSpec;
import org.apache.commons.httpclient.cookie.*;
import org.apache.commons.httpclient.Cookie;




 /**
  * This class is responsible for setting up authentication headers or cookies
  * if the current test requires authentication support. Vendor would put 
  * such tests in the <TS_HOME>/bin/authTestList.txt. The name of the test 
  * must be the full test  name as specified in the master file containing 
  * all tests portletTCKTestCases.xml.
  */
 public class AuthenticationUtil {


     static final String AUTH_CONFIG_TYPE_KEY = "authConfigType";
     static final String AUTH_USER_KEY = "authuser";
     static final String AUTH_PASSWORD_KEY = "authpassword";
     static final String AUTH_AUTH_COOKIE_CLASS_KEY = 
                        "porting.ts.portletAuthCookie.class";

     static final String AUTH_CONFIG_TYPE_NONE = "0";
     static final String AUTH_CONFIG_TYPE_BASIC_HEADER = "1";
     static final String AUTH_CONFIG_TYPE_COOKIE_CLASS = "2";



     /**
      * This method checks the authentication related configuration properties
      * and sets the headers/cookies accordingly on the request if 
      * authentication is enabled for the test.
      * 
      * @param request request to be sent
      * @param testName Unique name of the test.
      * @param isFirstRequest True if it is the first request for this test. 
      *            This flag is used to set the cookies, 
      *         which are set only on the first request.
      * @exception com.sun.ts.tests.common.webclient.TestFailureException
      */
     public static void setupAuth(HttpRequest request, 
                                  String host,
                                  int port,
                                  String testName,
                                  boolean isFirstRequest) 
                                  throws TestFailureException{

        String authConfigType = TestUtil.getProperty(
                                    AUTH_CONFIG_TYPE_KEY);

        if ((authConfigType.equals(AUTH_CONFIG_TYPE_NONE)) || 
                                !isAuthNeededForTest(testName)) {

            DetailBuffer.recordAuthenticationInfo("none");
            return;
        }
        else if ( authConfigType.equals(AUTH_CONFIG_TYPE_BASIC_HEADER)) {
            DetailBuffer.recordAuthenticationInfo("Basic");
            setupBasicAuthHeader(request);
        }
        else if ( authConfigType.equals(AUTH_CONFIG_TYPE_COOKIE_CLASS)) {
            if ( isFirstRequest) {
                DetailBuffer.recordAuthenticationInfo("Vendor Implementation");
                setupAuthCookie(request, host, port);
            }
        }
        else {
            throw new TestFailureException("Value of property " 
                + AUTH_CONFIG_TYPE_KEY 
                +  " has to be 0, 1 or 2.");
        }
     } 
     
     /**
      * If the test is configured to use the Basic Authentication, this 
      * method sets the basic authentication in the HttpRequest.
      * 
      * @param request request to be sent
      * @exception com.sun.ts.tests.common.webclient.TestFailureException
      */
     private static void setupBasicAuthHeader(HttpRequest request) 
                                                    throws TestFailureException{ 

            request.setAuthenticationCredentials(getUser(), 
                                     getPassword(),
                                     HttpRequest.BASIC_AUTHENTICATION, 
                                     null);
     }

     /**
      * If the test is configured to use a vendor implementation of
      * java interface TSPortletAuthCookieInterface, it calls the method
      * on the implementation to get a cookie that is set on the request.
      * 
      * @param request request to be sent.
      * @exception com.sun.ts.tests.common.webclient.TestFailureException
      */
     private static void setupAuthCookie(HttpRequest request,
                                        String host,
                                        int port) 
                                        throws TestFailureException{ 
        
        try
        {
            // create and initialize a new instance of 
            // porting.ts.portletContainerURL.class

            Class c = Class.forName(TestUtil.getProperty(
                    AUTH_AUTH_COOKIE_CLASS_KEY));
            TSPortletAuthCookieInterface tsPortletAuthCookie = 
                (TSPortletAuthCookieInterface)c.newInstance();

            String authCookie = tsPortletAuthCookie.authenticate(
                                    getUser(), 
                                    getPassword());

            


            if ( authCookie != null && authCookie.length() > 0) {
                RFC2109Spec cookieSpec = (RFC2109Spec)
                    CookiePolicy.getSpecByPolicy(CookiePolicy.RFC2109);
                Cookie[] cookies = cookieSpec.parse(
                            host, 
                            port,
                            request.getRequestPath(), 
                            request.isSecureRequest(), 
                            authCookie);

                for (int i = 0; i < cookies.length; i++) {
                    Cookie cookie = cookies[i];
                    cookie.setDomainAttributeSpecified(true);
                    cookie.setPathAttributeSpecified(true);
                    final CookieSpec validator
                        = CookiePolicy.getSpecByVersion(cookie.getVersion());
                    validator.validate(host, 
                                        port, 
                                        request.getRequestPath(), 
                                        request.isSecureRequest(),
                                        cookie);
                    request.getState().addCookie(cookie);
                    DetailBuffer.recordCookieAuthenticationInfo(
                                    cookieSpec.formatCookie(cookie));
                }

            }
        } catch(Exception e) {
            e.printStackTrace();
            throw new TestFailureException("[AuthenticationUtil] " 
                            + "failed! " + e.toString()); 
        }
        return ;
     }

     /**
      * Get user name configured
      * 
      * @exception com.sun.ts.tests.common.webclient.TestFailureException
      */
     private static String getUser() throws TestFailureException{
        String authUser = TestUtil.getProperty(
                                    AUTH_USER_KEY);
        return authUser;
     }

     /**
      * Get password configured.
      * 
      * @exception com.sun.ts.tests.common.webclient.TestFailureException
      */
     private static String getPassword() throws TestFailureException{
        String authPassword = TestUtil.getProperty(
                                    AUTH_PASSWORD_KEY);
        return authPassword;
     }

     /**
      * Checks <TS_HOME/bin/authTestList.txt to check 
      * whether current test needs the authentication.
      * 
      * @param testName Unique name of the test.
      * @exception com.sun.ts.tests.common.webclient.TestFailureException
      */
     private static boolean isAuthNeededForTest(String testName) 
                                            throws TestFailureException{
        return AuthTestListProcessor.isAuthNeededForTest(testName);
     }

 }
        

/**
 * Class reading the file containing the name of tests that need authentication
 * header/cookies set in the request.
 */
class AuthTestListProcessor {

     static final String TS_HOME = "ts_home";
    public static String AUTH_TEST_FILE = TestUtil.getProperty(TS_HOME)
                                 + File.separator
                                 + "bin"
                                 + File.separator
                                 + "authTestList.txt";


    public static boolean isAuthNeededForTest(String testname) 
                                throws TestFailureException {

        ArrayList testNameList = readAuthTestList();

        return testNameList.contains(testname);        
    }   

    private static ArrayList readAuthTestList() throws TestFailureException {
        ArrayList testNameList = new ArrayList(); 

        BufferedReader d = null;
        try {
            d = new BufferedReader (new FileReader (AUTH_TEST_FILE));
            String line;
            while ((line = d.readLine()) != null) {
                line = line.trim();
                if (line.length() > 0 && !line.startsWith ("#")) {
                    String entry = new String (line);
                    testNameList.add(entry.trim());
                }
            }
            d.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new TestFailureException(
                "File containing test names needing authentication " 
                + AUTH_TEST_FILE 
                +  " not found.");
        } catch (IOException e) {
            e.printStackTrace();
            throw new TestFailureException(
                "Error reading file containing tests needing authentication " 
                + AUTH_TEST_FILE );
        }
        return testNameList;
    }

}
