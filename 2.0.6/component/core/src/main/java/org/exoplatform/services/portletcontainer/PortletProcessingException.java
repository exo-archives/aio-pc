/*
 * Copyright (C) 2003-2008 eXo Platform SAS.
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
package org.exoplatform.services.portletcontainer;

/**
 * Created by The eXo Platform SAS .
 * @author <a href="mailto:roman.pedchenko@exoplatform.com.ua">Roman Pedchenko</a>
 * @version $Id: $
*/
public class PortletProcessingException extends PortletContainerException {

  /**
   * @param cause
   */
  public PortletProcessingException(Throwable cause) {
    super(cause);
  }

  /**
   * @param message
   */
  public PortletProcessingException(String message) {
    super(message);
  }

  /**
   * @param message
   * @param cause
   */
  public PortletProcessingException(String message, Throwable cause) {
    super(message, cause);
  }

}
