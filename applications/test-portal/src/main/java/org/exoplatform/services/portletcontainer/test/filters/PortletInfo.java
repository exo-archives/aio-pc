/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 */

package org.exoplatform.services.portletcontainer.test.filters;

import java.io.Serializable;

/**
 * Created by The eXo Platform SARL .
 *
 * @author <a href="mailto:lautarul@gmail.com">Roman Pedchenko</a>
 * @version $Id$
 */

public class PortletInfo implements Serializable{

  public String title;
  public String fqTitle;
  public String out;
  public String mode;
  public String portletapp;
  public String state;
  public boolean attr;

}
