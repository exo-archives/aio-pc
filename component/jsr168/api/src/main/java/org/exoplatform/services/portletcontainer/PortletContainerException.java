package org.exoplatform.services.portletcontainer;

/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 10 nov. 2003
 * Time: 09:44:59
 */
public class PortletContainerException extends Exception{

  public PortletContainerException() {
  }

  public PortletContainerException(Throwable cause) {
    super(cause.getMessage());
    cause.printStackTrace() ;
  }

  public PortletContainerException(String message) {
    super(message);
  }

  public PortletContainerException(String message, Throwable cause) {
    super(message);
    cause.printStackTrace() ;
  }

}
