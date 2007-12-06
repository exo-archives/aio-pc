/*
* Copyright 2001-2004 The eXo platform SARL All rights reserved.
* Please look at license.txt in info directory for more license detail.
*/

package org.exoplatform.services.wsrp.consumer.impl;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.exoplatform.services.wsrp.consumer.User;
import org.exoplatform.services.wsrp.consumer.UserRegistry;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 5 févr. 2004
 * Time: 13:47:56
 */

public class UserRegistryImpl implements UserRegistry{

  private Map users = new HashMap();

  public User addUser(User user) {
    return (User) users.put(user.getUserID(), user);
  }

  public User getUser(String userID) {
    return (User) users.get(userID);
  }

  public User removeUser(String userID) {
    return (User) users.remove(userID);
  }

  public void removeAllUsers() {
    users.clear();
  }

  public Iterator getAllUsers() {
    return users.values().iterator();
  }
}