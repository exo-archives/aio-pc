/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.services.portletcontainer.plugins.pc.aop;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.container.component.ExecutionContext;
import org.exoplatform.container.component.ExecutionUnit;

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
    if(context instanceof  EventExecutionContext) {
      EventExecutionContext econtext  = (EventExecutionContext) context ;
      return processEvent(econtext) ;
    }
    if(context instanceof  ResourceExecutionContext) {
      ResourceExecutionContext rcontext  = (ResourceExecutionContext) context ;
      return serveResource(rcontext) ;
    }
    ActionExecutionContext acontext  = (ActionExecutionContext) context ;
    return processAction(acontext) ;
  }

  protected Object render(RenderExecutionContext rcontext)  throws Throwable {
    return rcontext.executeNextUnit() ;
  }

  protected Object serveResource(ResourceExecutionContext rcontext)  throws Throwable {
    return rcontext.executeNextUnit() ;
  }

  protected Object processEvent(EventExecutionContext econtext)  throws Throwable  {
    return econtext.executeNextUnit() ;
  }

  protected Object processAction(ActionExecutionContext acontext)  throws Throwable  {
    return acontext.executeNextUnit() ;
  }

//  protected Object serveFragment(FragmentExecutionContext fcontext)  throws Throwable  {
//    return fcontext.executeNextUnit() ;
//  }

}