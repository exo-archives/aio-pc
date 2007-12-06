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

package org.exoplatform.services.wsrp.consumer.impl.helpers;

import javax.xml.rpc.ServiceException;
import org.exoplatform.services.wsrp.consumer.templates.InitCookieTemplate;
import org.exoplatform.services.wsrp.intf.WSRP_v1_Markup_PortType;
import org.exoplatform.services.wsrp.wsdl.WSRPService;
import org.exoplatform.services.wsrp.wsdl.WSRPServiceLocator;
import java.net.URL;
import java.net.MalformedURLException;
import org.exoplatform.container.*;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 11 mai 2004
 */
public class InitCookieImpl extends InitCookieTemplate{
  private WSRPService service;
  private String markupInterfaceURL;
  private WSRP_v1_Markup_PortType markupInterface;

  public InitCookieImpl(String markupInterfaceURL) {
    service = (WSRPService)(ExoContainerContext.getCurrentContainer().getComponentInstanceOfType(WSRPService.class));
    ((WSRPServiceLocator)service).setMaintainSession(true);
    this.markupInterfaceURL = markupInterfaceURL;
    try {
      this.markupInterface = service.getWSRPBaseService(new URL(markupInterfaceURL));
    } catch (ServiceException e) {
      e.printStackTrace();
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
  }

  public String getMarkupInterfaceURL() {
    return markupInterfaceURL;
  }

  public WSRP_v1_Markup_PortType getWSRPBaseService() {
    return markupInterface;
  }

  public void setWSRPBaseService(WSRP_v1_Markup_PortType markupPortType) {
    this.markupInterface = markupPortType;
  }
}
