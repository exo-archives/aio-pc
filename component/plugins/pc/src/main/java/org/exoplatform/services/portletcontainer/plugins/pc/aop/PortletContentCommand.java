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
package org.exoplatform.services.portletcontainer.plugins.pc.aop;

import org.exoplatform.services.portletcontainer.pci.RenderOutput;
import org.exoplatform.services.portletcontainer.pci.ResourceOutput;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.PortletResponseImp;
import org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.helpers.CustomResponseWrapper;

public class PortletContentCommand extends BaseCommandUnit {

  public Object render(RenderExecutionContext rcontext) throws Throwable {
    log_.debug("--> render method, call content ");
    Object result = rcontext.executeNextUnit();
    PortletResponseImp rimpl = (PortletResponseImp) rcontext.response_;
    CustomResponseWrapper responseWrapper = (CustomResponseWrapper) rimpl.getResponse();
    responseWrapper.flushBuffer();
    RenderOutput routput = (RenderOutput) rimpl.getOutput();
    routput.setContent(responseWrapper.getPortletContent());
    routput.setCacheHit(false);
    // бред:
    // RenderResponseImp renderRespImpl = (RenderResponseImp) rcontext.response_;
    // routput.setNextPossiblePortletModes(renderRespImpl.getNextPossiblePortletModes());
    return result;
  }

  public Object serveResource(ResourceExecutionContext rcontext) throws Throwable {
    log_.debug("--> resource method, call content ");
    Object result = rcontext.executeNextUnit();
    PortletResponseImp rimpl = (PortletResponseImp) rcontext.response_;
    CustomResponseWrapper responseWrapper = (CustomResponseWrapper) rimpl.getResponse();
    responseWrapper.flushBuffer();
    ResourceOutput routput = (ResourceOutput) rimpl.getOutput();
    routput.setContent(responseWrapper.getPortletContent());
    return result;
  }

}
