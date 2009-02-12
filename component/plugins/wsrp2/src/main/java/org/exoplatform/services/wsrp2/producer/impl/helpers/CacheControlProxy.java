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

package org.exoplatform.services.wsrp2.producer.impl.helpers;

import org.exoplatform.services.wsrp2.type.CacheControl;

/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 25 janv. 2004
 * Time: 18:46:09
 */

public class CacheControlProxy {

  private CacheControl cacheControl;

  private long         creationTime;

  public CacheControlProxy(CacheControl cacheControl) {
    this.cacheControl = cacheControl;
    creationTime = System.currentTimeMillis();
  }

  public CacheControl getCacheControl() {
    return cacheControl;
  }

  public boolean isValid() {
    if (System.currentTimeMillis() - creationTime < cacheControl.getExpires() * 1000)
      return true;
    else
      return false;
  }

}
