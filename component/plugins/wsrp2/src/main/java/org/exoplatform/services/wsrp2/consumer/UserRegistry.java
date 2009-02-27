/*
 * Copyright (C) 2003-2009 eXo Platform SAS.
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

package org.exoplatform.services.wsrp2.consumer;

import java.util.Iterator;

/**
 * Defines a registry which can be used to manage users.
 * 
 * @author Stephan Laertz
 * @author Benjamin Mestrallet
 */
public interface UserRegistry {

  /**
   * Add a user to the registry.
   * 
   * @param user The user to add
   * @return The user added or null
   */
  public User addUser(User user);

  /**
   * Get the user with the given id.
   * 
   * @param userID The ID of the user
   * @return The user object with the given user id
   */
  public User getUser(String userID);

  /**
   * Remove a user from the list of known user.
   * 
   * @param userID The ID of the user
   * @return The user which has been removed or null
   */
  public User removeUser(String userID);

  /**
   * Remove all users from the registry.
   */
  public void removeAllUsers();

  /**
   * Get an iterator with all known users.
   * 
   * @return All known user objects in an iterator
   */
  public Iterator getAllUsers();
}
