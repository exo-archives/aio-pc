/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * @(#)TestSequence.java	1.5 03/05/16
 */

/*
 * @(#)TestSequence.java	1.5 05/16/03
 */

package com.sun.ts.tests.common.webclient;

import com.sun.ts.lib.util.TestUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * This class represents a logic sequence for executing a series
 * of test cases in a specific order.  In this case, the execution
 * order will be the same order that the test cases were added to
 * the sequence.
 *
 * The <code>TestSequence</code> has the added benefit of managing
 * state between the test invocations.
 */
public class TestSequence implements TestCase {

    private Map _testMap = null;
    private List _testNameList = null;
    private String _name = "DEFAULT";
    private boolean _managed = false;

    /** Creates a new instance of TestSequence */
    public TestSequence() {
        _testMap = new HashMap();
        _testNameList = new ArrayList();
    }

/*
 * public methods
 * ========================================================================
 */

    /**
     * Executes the test sequence.
     *
     * @throws TestFailureException if any test in the sequence fails.
     */
    public void execute() throws TestFailureException {

        TestUtil.logTrace("[TestSequence] Beginning execution of sequence '" +
                          _name + "' containing '" + _testNameList.size() +
                          "' test entities.");

        TestCase cse = null;
        for (int i = 0, size = _testNameList.size(); i < size; i++ ) {
            String testName = (String) _testNameList.get(i);
            TestUtil.logTrace("[TestSequence] Executing test case: " +
                              testName);
            cse = (TestCase) _testMap.get(testName);
            if (_managed) {
                cse.setState(cse.getState());
            }
            cse.execute();
            TestUtil.logTrace("[TestSequence] Test case: " + testName +
                              "complete.");
        }
        TestUtil.logTrace("[TestSequence] Sequence complete!");
    }

    /**
     * <code>enableStateManagement</code>, when enabled, will
     * cause the test sequence to manage state between test case
     * invocations.  By default, a test sequence will not
     * manage state.
     *
     * @param value a value of true enables session management.
     */
    public void enableStateManagement(boolean value) {
        _managed = value;
    }

    /**
     * Returns a value indicating whether state management is enabled
     * or not.
     *
     * @return boolean value indicating state management status
     */
    public boolean isStateManagementEnabled() {
        return _managed;
    }

    /**
     * Adds a test case to the sequence denoted by a unique identifier.
     *
     * @param identifier for this test case
     * @param cs the test case
     */
    public void addTestCase(String identifier, TestCase cs) {
        _testMap.put(identifier, cs);
        _testNameList.add(identifier);
    }

    /**
     * Removes a test case from the sequence.
     *
     * @param identifier
     */
    public void removeTestCase(String identifier) {
        _testMap.remove(identifier);
        _testNameList.remove(identifier);
    }

    /**
     * Sets the name of this TestSequence.  If not set, the
     * default value is "DEFAULT".
     *
     * @param name
     */
    public void setName(String name) {
        _name = name;
    }

    /**
     * Returns the name of this TestSequence.
     *
     * @return sequence name
     */
    public String getName() {
        return _name;
    }

    /**
     * Sets the initial state for the test sequence
     * to use when invoking test cases.
     *
     * @param state the initial state
     */
    public void setState(Object state) {
    }

    /**
     * Returns the state of the sequence.  Note: This
     * value can differ depending on when it has been
     * called in relation to when execute has been called.
     *
     * @return state of the sequence
     */
    public Object getState() {
        throw new UnsupportedOperationException();
    }

}
