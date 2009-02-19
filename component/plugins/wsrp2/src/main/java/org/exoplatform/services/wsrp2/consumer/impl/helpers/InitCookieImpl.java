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

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.rpc.ServiceException;

import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.wsrp2.consumer.templates.InitCookieTemplate;
import org.exoplatform.services.wsrp2.intf.WSRP_v2_Markup_PortType;
import org.exoplatform.services.wsrp2.wsdl.WSRPService;
import org.exoplatform.services.wsrp2.wsdl.WSRPServiceLocator;

/**
 * User: Benjamin Mestrallet Date: 11 mai 2004
 */
public class InitCookieImpl extends InitCookieTemplate {
  private WSRPService             service;

  private String                  markupInterfaceURL;

  private WSRP_v2_Markup_PortType markupPort;

  public InitCookieImpl(String markupInterfaceURL) {
    service = (WSRPService) (ExoContainerContext.getCurrentContainer().getComponentInstanceOfType(WSRPService.class));
    ((WSRPServiceLocator) service).setMaintainSession(true);
    this.markupInterfaceURL = markupInterfaceURL;
    try {
      this.markupPort = service.getWSRPMarkupService(new URL(markupInterfaceURL));
    } catch (ServiceException e) {
      e.printStackTrace();
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
  }

  public String getMarkupInterfaceURL() {
    return markupInterfaceURL;
  }

  public WSRP_v2_Markup_PortType getWSRPMarkupService() {
    return markupPort;
  }

  public void setWSRPMarkupService(WSRP_v2_Markup_PortType markupPortType) {
    this.markupPort = markupPortType;
  }
}
