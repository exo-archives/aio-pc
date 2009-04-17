/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.services.portletcontainer.test.portlet;

import javax.portlet.PreferencesValidator;
import javax.portlet.PortletPreferences;
import javax.portlet.ValidatorException;

/**
 * Created by The eXo Platform SAS
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Jul 27, 2003
 * Time: 11:23:49 PM
 */

/**
 * Test preferences validator.
 */
public class SimplePreferencesValidator implements PreferencesValidator {

  /**
   * Does nothing.
   *
   * @param portletPreferences preferences to validate
   * @throws ValidatorException throws it when validation fails
   * @see javax.portlet.PreferencesValidator#validate(javax.portlet.PortletPreferences)
   */
  public void validate(PortletPreferences portletPreferences) throws ValidatorException { }

}
