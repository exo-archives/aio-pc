/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 *  
 * Created on 16 janv. 2004
 */
package org.exoplatform.services.wsrp.producer;

import java.util.Collection;
import java.util.Map;

import org.exoplatform.services.portletcontainer.pci.ActionInput;
import org.exoplatform.services.portletcontainer.pci.ActionOutput;
import org.exoplatform.services.portletcontainer.pci.RenderInput;
import org.exoplatform.services.portletcontainer.pci.RenderOutput;
import org.exoplatform.services.wsrp.exceptions.WSRPException;
import org.exoplatform.services.wsrp.producer.impl.helpers.WSRPHttpServletRequest;
import org.exoplatform.services.wsrp.producer.impl.helpers.WSRPHttpServletResponse;
import org.exoplatform.services.wsrp.type.PortletDescription;
import org.exoplatform.services.wsrp.type.PropertyList;
import org.exoplatform.services.wsrp.type.ResourceList;


/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public interface PortletContainerProxy {

  public boolean isPortletOffered(String portletHandle);
  
  public ResourceList getResourceList(String[] desiredLocales);
  public PortletDescription getPortletDesciption(String portletHandle, 
                                                 String[] desiredLocales);

  public void setPortletProperties(String portletHandle, 
                                   String userID, 
                                   PropertyList propertyList)
    throws WSRPException;

  public Map getPortletProperties(String portletHandle, String userID) throws WSRPException;
  public Map getAllPortletMetaData();
  public Collection getWindowStates(String s);
  public Collection getSupportedWindowStates();

  public RenderOutput render(WSRPHttpServletRequest request, WSRPHttpServletResponse response, RenderInput input)
      throws WSRPException;
  public ActionOutput processAction(WSRPHttpServletRequest request, WSRPHttpServletResponse response, ActionInput input)
      throws WSRPException;

  public Collection getSupportedPortletModesWithDescriptions();
  public Collection getSupportedWindowStatesWithDescriptions();
}
