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

package org.exoplatform.services.wsrp2.consumer.impl.helpers;

import org.exoplatform.services.wsrp2.consumer.templates.InitCookieTemplate;

/**
 * User: Benjamin Mestrallet Date: 11 mai 2004
 */
public class InitCookieImpl extends InitCookieTemplate {

  private String producerID;
  
  public InitCookieImpl() {
  }

  public InitCookieImpl(String producerID) {
    this.producerID = producerID;
  }

  public String getProducerID() {
    return producerID;
  }

}
