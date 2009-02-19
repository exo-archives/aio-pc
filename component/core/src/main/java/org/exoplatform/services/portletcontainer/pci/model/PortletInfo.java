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
package org.exoplatform.services.portletcontainer.pci.model;

/**
 * Jul 11, 2004 .
 *
 * @author: Tuan Nguyen
 * @email: tuan08@users.sourceforge.net
 * @version: $Id: PortletInfo.java,v 1.1 2004/07/13 02:31:13 tuan08 Exp $
 */
public class PortletInfo {

  /**
   * Title.
   */
  private String title;

  /**
   * Short title.
   */
  private String shortTitle;

  /**
   * Keywords.
   */
  private String keywords;

  // portlet api 2.0

  /**
   * Id.
   */
  private String id;

  /**
   * @return keywords
   */
  public final String getKeywords() {
    return keywords;
  }

  /**
   * @param keywords keywords
   */
  public final void setKeywords(final String keywords) {
    this.keywords = keywords;
  }

  /**
   * @return short title
   */
  public final String getShortTitle() {
    return shortTitle;
  }

  /**
   * @param shortTitle short title
   */
  public final void setShortTitle(final String shortTitle) {
    this.shortTitle = shortTitle;
  }

  /**
   * @return title
   */
  public final String getTitle() {
    return title;
  }

  /**
   * @param title title
   */
  public final void setTitle(final String title) {
    this.title = title;
  }

  /**
   * @return id
   */
  public final String getId() {
    return this.id;
  }

  /**
   * @param value id
   */
  public final void setId(final String value) {
    this.id = value;
  }
}
