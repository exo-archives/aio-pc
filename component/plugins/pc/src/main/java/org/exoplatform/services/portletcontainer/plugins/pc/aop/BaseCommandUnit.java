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

import org.exoplatform.services.log.Log;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.container.component.ExecutionContext;
import org.exoplatform.container.component.ExecutionUnit;

/**
 * @author: Benjamin Mestrallet
 * @author: Tuan Nguyen
 */
abstract public class BaseCommandUnit extends ExecutionUnit {

  /**
   * Logger.
   */
  protected Log log = ExoLogger.getLogger("org.exoplatform.services.portletcontainer");

  /**
   * Overridden method.
   *
   * @param context context
   * @return object
   * @throws Throwable throwable
   * @see org.exoplatform.container.component.ExecutionUnit#execute(org.exoplatform.container.component.ExecutionContext)
   */
  public Object execute(final ExecutionContext context) throws Throwable {
    if (context instanceof RenderExecutionContext) {
      RenderExecutionContext rcontext = (RenderExecutionContext) context;
      return render(rcontext);
    }
    if (context instanceof EventExecutionContext) {
      EventExecutionContext econtext = (EventExecutionContext) context;
      return processEvent(econtext);
    }
    if (context instanceof ResourceExecutionContext) {
      ResourceExecutionContext rcontext = (ResourceExecutionContext) context;
      return serveResource(rcontext);
    }
    ActionExecutionContext acontext = (ActionExecutionContext) context;
    return processAction(acontext);
  }

  /**
   * @param rcontext context
   * @return object
   * @throws Throwable throwable
   */
  protected Object render(final RenderExecutionContext rcontext) throws Throwable {
    return rcontext.executeNextUnit();
  }

  /**
   * @param rcontext context
   * @return object
   * @throws Throwable throwable
   */
  protected Object serveResource(final ResourceExecutionContext rcontext) throws Throwable {
    return rcontext.executeNextUnit();
  }

  /**
   * @param econtext context
   * @return object
   * @throws Throwable throwable
   */
  protected Object processEvent(final EventExecutionContext econtext) throws Throwable {
    return econtext.executeNextUnit();
  }

  /**
   * @param acontext context
   * @return object
   * @throws Throwable throwable
   */
  protected Object processAction(final ActionExecutionContext acontext) throws Throwable {
    return acontext.executeNextUnit();
  }

}
