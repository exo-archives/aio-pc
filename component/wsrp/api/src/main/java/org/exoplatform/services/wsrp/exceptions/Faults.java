/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 *  
 * Created on 14 janv. 2004
 */
package org.exoplatform.services.wsrp.exceptions;

/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public class Faults {
  //producer
  public static final String ACCESS_DENIED_FAULT = "AccessDenied";
  public static final String INCONSISTENT_PARAMETERS_FAULT = "InconsistenParameters";
  public static final String INVALID_REGISTRATION_FAULT = "InvalidRegistration";
  public static final String INVALID_COOKIE_FAULT = "InvalidCookie";
  public static final String INVALID_HANDLE_FAULT = "InvalidHandle";
  public static final String INVALID_SESSION_FAULT = "InvalidSession";
  public static final String INVALID_USER_CATEGORY_FAULT = "InvalidUserCategory";
  public static final String MISSING_PARAMETERS_FAULT = "MissingParameters";
  public static final String OPERATION_FAILED_FAULT = "OperationFailed";
  public static final String PORTLET_STATE_CHANGE_REQUIRED_FAULT = "PortletStateChangeRequired";
  public static final String UNSUPPORTED_LOCALE_FAULT = "UnsupportedLocale";
  public static final String UNSUPPORTED_MIME_TYPE_FAULT = "UnsupportedMimeType";
  public static final String UNSUPPORTED_MODE_FAULT = "UnsupportedMode";
  public static final String UNSUPPORTED_WINDOW_STATE_FAULT = "UnsupportedWindowState";

  //consumer
  public static final String PREFERENCES_STORING_IMPOSSIBLE = "canNotStorePreferences";
  public static final String UNKNOWN_PORTLET_DESCRIPTION = "unknownPortletDescription";
  public static final String UNKNOWN_PRODUCER = "unknownProducer";  
}
