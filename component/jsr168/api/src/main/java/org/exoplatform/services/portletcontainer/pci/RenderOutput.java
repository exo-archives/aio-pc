/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Jul 30, 2003
 * Time: 9:08:33 PM
 */
package org.exoplatform.services.portletcontainer.pci;

public class RenderOutput extends Output {

  private String title;
  private char[] content;
  private boolean cacheHit = true;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public char[] getContent() {
    return content;
  }

  public void setContent(char[] content) {
    this.content = content;
  }

  public boolean isCacheHit() {
    return cacheHit;
  }

  public void setCacheHit(boolean cacheHit) {
    this.cacheHit = cacheHit;
  }

}
