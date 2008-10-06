package org.exoplatform.services.wsrp.consumer;

import java.util.Iterator;

/**
 * Defines a registry which can be used to manage users.
 * 
 * @author Stephan Laertz
 * @author Benjamin Mestrallet
 */
public interface UserRegistry {

  /**
   * Add a user to the registry
   * 
   * @param user The user to add
   * @return The user added or null
   */
  public User addUser(User user);

  /**
   * Get the user with the given id
   * 
   * @param userID The ID of the user
   * @return The user object with the given user id
   */
  public User getUser(String userID);

  /**
   * Remove a user from the list of known user
   * 
   * @param userID The ID of the user
   * @return The user which has been removed or null
   */
  public User removeUser(String userID);

  /**
   * Remove all users from the registry
   */
  public void removeAllUsers();

  /**
   * Get an iterator with all known users
   * 
   * @return All known user objects in an iterator
   */
  public Iterator getAllUsers();
}
