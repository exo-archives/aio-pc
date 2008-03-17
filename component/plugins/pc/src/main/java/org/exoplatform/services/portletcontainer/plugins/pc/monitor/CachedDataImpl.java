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
package org.exoplatform.services.portletcontainer.plugins.pc.monitor;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;
import org.exoplatform.services.portletcontainer.monitor.CachedData;

/**
 * Created y the eXo platform team.
 * User: Benjamin Mestrallet
 * Date: 6 mai 2004
 */
public class CachedDataImpl implements CachedData {

  /**
   * Last access time.
   */
  private long lastAccessTime = 0;

  /**
   * Last update time.
   */
  private long lastUpdateTime = 0;

  /**
   * Portlet title.
   */
  private String title;

  /**
   * ETag.
   */
  private String etag;

  /**
   * Content.
   */
  private byte[] content;

  /**
   * Portlet mode.
   */
  private PortletMode mode;

  /**
   * Window state.
   */
  private WindowState state;

  /**
   * Overridden method.
   *
   * @return content
   * @see org.exoplatform.services.portletcontainer.monitor.CachedData#getContent()
   */
  public final byte[] getContent() {
    return content;
  }

  /**
   * @param content content
   */
  public final synchronized void setContent(final byte[] content) {
    this.content = content;
  }

  /**
   * Overridden method.
   *
   * @return last access time
   * @see org.exoplatform.services.portletcontainer.monitor.CachedData#getLastAccessTime()
   */
  public final long getLastAccessTime() {
    return lastAccessTime;
  }

  /**
   * @param lastAccessTime last access time
   */
  public final synchronized void setLastAccessTime(final long lastAccessTime) {
    this.lastAccessTime = lastAccessTime;
  }

  /**
   * Overridden method.
   *
   * @return last update time
   * @see org.exoplatform.services.portletcontainer.monitor.CachedData#getLastAccessTime()
   */
  public final long getLastUpdateTime() {
    return lastUpdateTime;
  }

  /**
   * @param lastUpdateTime last update time
   */
  public final synchronized void setLastUpdateTime(final long lastUpdateTime) {
    this.lastUpdateTime = lastUpdateTime;
  }

  /**
   * Overridden method.
   *
   * @return portlet title
   * @see org.exoplatform.services.portletcontainer.monitor.CachedData#getTitle()
   */
  public final String getTitle() {
    return title;
  }

  /**
   * @param title portlet title
   */
  public final synchronized void setTitle(final String title) {
    this.title = title;
  }

  /**
   * Overridden method.
   *
   * @return ETag
   * @see org.exoplatform.services.portletcontainer.monitor.CachedData#getETag()
   */
  public final String getETag() {
    return etag;
  }

  /**
   * @param etag ETag
   */
  public final synchronized void setETag(final String etag) {
    this.etag = etag;
  }

  /**
   * Overridden method.
   *
   * @return portlet mode
   * @see org.exoplatform.services.portletcontainer.monitor.CachedData#getMode()
   */
  public final PortletMode getMode() {
    return mode;
  }

  /**
   * @param mode portlet mode
   */
  public final synchronized void setMode(final PortletMode mode) {
    this.mode = mode;
  }

  /**
   * Overridden method.
   *
   * @return window state
   * @see org.exoplatform.services.portletcontainer.monitor.CachedData#getWindowState()
   */
  public final WindowState getWindowState() {
    return state;
  }

  /**
   * @param state window state
   */
  public final synchronized void setWindowState(final WindowState state) {
    this.state = state;
  }
}