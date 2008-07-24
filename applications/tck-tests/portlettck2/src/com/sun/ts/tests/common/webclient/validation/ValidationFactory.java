/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * @(#)ValidationFactory.java	1.8 03/05/16
 */

/*
 * @(#)ValidationFactory.java	1.8 05/16/03
 */

package com.sun.ts.tests.common.webclient.validation;

import com.sun.ts.lib.util.TestUtil;

/**
 * Returns a ValidationStrategy instance used to validate a response
 * against a particular WebTestCase
 *
 * @author Ryan Lubke
 * @version 1.8
 */
public class ValidationFactory {


    /**
     * Private constructor as all interaction with the
     * class is through the getInstance() method.
     */
    private ValidationFactory() {
    }

/*
 * public methods
 * ========================================================================
 */

    /**
     * Returns a ValidationStrategy instance based on the available
     * factory types.
     *
     * @param validator Validator instance to obtain
     * @return a ValidationStrategy instance or null
     *         if the instance could not be obtained.
     */
    public static ValidationStrategy getInstance(String validator) {
	try {
	    Object o = Thread.currentThread().getContextClassLoader().
		           loadClass(validator).newInstance();
	    if (o instanceof ValidationStrategy) {
		return (ValidationStrategy) o;
	    }
	} catch (Throwable t) {
	    TestUtil.logMsg("[ValidationFactory] Unable to obtain " +
			    "ValidationStrategy instance: " + validator);
	}
	return null;
    }
}
