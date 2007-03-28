/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.wsrp.consumer.templates;

import org.exoplatform.services.wsrp.consumer.InitCookieInfo;
import org.exoplatform.services.wsrp.intf.WSRP_v1_Markup_PortType;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 11 mai 2004
 */
public abstract class InitCookieTemplate
    implements InitCookieInfo{

  private boolean initCookieRequired;
  private boolean initCookieDone;

  public boolean isInitCookieRequired() {
    return initCookieRequired;
  }

  public void setInitCookieRequired(boolean initCookieRequired) {
    this.initCookieRequired = initCookieRequired;
  }

  public boolean isInitCookieDone() {
    return initCookieDone;
  }

  public void setInitCookieDone(boolean initCookieDone) {
    this.initCookieDone = initCookieDone;
  }

  public abstract String getMarkupInterfaceURL();
  public abstract WSRP_v1_Markup_PortType getWSRPBaseService();
  public abstract void setWSRPBaseService(WSRP_v1_Markup_PortType markupPortType);

}