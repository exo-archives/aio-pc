package org.exoplatform.services.wsrp.producer.impl.helpers;


import java.util.Enumeration;
import java.util.List;

import javax.portlet.PortletURL;

import org.exoplatform.services.portletcontainer.pci.PortletURLFactory;
import org.exoplatform.services.wsrp.producer.PersistentStateManager;

/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 23 janv. 2004
 * Time: 18:20:54
 */
public class WSRPConsumerRewriterPortletURLFactory implements PortletURLFactory {
  private String markup;
  private String sessionID;
  private boolean isCurrentlySecured;
  private PersistentStateManager stateManager;
  private String portletHandle;
  private String template;
  private Enumeration supportedWindowState;
  private List customWindowStates;
  private List supports;

  public WSRPConsumerRewriterPortletURLFactory(String markup,
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

  public PortletURL createPortletURL(String Type) {
    return new ConsumerRewriterPortletURLImp(markup, markup,
                                             supports, isCurrentlySecured,
                                             customWindowStates, supportedWindowState,
                                             template, portletHandle,
                                             stateManager, sessionID);
  }
}
