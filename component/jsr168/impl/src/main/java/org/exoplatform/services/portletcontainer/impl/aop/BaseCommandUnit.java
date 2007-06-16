/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL    All rights reserved.       *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portletcontainer.impl.aop;

import org.apache.commons.logging.Log;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.container.component.ExecutionContext;
import org.exoplatform.container.component.ExecutionUnit ;
/*
 * @author: Benjamin Mestrallet
 * @author: Tuan Nguyen
 */
abstract public class BaseCommandUnit extends ExecutionUnit {
  protected Log log_ = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");
  
  public Object execute(ExecutionContext context)  throws Throwable {
    if(context instanceof  RenderExecutionContext) {
      RenderExecutionContext rcontext  = (RenderExecutionContext) context ;
      return render(rcontext) ;
    } 
    ActionExecutionContext acontext  = (ActionExecutionContext) context ;
    return processAction(acontext) ;
  }
  
  protected Object render(RenderExecutionContext rcontext)  throws Throwable {
    return rcontext.executeNextUnit() ;
  }
  
  protected Object processAction(ActionExecutionContext acontext)  throws Throwable  {
    return acontext.executeNextUnit() ;
  }
}