package org.exoplatform.services.portletcontainer.impl.portletAPIImp;

import javax.portlet.PortletContext;
import javax.servlet.ServletContext;
import java.net.URLClassLoader;
import org.exoplatform.container.ExoContainer;

/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 11 nov. 2003
 * Time: 19:52:26
 */
public class PortletAPIObjectFactory {

  private static PortletAPIObjectFactory ourInstance;

  public synchronized static PortletAPIObjectFactory getInstance() {
    if (ourInstance == null) {
      ourInstance = new PortletAPIObjectFactory();
    }
    return ourInstance;
  }

  private PortletAPIObjectFactory() {
  }

  public PortletContext createPortletContext(ExoContainer cont, ServletContext scontext) {
    return new PortletContextImpl(cont, scontext) ;
  }

  public PortletContext createPortletContext(ExoContainer cont, ServletContext scontext, URLClassLoader cl) {
    return new PortletContextImpl(cont, scontext) ;
  }

}