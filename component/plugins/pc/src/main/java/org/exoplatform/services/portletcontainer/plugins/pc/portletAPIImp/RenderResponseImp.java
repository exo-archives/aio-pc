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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.portlet.PortletMode;
import javax.portlet.RenderResponse;

import org.exoplatform.services.portletcontainer.pci.RenderOutput;
import org.exoplatform.services.portletcontainer.pci.model.Supports;
import org.exoplatform.services.portletcontainer.plugins.pc.PCConstants;

/**
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Jul 29, 2003
 * Time: 5:52:27 PM
 */
public class RenderResponseImp extends MimeResponseImp implements RenderResponse {

  public RenderResponseImp(ResponseContext resCtx) {
    super(resCtx);
  }

  public void setTitle(String s) {
    ((RenderOutput) getOutput()).setTitle(s);
  }

  public void setNextPossiblePortletModes(Collection<PortletMode> portletModes) {
    ((RenderOutput)output_).setNextPossiblePortletModes(portletModes);
  }


}
