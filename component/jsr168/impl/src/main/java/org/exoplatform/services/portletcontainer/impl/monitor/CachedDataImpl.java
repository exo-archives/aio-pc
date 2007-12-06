/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.portletcontainer.impl.monitor;


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
  private char[] content;
  private PortletMode mode;
  private WindowState state;

  public char[] getContent() {
    return content;
  }

  public synchronized void setContent(char[] content) {
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