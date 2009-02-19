package org.exoplatform.services.wsrp2.consumer;

import org.exoplatform.services.wsrp2.type.UserContext;

/**
 * This interface defines a user with a certain user id and a user context.
 * 
 * @author Stephan Laertz
 * @author Benjamin Mestrallet
 */
public interface User {

  /**
   * Returns the userID
   * 
   * @return _userID
   */
  public String getUserID();

  /**
   * Sets the userID
   * 
   * @param userID as String
   */
  public void setUserID(String userID);

  /**
   * Returns the UserContext for this userid
   * 
   * @return _userContext
   */
  public UserContext getUserContext();

  /**
   * Sets the UserContext for this userID
   */
  public void setUserContext(UserContext userContext);
}
