/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portletcontainer.impl.aop;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;

import org.exoplatform.container.component.ExecutionContext;

/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Sep 17, 2005
 */
public class ActionExecutionContext extends  ExecutionContext {
  ActionRequest request_ ;
  ActionResponse response_ ;
  Portlet  portlet_ ;
  
  public  ActionExecutionContext(Portlet p,  ActionRequest req, ActionResponse res) {
    portlet_ =  p ;
    request_ = req ;
    response_ =  res ;
  }
}
