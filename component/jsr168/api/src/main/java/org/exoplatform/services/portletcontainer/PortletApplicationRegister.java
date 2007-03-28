 /***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.portletcontainer;

import java.util.Collection;

import javax.servlet.ServletContext;

import org.exoplatform.container.component.ComponentPlugin;
import org.exoplatform.services.portletcontainer.pci.model.PortletApp;


/**
 * @author Benjamin Mestrallet
 * benjamin.mestrallet@exoplatform.com
 */
public interface PortletApplicationRegister {
    
  public void registerPortletApplication(ServletContext servletContext, PortletApp portletApp_,
      Collection roles) throws PortletContainerException;

  public void removePortletApplication(ServletContext servletContext) throws PortletContainerException;  
  
  public void addListenerPlugin(ComponentPlugin listener) ;
}
