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

package org.exoplatform.services.wsrp1.consumer.impl.helpers;

import java.net.MalformedURLException;
import java.net.URL;

import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.wsrp1.consumer.templates.InitCookieTemplate;
import org.exoplatform.services.wsrp1.intf.WSRPService;
import org.exoplatform.services.wsrp1.intf.WSRPV1MarkupPortType;

/**
 * Created y the eXo platform team User: Benjamin Mestrallet Date: 11 mai 2004
 */
public class InitCookieImpl extends InitCookieTemplate {
  private WSRPService             service;

  private String                  markupInterfaceURL;

  private WSRPV1MarkupPortType markupInterface;

  public InitCookieImpl(String markupInterfaceURL) {
    service = (WSRPService) (ExoContainerContext.getCurrentContainer().getComponentInstanceOfType(WSRPService.class));
    ((WSRPServiceLocator) service).setMaintainSession(true);
    this.markupInterfaceURL = markupInterfaceURL;
    try {
      this.markupInterface = service.getWSRPMarkupService(new URL(markupInterfaceURL));
    } catch (ServiceException e) {
      e.printStackTrace();
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
  }

  public String getMarkupInterfaceURL() {
    return markupInterfaceURL;
  }

  public WSRPV1MarkupPortType getWSRPMarkupService() {
    return markupInterface;
  }

  public void setWSRPMarkupService(WSRPV1MarkupPortType markupPortType) {
    this.markupInterface = markupPortType;
  }
}
