/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */

package org.exoplatform.services.wsrp.consumer.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.exoplatform.services.wsrp.consumer.User;
import org.exoplatform.services.wsrp.consumer.UserRegistry;

/*
 * @author Mestrallet Benjamin benjmestrallet@users.sourceforge.net Date: 5
 * févr. 2004 Time: 13:47:56
 */

public class UserRegistryImpl implements UserRegistry {

  private Map<String, User> users = new HashMap<String, User>();

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

  public Iterator<User> getAllUsers() {
    return users.values().iterator();
  }
}