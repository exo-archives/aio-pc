/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 *  
 * Created on 12 janv. 2004
 */
package org.exoplatform.services.wsrp.producer.impl.helpers;

import java.util.Enumeration;
import java.util.List;

import javax.portlet.PortletURL;

import org.exoplatform.services.portletcontainer.pci.PortletURLFactory;
import org.exoplatform.services.wsrp.producer.PersistentStateManager;


/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public class WSRPProducerRewriterPortletURLFactory implements PortletURLFactory{

  private String sessionID;
  private List supports;
  private String portletHandle;
  private String template;
  private Enumeration supportedWindowState;
  private List customWindowStates;
  private boolean isCurrentlySecured;
  private String markup;
  private PersistentStateManager stateManager;

  public WSRPProducerRewriterPortletURLFactory(String markup,
                                               List supports,
                                               boolean isCurrentlySecured,
                                               List customWindowStates,
                                               Enumeration supportedWindowState,
                                               String template, String portletHandle,
                                               PersistentStateManager stateManager,
                                               String sessionID) {
    this.markup = markup;
    this.supports = supports;
    this.isCurrentlySecured = isCurrentlySecured;
    this.customWindowStates = customWindowStates;
    this.supportedWindowState = supportedWindowState;
    this.template = template;
    this.portletHandle = portletHandle;
    this.stateManager = stateManager;
    this.sessionID = sessionID;                                  
  }

  public PortletURL createPortletURL(String type){
    return new ProducerRewriterPortletURLImp(type,
                             markup, 
                             supports,
                             isCurrentlySecured,
                             customWindowStates, 
                             supportedWindowState,
                             template,
                             portletHandle,
                             stateManager,
                             sessionID );    
  }

}
