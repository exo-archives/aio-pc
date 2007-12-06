/*
* Copyright 2001-2004 The eXo platform SARL All rights reserved.
* Please look at license.txt in info directory for more license detail.
*/

package org.exoplatform.services.wsrp.producer.impl.helpers;

import org.exoplatform.services.wsrp.type.CacheControl;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 25 janv. 2004
 * Time: 18:46:09
 */

public class CacheControlProxy {

  private CacheControl cacheControl;
  private long creationTime;

  public CacheControlProxy(CacheControl cacheControl) {
    this.cacheControl = cacheControl;
    creationTime = System.currentTimeMillis();
  }

  public CacheControl getCacheControl() {
    return cacheControl;
  }

  public boolean isValid(){
    if(System.currentTimeMillis() - creationTime < cacheControl.getExpires() * 1000)
      return true;
    else
      return false;
  }

}