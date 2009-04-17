/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.common.util; 

import com.sun.ts.tests.portlet.common.util.tags.PortletTCKCustomTag; 

/**
 * This class provides a way to put test results calculated in
 * portlets/servlets in the HTTP response body 
 * These test results are delimited by portlet custom tags.
 */
public class ResultWriter {

    static public boolean PASS = true;
    static public boolean FAIL = false;

    static private String LINE_BREAK="\n    ";

    private String _testName = null;
    private PortletTCKCustomTag _detailTag = 
                    new PortletTCKCustomTag("print-detail");
    private PortletTCKCustomTag _statusTag = 
                    new PortletTCKCustomTag("print-status");


    /**
     * Constructor
     * 
     * @param testName name of the test
     */
    public ResultWriter(String testName) {
        super();
        _testName = testName;
    }

    /**
     * Sets the success/failure status for the test.
     * 
     * @param status
     */
    public void setStatus(boolean status) {
        _statusTag.setTagContent(getStatusString(status));
    }

    /**
     * Let's the tests add details about the test run.
     * 
     * @param detailLine line containing detail information about the test run.
     */
    public void addDetail(String detailLine) {
        _detailTag.appendTagContent(LINE_BREAK + detailLine );
    }
    /**
     * Returns the string to be embedded in the Http Response.
     */

    public String toString() {
        _detailTag.appendTagContent("\n");
        return  _statusTag.toString() + _detailTag.toString();
    }

    /**
     * Returns the string stating the status of the test.
     * 
     * @param status
     */
    private String getStatusString(boolean status) {
        if (status) {
            return getPassedString(_testName);
        }
        else {
            return getFailedString(_testName);
        }
    }

    /**
     * Returns the fail string. Also used on the client side to
     * provide failure critieria for a test.
     * 
     * @param testName
     */
    static public String getFailedString(String testName) {
        return testName + " Test FAILED."; 
    }

    /**
     * Returns the pass string. Also used on the client side, for 
     * specifying the success criteria.
     * 
     * @param testName
     */
    static public String getPassedString(String testName) {

        return testName + " Test PASSED."; 
    }

}
