/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletSessionUtil;

import javax.portlet.PortletSessionUtil;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * Class implementing HttpSessionBindingListener.
 * It is used to excercise PortletSessionUtil.
 * Instance of this class is bound to the portlet
 * session, which triggers the invocation
 * of its valueBound method. This method
 * tests for expected values from 
 * PortletSessionUtil methods and computes
 * success and failure of the test.
 */

public final class SampleBindingListener
    implements HttpSessionBindingListener {


    /**
     * statics that are used as hints
     * on what PortletSessionUtil needs
     * to be tested.
     */

    static public int TEST_DECODE_ATTRIBUTE = 0;
    static public int TEST_DECODE_SCOPE = 1;

    private String _testName;
    private String _attributeBound;
    private int _scope;
    private ResultWriter _resultWriter = null;
    private int _whatToTest = 0;

    /**
     * Constructor takes all information
     * needed to run the test.
     * 
     * @param whatToTest Takes either TEST_DECODE_ATTRIBUTE or TEST_DECODE_SCOPE
     * @param testName Name of the test, used for writing results.
     * @param attributeBound Name of the string used to bind instance of this class to the 
     * portlet session.
     * @param scope scope used to bind an instance of the class to portlet session
     */

    public SampleBindingListener(int whatToTest,
                                String testName, 
                                String attributeBound,
                                            int scope) {
        _testName = testName;
        _attributeBound = attributeBound;
        _scope = scope;
        _whatToTest = whatToTest;
        if((_whatToTest != TEST_DECODE_ATTRIBUTE) && 
                    (_whatToTest !=TEST_DECODE_SCOPE )) {
            throw new IllegalArgumentException("First parameter must be" 
                + " SampleBindingListener.TEST_DECODE_ATTRIBUTE" 
                + " or SampleBindingListener.TEST_DECODE_SCOPE");
        } 
        
    } 

    /**
     * Implementation of HttpSessionListener method.
     * 
     * @param event
     */
    public void valueBound( HttpSessionBindingEvent event ) {
        computeResults(event);

    }

    /**
     * Implementation of a HttpSessionListenerMethod
     * 
     * @param event
     */
    public void valueUnbound( HttpSessionBindingEvent event ) {
    }



    /**
     * Called by the Test Portlet to get the results back
     */
    public ResultWriter getResultWriter() {
        if(_resultWriter == null) {
            _resultWriter = new ResultWriter(_testName);
            _resultWriter.setStatus(ResultWriter.FAIL);
            _resultWriter.addDetail("HttpSessionBindingListener methods not called.");
        }
        return _resultWriter;
        
    }


    /**
     * Internal method that calls the PortletSessionUtil
     * methods to check for expected values and set the
     * result string accordingly.
     * 
     * @param event
     */
    private void computeResults( HttpSessionBindingEvent event ) {

        _resultWriter = new ResultWriter(_testName);

        int reportedScope = 
                PortletSessionUtil.decodeScope(event.getName());
        String reportedAttribute = 
                PortletSessionUtil.decodeAttributeName(event.getName());

        /*
         * test decodeAttributeName functionality
         */
        if (_whatToTest == TEST_DECODE_ATTRIBUTE) {
            if (!_attributeBound.equals(reportedAttribute)) {
                _resultWriter.setStatus(ResultWriter.FAIL);
                _resultWriter.addDetail(
                    "PortletSessionUtil.decodeAttributeName returned different value than expected.");
                _resultWriter.addDetail("Expected:" + _attributeBound);
                _resultWriter.addDetail("Returned:" + reportedAttribute);
            }
            else {
                _resultWriter.setStatus(ResultWriter.PASS);
            }
        }

        /*
         * test decodeScope functionality
         */
        if (_whatToTest == TEST_DECODE_SCOPE) {
            if(_scope != reportedScope) {
                _resultWriter.setStatus(ResultWriter.FAIL);
                _resultWriter.addDetail("PortletSessionUtil.decodeScope is different than expected.");
                _resultWriter.addDetail("Expected:" + _scope);
                _resultWriter.addDetail("Returned:" + reportedScope);
            }
            else {
                _resultWriter.setStatus(ResultWriter.PASS);
            }
        }
    }
}
