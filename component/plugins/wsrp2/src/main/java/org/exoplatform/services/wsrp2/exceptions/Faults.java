/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
 
package org.exoplatform.services.wsrp2.exceptions;

/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public class Faults {
  //producer
  public static final String ACCESS_DENIED_FAULT = "AccessDenied";
  public static final String EXPORT_BY_VALUE_NOT_SUPPORTED_FAULT = "ExportByValueNotSupported";
  public static final String EXPORT_NO_LONGER_VALID_FAULT = "ExportNoLongerValid";
  public static final String INCONSISTENT_PARAMETERS_FAULT = "InconsistenParameters";
  public static final String INVALID_REGISTRATION_FAULT = "InvalidRegistration";
  public static final String INVALID_COOKIE_FAULT = "InvalidCookie";
  public static final String INVALID_HANDLE_FAULT = "InvalidHandle";
  public static final String INVALID_SESSION_FAULT = "InvalidSession";
  public static final String INVALID_USER_CATEGORY_FAULT = "InvalidUserCategory";
  public static final String MODIFY_REGISTRATION_REQUIRED_FAULT = "ModifyRegistrationRequired";
  public static final String MISSING_PARAMETERS_FAULT = "MissingParameters";
  public static final String OPERATION_FAILED_FAULT = "OperationFailed";
  public static final String OPERATION_NOT_SUPPORTED_FAULT = "OperationNotSupported";
  public static final String RESOURCE_SUSPENDED_FAULT = "ResourceSuspended";
  
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
