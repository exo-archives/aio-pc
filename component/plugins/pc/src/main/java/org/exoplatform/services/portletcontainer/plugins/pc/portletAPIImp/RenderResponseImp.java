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

import java.util.Collection;

import javax.portlet.PortletMode;
import javax.portlet.RenderResponse;

import org.exoplatform.services.portletcontainer.pci.RenderOutput;

/**
 * Author : Mestrallet Benjamin benjmestrallet@users.sourceforge.net .
 * Date: Jul 29, 2003
 * Time: 5:52:27 PM
 */
public class RenderResponseImp extends MimeResponseImp implements RenderResponse {

  /**
   * @param resCtx response context
   */
  public RenderResponseImp(final ResponseContext resCtx) {
    super(resCtx);
  }

  /**
   * Overridden method.
   *
   * @param s title
   * @see javax.portlet.RenderResponse#setTitle(java.lang.String)
   */
  public final void setTitle(final String s) {
    ((RenderOutput) getOutput()).setTitle(s);
  }

  /**
   * Overridden method.
   *
   * @param portletModes portlet modes
   * @see javax.portlet.RenderResponse#setNextPossiblePortletModes(java.util.Collection)
   */
  public final void setNextPossiblePortletModes(final Collection<PortletMode> portletModes) {
    ((RenderOutput) getOutput()).setNextPossiblePortletModes(portletModes);
  }

}
