/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * @(#)ValidationStrategy.java	1.6 03/05/16
 */

/*
 * @(#)ValidationStrategy.java	1.6 05/16/03
 */

package com.sun.ts.tests.common.webclient.validation;

import com.sun.ts.tests.common.webclient.WebTestCase;

/**
 * A ValidationStrategy is used to compare a server response
 * with a configured test case.  How this validation is performed
 * is up to the concrete implementation.
 */
public interface ValidationStrategy {
    public boolean validate(WebTestCase testCase);
}
