/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.common.util;

import java.lang.StringBuffer;
import java.util.Iterator;
import java.util.Map;


/**
 * A class to compare two maps that hold String[] as values.
 */

public class MapCompare {


    private StringBuffer _misMatchReason = new StringBuffer();
    private boolean _misMatch = false;


    public MapCompare( Map expectedMap, Map actualMap) {


        if ( actualMap == null && expectedMap == null) {
            return;
        }
        else if ( actualMap == null ) {
            _misMatch = true;
            addMisMatchDetail(
                "Actual Map returned is null. Expected Map is not");
            return;
        }
        else if (expectedMap == null) {
            _misMatch = true;
            addMisMatchDetail(
                "Expected Map is null. Actual Map is not null.");
            return;
        }

        // Compare the key set
        Iterator expectedKeys = expectedMap.keySet().iterator();
        Iterator actualKeys = actualMap.keySet().iterator();


        ListCompare keyCompare = new ListCompare(expectedKeys,
                                        actualKeys,
                                        null,
                                        ListCompare.ALL_ELEMENTS_MATCH);
        if (keyCompare.misMatch()) {
            _misMatch = true;
            addMisMatchDetail("Keys for expected and actual map don't match");
            addMisMatchDetail(keyCompare.getMisMatchReason());

        }

        // get the iterator again!
        // because ListCompare consumed it.

        expectedKeys = expectedMap.keySet().iterator();
        actualKeys = actualMap.keySet().iterator();

        while(expectedKeys.hasNext()) {
            String key = (String)expectedKeys.next();
            String[] expectedValues = (String[])expectedMap.get(key);
            try {
                String[] actualValues = (String[])actualMap.get(key);
                ListCompare valueCompare = new ListCompare(
                                    expectedValues,
                                    actualValues,
                                    null,
                                    ListCompare.ALL_ELEMENTS_AND_ORDER_MATCH);
                if ( valueCompare.misMatch()) {
                    _misMatch = true;
                    addMisMatchDetail("Values different for key:" + key);
                    addMisMatchDetail(valueCompare.getMisMatchReason());
                    return;
                }
            }
            catch(ClassCastException ex) {
                    _misMatch = true;
                    addMisMatchDetail("Value for key:"
                    + key
                    + " is not a String[]");
                return;
            }
        }

    }

    public String getMisMatchReason() {
        return _misMatchReason.toString();
    }
    public boolean misMatch() {
        return _misMatch;
    }

    private void addMisMatchDetail(String str) {
        _misMatchReason.append("\n" + str);
    }

}
