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
package org.exoplatform.frameworks.portletcontainer.portalframework;

import javax.portlet.Event;

/**
 * Created by The eXo Platform SAS  .
 *
 * @author <a href="mailto:roman.pedchenko@exoplatform.com.ua">Roman Pedchenko</a>
 * @version $Id$
 */

/**
 * Internal class that defines event + its target portlet each event before processing is splitted
 * into a number of EventInfo instances -- one instance per target portlet.
 *
 */
public class EventInfo {

  /**
   * Formed event.
   */
  private Event event;

  /**
   * Target portlet which the event has to be delivered to.
   */
  private String target;

  /**
   * Default constructor.
   */
  public EventInfo() {
  }

  /**
   * Normal constructor.
   *
   * @param event event to deliver
   * @param target target portlet name to deliver the event to
   */
  public EventInfo(final Event event, final String target) {
    this.event = event;
    this.target = target;
  }

  /**
   * Event field getter.
   *
   * @return event itself
   */
  public final Event getEvent() {
    return event;
  }

  /**
   * Event field setter.
   *
   * @param event event to set
   */
  public final void setEvent(final Event event) {
    this.event = event;
  }

  /**
   * Target field getter.
   *
   * @return target portlet name
   */
  public final String getTarget() {
    return target;
  }

  /**
   * Target field setter.
   *
   * @param target target portlet name to set
   */
  public final void setTarget(final String target) {
    this.target = target;
  }

}
