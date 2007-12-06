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
package org.exoplatform.services.portletcontainer.pci;

import java.util.Collection;

import javax.portlet.PortletMode;

/**
 * Created by The eXo Platform SAS
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Jul 30, 2003
 * Time: 9:08:33 PM
 */
public class RenderOutput extends Output {

  private String                  title;

  private byte[]                  content;

  private boolean                 cacheHit = true;

  private Collection<PortletMode> portletModes;

  private String                  contentType;

  public RenderOutput() {
  }
  
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public byte[] getBinContent() {
    return content;
  }

  public char[] getContent() {
    if (content == null)
      return null;
    try {
      return new String(content, "utf-8").toCharArray();
    } catch (java.io.UnsupportedEncodingException e) {
      return new String(content).toCharArray();
    }
  }

  public void setContent(byte[] content) {
    this.content = content;
  }

  public boolean isCacheHit() {
    return cacheHit;
  }

  public void setCacheHit(boolean cacheHit) {
    this.cacheHit = cacheHit;
  }

  public Collection<PortletMode> getNextPossiblePortletModes() {
    return portletModes;
  }

  public void setNextPossiblePortletModes(Collection<PortletMode> portletModes) {
    this.portletModes = portletModes;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

}
