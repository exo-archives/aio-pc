/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletSessionDistributableapp;

import java.io.Serializable;

/* 
 * Class that implements the Serializable interface. 
 */
public class SampleSerializable implements Serializable {

    int num = 0;

    SampleSerializable() {
    }

    SampleSerializable(int i) {
        this.num = i;
    }

    public int getNum() {
        return num;
    }
}
