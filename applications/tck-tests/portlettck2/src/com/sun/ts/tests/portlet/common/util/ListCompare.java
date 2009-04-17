/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.common.util;

import java.lang.StringBuffer;
import java.util.Iterator;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;


public class ListCompare {


    public static int ALL_ELEMENTS_MATCH = 0;
    public static int ALL_ELEMENTS_AND_ORDER_MATCH = 1;
    public static int SUBSET_MATCH = 2;
    private StringBuffer _misMatchReason = new StringBuffer();
    private boolean _misMatch = false;
    String[] _expectedValues;
    String[] _actualValues;
    String _firstValue;
    int _matchType;


    public ListCompare( String[] expectedValues, 
                        Iterator actualValues, 
                        String firstValue, 
                        int matchType) {

        
        _expectedValues = expectedValues;
        if ( actualValues != null) {
            ArrayList list = new ArrayList();
            try {
                while ( actualValues.hasNext()) {
                    list.add((String) actualValues.next());
                }
                _actualValues = new String[list.size()];
                list.toArray(_actualValues);
            }
            catch(ClassCastException ex) {
                _misMatch = true;
                addMisMatchDetail("List doesn't contain String elements");
            }

        }
        _firstValue = firstValue;
        _matchType = matchType;
        computeMisMatchStatus();

    }

    public ListCompare( Iterator expectedValues, 
                        Iterator actualValues, 
                        String firstValue, 
                        int matchType) {

        if ( expectedValues != null) {
            ArrayList list = new ArrayList();
            try {
                while ( expectedValues.hasNext()) {
                    list.add((String) expectedValues.next());
                }
                _expectedValues = new String[list.size()];
                list.toArray(_expectedValues);
            }
            catch(ClassCastException ex) {
                _misMatch = true;
                addMisMatchDetail("List doesn't contain String elements");
            }

        }
        
        if ( actualValues != null) {
            ArrayList list = new ArrayList();
            try {
                while ( actualValues.hasNext()) {
                    list.add((String) actualValues.next());
                }
                _actualValues = new String[list.size()];
                list.toArray(_actualValues);
            }
            catch(ClassCastException ex) {
                _misMatch = true;
                addMisMatchDetail("List doesn't contain String elements");
            }

        }
        _firstValue = firstValue;
        _matchType = matchType;
        computeMisMatchStatus();
    }
        
    public ListCompare( String[] expectedValues, 
                        String[] actualValues, 
                        String firstValue, 
                        int matchType) {

        
        _expectedValues = expectedValues;
        _actualValues = actualValues;
        _firstValue = firstValue;
        _matchType = matchType;
        computeMisMatchStatus();

    }

    public ListCompare( String[] expectedValues, 
                        Enumeration actualValues, 
                        String firstValue, 
                        int matchType) {

        
        _expectedValues = expectedValues;
        if ( actualValues != null) {
            ArrayList list = new ArrayList();
            try {
                while ( actualValues.hasMoreElements()) {
                    list.add((String) actualValues.nextElement());
                }
                _actualValues = new String[list.size()];
                list.toArray(_actualValues);
            }
            catch(ClassCastException ex) {
                _misMatch = true;
                addMisMatchDetail("List doesn't contain String elements");
            }

        }
        _firstValue = firstValue;
        _matchType = matchType;
        computeMisMatchStatus();

    }

    public ListCompare( Enumeration expectedValues, 
                        Enumeration actualValues, 
                        String firstValue, 
                        int matchType) {

        if ( expectedValues != null) {
            ArrayList list = new ArrayList();
            try {
                while ( expectedValues.hasMoreElements()) {
                    list.add((String) expectedValues.nextElement());
                }
                _expectedValues = new String[list.size()];
                list.toArray(_expectedValues);
            }
            catch(ClassCastException ex) {
                _misMatch = true;
                addMisMatchDetail("List doesn't contain String elements");
            }

        }
        
        if ( actualValues != null) {
            ArrayList list = new ArrayList();
            try {
                while ( actualValues.hasMoreElements()) {
                    list.add((String) actualValues.nextElement());
                }
                _actualValues = new String[list.size()];
                list.toArray(_actualValues);
            }
            catch(ClassCastException ex) {
                _misMatch = true;
                addMisMatchDetail("List doesn't contain String elements");
            }

        }
        _firstValue = firstValue;
        _matchType = matchType;
        computeMisMatchStatus();
    }

