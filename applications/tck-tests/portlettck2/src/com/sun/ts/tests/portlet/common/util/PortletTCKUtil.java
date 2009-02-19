/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.common.util;

import javax.portlet.Portlet;
import javax.servlet.Servlet;

/**
 * This class provides miscellaneous utilities for Portlet TCK test
 * portlets.  For example, it provides a static method to extract the
 * test name corresponding to a test porlet or servlet.
 */
public class PortletTCKUtil {
    /**
     * Returns the test name corresponding to a test portlet or
     * servlet.  For an example test named FooTest, there is at least
     * one test portlet, named FooTestPortlet, associated with it.  If
     * more than one test portlets are associated with the test, the
     * other are named FooTest_N_Portlet where N is a positive integer
     * starting from 1.  If FooTest requires a test servlet, it's
     * named FooTestServlet.  Similar to the case of the test
     * portlets, if there are more than one test servlets, the other
     * are named FooTest_N_Servlet.
     *
     * @param testPortlet a portlet or servlet object instance whose
     *                    corresponding test name is to be returned.
     * @exception ClassCastException if the given object is neither a
     *                               portlet nor a servlet, or if its
     *                               class name doesn't end with
     *                               "Portlet" or "Servlet".
     */
    public static String getTestName(Object testPortlet)
        throws ClassCastException {

        if (!(testPortlet instanceof Portlet)
            && !(testPortlet instanceof Servlet)) {

            // It's neither a portlet nor a servlet!
            throw new ClassCastException("Not a portlet or servlet object!");
        }

        // Get the fully-qualified name of the test portlet/servlet class.
        String className = testPortlet.getClass().getName();

        // Strip off the package at the beginning of the class name.
        className = className.substring(className.lastIndexOf(".") + 1);

        // The name of a test portlet/servlet must end with "Portlet"
        // or "Servlet".
        if (!className.endsWith("Portlet") && !className.endsWith("Servlet")) {
            throw new ClassCastException("Name doesn't end with Portlet or Servlet!");
        }

        // Strip off "Portlet" or "Servlet" at the end of the class name.
        String testName = className.substring(0, className.length() - 7);

        // Strip off "_N_" if it isn't the first test portlet or servlet.
        if (testName.endsWith("_")) {
            // Strip off the second "_" first.
            testName = testName.substring(0, testName.length() - 1);

            // Then strip off "_N".
            testName = testName.substring(0, testName.lastIndexOf("_"));
        }

        return testName;
    }
}
