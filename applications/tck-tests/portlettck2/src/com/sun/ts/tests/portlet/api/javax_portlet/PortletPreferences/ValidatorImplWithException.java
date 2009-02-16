/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package com.sun.ts.tests.portlet.api.javax_portlet.PortletPreferences;

import javax.portlet.PortletPreferences;
import javax.portlet.PreferencesValidator;
import javax.portlet.ValidatorException;

/**
 *  This class implements the PreferencesValidator interface. In the validate()
 *  method ValidatorException() is thrown.
 */

public class ValidatorImplWithException implements PreferencesValidator {

    public void validate (PortletPreferences preferences) throws ValidatorException {
        throw (new ValidatorException("Invalid settings", null));
    }
}
