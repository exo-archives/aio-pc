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

package org.exoplatform.services.wsrp1.consumer.adapters;

import org.exoplatform.services.wsrp.PortletSession;
import org.exoplatform.services.wsrp.PortletWindowSession;
import org.exoplatform.services.wsrp1.type.MarkupContext;

/*
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net Date: 9
 * févr. 2004 Time: 15:49:27
 */

public class PortletWindowSessionAdapter implements PortletWindowSession {

  private String         windowID;

  private PortletSession portletSession;

  private MarkupContext  markupContext;

  private String         navigationalState;

  public String getWindowID() {
    return windowID;
  }

  public void setWindowID(String windowID) {
    this.windowID = windowID;
  }

  public MarkupContext getCachedMarkup() {
    return markupContext;
  }

  public void updateMarkupCache(MarkupContext markupContext) {
    this.markupContext = markupContext;
  }

  public PortletSession getPortletSession() {
    return portletSession;
  }

  public void setPortletSession(PortletSession portletSession) {
    this.portletSession = portletSession;
  }

  public MarkupContext getMarkupContext() {
    return markupContext;
  }

  public void setMarkupContext(MarkupContext markupContext) {
    this.markupContext = markupContext;
  }

  public String getNavigationalState() {
    return navigationalState;
  }

  public void setNavigationalState(String navigationalState) {
    this.navigationalState = navigationalState;
  }

}
