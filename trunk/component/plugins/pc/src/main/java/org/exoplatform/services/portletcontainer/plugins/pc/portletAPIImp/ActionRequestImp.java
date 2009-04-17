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
package org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp;

import javax.portlet.ActionRequest;

/**
 * Created by The eXo Platform SAS.
 * Author : Mestrallet Benjamin benjmestrallet@users.sourceforge.net
 * Date: Jul 29, 2003
 * Time: 1:38:46 PM
 */
public class ActionRequestImp extends ClientDataRequestImp implements ActionRequest {

  /**
   * @param reqCtx request context
   */
  public ActionRequestImp(final RequestContext reqCtx) {
    super(reqCtx);
  }

  /**
   * Overridden method.
   *
   * @return action lifecycle phase
   * @see org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.ClientDataRequestImp#getLifecyclePhase()
   */
  public String getLifecyclePhase() {
    return ACTION_PHASE;
  }

}
