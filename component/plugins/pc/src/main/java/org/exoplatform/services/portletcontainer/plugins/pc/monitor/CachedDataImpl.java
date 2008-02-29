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
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 6 mai 2004
 */
public class CachedDataImpl implements CachedData {
  private long lastAccessTime = 0;
  private String title;
  private String etag;
  private byte[] content;
  private PortletMode mode;
  private WindowState state;

  public byte[] getContent() {
    return content;
  }

  public synchronized void setContent(byte[] content) {
    this.content = content;
  }

  public long getLastAccessTime() {
    return lastAccessTime;
  }

  public synchronized void setLastAccessTime(long lastAccessTime) {
    this.lastAccessTime = lastAccessTime;
  }

  public String getTitle() {
    return title;
  }

  public synchronized void setTitle(String title) {
    this.title = title;
  }

  public String getETag() {
    return etag;
  }

  public synchronized void setETag(String etag) {
    this.etag = etag;
  }

  public PortletMode getMode() {
    return mode;
  }

  public synchronized void setMode(PortletMode mode) {
    this.mode = mode;
  }

  public WindowState getWindowState() {
    return state;
  }

  public synchronized void setWindowState(WindowState state) {
    this.state = state;
  }
}