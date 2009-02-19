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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by The eXo Platform SAS  .
 *
 * @author <a href="mailto:roman.pedchenko@exoplatform.com.ua">Roman Pedchenko</a>
 * @version $Id$
 */

/**
 * PortletInfo class transfers generated portlet data (title, markup, etc.) from the portal
 * framework to a portal that will finally render its page.
 */
public class PortletInfo implements Serializable {

  /**
   * Window ID.
   */
  private String wid;

  /**
   * Portlet name.
   */
  private String portlet;

  /**
   * Portlet title.
   */
  private String title;

  /**
   * Portlet output.
   */
  private String out;

  /**
   * Current portlet mode.
   */
  private String mode;

  /**
   * Current window state.
   */
  private String state;

  /**
   * Available portlet modes.
   */
  private ArrayList<String> modes = new ArrayList<String>();

  /**
   * Available window states.
   */
  private ArrayList<String> states = new ArrayList<String>();

  /**
   * Portlet name.
   */
  private String name;

  /**
   * Session map is needed for session replication.
   */
  private HashMap<String, Object> sessionMap;

  /**
   * Either the portlet has to be rendered.
   */
  private boolean isToRender;

  /**
   * Getter for window ID.
   *
   * @return window ID
   */
  public final String getWid() {
    return wid;
  }

  /**
   * Setter for window ID.
   *
   * @param wid window ID
   */
  public final void setWid(final String wid) {
    this.wid = wid;
  }

  /**
   * Getter for portlet name.
   *
   * @return portlet name
   */
  public final String getPortlet() {
    return portlet;
  }

  /**
   * Setter for portlet name.
   *
   * @param portlet portlet name
   */
  public final void setPortlet(final String portlet) {
    this.portlet = portlet;
  }

  /**
   * Getter for portlet title.
   *
   * @return portlet title
   */
  public final String getTitle() {
    return title;
  }

  /**
   * Setter for portlet title.
   *
   * @param title portlet title to show
   */
  public final void setTitle(final String title) {
    this.title = title;
  }

  /**
   * Getter for portlet output.
   *
   * @return portlet output
   */
  public final String getOut() {
    return out;
  }

  /**
   * Setter for portlet output.
   *
   * @param out portlet output
   */
  public final void setOut(final String out) {
    this.out = out;
  }

  /**
   * Getter for current portlet mode.
   *
   * @return current portlet mode
   */
  public final String getMode() {
    return mode;
  }

  /**
   * Setter for current portlet mode.
   *
   * @param mode current portlet mode
   */
  public final void setMode(final String mode) {
    this.mode = mode;
  }

  /**
   * Getter for current window state.
   *
   * @return current window state
   */
  public final String getState() {
    return state;
  }

  /**
   * Setter for current window state.
   *
   * @param state current window state
   */
  public final void setState(final String state) {
    this.state = state;
  }

  /**
   * Getter for available portlet modes.
   *
   * @return list of available portlet modes
   */
  public final ArrayList<String> getModes() {
    return modes;
  }

  /**
   * Setter for available portlet modes.
   *
   * @param modes list of available portlet modes
   */
  public final void setModes(final ArrayList<String> modes) {
    this.modes = modes;
  }

  /**
   * Getter for available window states.
   *
   * @return list of available window states
   */
  public final ArrayList<String> getStates() {
    return states;
  }

  /**
   * Setter for available window states.
   *
   * @param states list of available window states
   */
  public final void setStates(final ArrayList<String> states) {
    this.states = states;
  }

  /**
   * Getter for session map.
   *
   * @return session map
   */
  public final HashMap<String, Object> getSessionMap() {
    return sessionMap;
  }

  /**
   * Setter for session map.
   *
   * @param sessionMap session map
   */
  public final void setSessionMap(final HashMap<String, Object> sessionMap) {
    this.sessionMap = sessionMap;
  }

  /**
   * Getter for isToRender.
   *
   * @return either the portlet has to be rendered
   */
  public final boolean isToRender() {
    return isToRender;
  }

  /**
   * Setter for isToRender.
   *
   * @param isToRender1 has the portlet to be rendered
   */
  public final void setToRender(final boolean isToRender1) {
    this.isToRender = isToRender1;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

}
