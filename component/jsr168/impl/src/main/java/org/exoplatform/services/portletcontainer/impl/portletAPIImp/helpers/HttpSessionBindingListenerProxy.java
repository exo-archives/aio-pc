/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 *  
 * Created on 2 d�c. 2003
 */
package org.exoplatform.services.portletcontainer.impl.portletAPIImp.helpers;

import javax.servlet.http.HttpSessionBindingListener;

/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public class HttpSessionBindingListenerProxy {
  
  private HttpSessionBindingListener listener;
  
  public HttpSessionBindingListenerProxy(HttpSessionBindingListener listener) {
    this.listener = listener;    
  }

  public HttpSessionBindingListener getListener() {
    return listener;
  }
}