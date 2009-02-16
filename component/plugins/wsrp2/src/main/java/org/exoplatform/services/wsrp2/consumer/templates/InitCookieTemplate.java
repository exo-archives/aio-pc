/*
 * Copyright (C) 2003-2009 eXo Platform SAS.
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

package org.exoplatform.services.wsrp2.consumer.templates;

import org.exoplatform.services.wsrp2.consumer.InitCookieInfo;

/**
 * User: Benjamin Mestrallet Date: 11 mai 2004
 */
public abstract class InitCookieTemplate implements InitCookieInfo {

  private boolean initCookieRequired;

  private boolean initCookieDone;

  public InitCookieTemplate() {
  }
  
  public boolean isInitCookieRequired() {
    return initCookieRequired;
  }

  public void setInitCookieRequired(boolean initCookieRequired) {
    this.initCookieRequired = initCookieRequired;
  }

  public boolean isInitCookieDone() {
    return initCookieDone;
  }

  public void setInitCookieDone(boolean initCookieDone) {
    this.initCookieDone = initCookieDone;
  }

}
