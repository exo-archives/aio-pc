/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.portlets.wsrp;


import javax.xml.rpc.ServiceException;
import org.exoplatform.container.PortalContainer;
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
    ExoContainer standaloneContainer = ExoContainerContext.getCurrentContainer();
    //service = (WSRPService)(PortalContainer.getInstance().getComponentInstanceOfType(WSRPService.class));
    service = (WSRPService)(standaloneContainer.getComponentInstanceOfType(WSRPService.class));
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
