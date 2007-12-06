/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portletcontainer.impl.aop;

import javax.portlet.Portlet;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.exoplatform.container.component.ExecutionContext;
/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Sep 17, 2005
 */
public class RenderExecutionContext extends  ExecutionContext {
  RenderRequest request_ ;
  RenderResponse response_ ;
  Portlet  portlet_ ;
  
  public  RenderExecutionContext(Portlet p,  RenderRequest req, RenderResponse res) {
    portlet_ =  p ;
    request_ = req ;
    response_ =  res ;
  }
}