    public String getMisMatchReason() {
        return _misMatchReason.toString();
    }
    public boolean misMatch() {
        return _misMatch; 
    }

    private void computeMisMatchStatus() {

        if (_misMatch) {
            return;
        }

        if ( _actualValues == null && _expectedValues == null) {
            return;
        }



        /*
        ** Check for null for actual values
        */

        if ( _actualValues == null ) {
            _misMatch = true;
            addMisMatchDetail("Actual list returned is null. Expected list is not");
        }
        /*
        ** Check for null for expected values
        */
        else if (_expectedValues == null) {
            _misMatch = true;
            addMisMatchDetail("Expected list is null. Actual list is not null.");
        }
            
        /*
        ** Check for length
        */

        else if (( _matchType != SUBSET_MATCH) && 
                _actualValues.length != _expectedValues.length) {
            _misMatch = true;
            addMisMatchDetail("Expected size of the list:" +   
                            _expectedValues.length);
            addMisMatchDetail("Actual size of the list:" + 
                            _actualValues.length);
            addMisMatchDetail("Expected Values:");
            for ( int i = 0; i < _expectedValues.length; i++) {
                addMisMatchDetail("    " + _expectedValues[i]);
            }
            addMisMatchDetail("Actual Values:");
            for ( int i = 0; i < _actualValues.length; i++) {
                addMisMatchDetail("    " + _actualValues[i]);
            }
        }

        /*
        ** Check for first values
        */

        else if ((_firstValue != null) && 
                    !_firstValue.equals(_actualValues[0])) {
            _misMatch = true;
            addMisMatchDetail("First value in the list is not same as expected.");
            addMisMatchDetail("Expected First Value:" + _firstValue);
            addMisMatchDetail("Actual First Value:" + _actualValues[0]);
        }

        /*
        ** Check for complete array ( either with order match or without)
        */

        else {
            if ( _matchType == ALL_ELEMENTS_AND_ORDER_MATCH ) {
                if (!Arrays.equals(_expectedValues, _actualValues)) {
                    _misMatch=true;
                }
            }
            else if (_matchType == ALL_ELEMENTS_MATCH) {
                List actualList = Arrays.asList(_actualValues);
                List expectedList = Arrays.asList(_expectedValues);
                if (!actualList.containsAll(expectedList) || 
                        !expectedList.containsAll(actualList)) {
                        _misMatch = true;
                }
            }
            else if (_matchType == SUBSET_MATCH) {
                List actualList = Arrays.asList(_actualValues);
                List expectedList = Arrays.asList(_expectedValues);
                if (!actualList.containsAll(expectedList)) { 
                        _misMatch = true;
                }
            }
            else {
                throw new IllegalArgumentException(
                        "match type parameter mush be 0,1 or 2");
            }

            // Print the values if mismatch

            if (_misMatch) {
                if (_matchType == SUBSET_MATCH) {
                    addMisMatchDetail("Actual Values don't include expected:");
                }
                else {
                    addMisMatchDetail("Actual Values not same as the expected:");
                }
                addMisMatchDetail("Expected Values:");
                for ( int i = 0; i < _expectedValues.length; i++) {
                    addMisMatchDetail("    " + _expectedValues[i]);
                }
                addMisMatchDetail("Actual Values:");
                for ( int i = 0; i < _actualValues.length; i++) {
                    addMisMatchDetail("    " + _actualValues[i]);
                }
                    
            }
        }
    }

    private void addMisMatchDetail(String str) {
        _misMatchReason.append("\n" + str);
    }

}
