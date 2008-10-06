package org.exoplatform.services.wsrp2.consumer;

import java.util.Iterator;

import org.exoplatform.services.wsrp2.exceptions.WSRPException;

/**
 * A consumer based session which represents a user session with a certain
 * producer. This user session contains one or more group sessions.
 * 
 * @author <a href="mailto:stephan.laertz@de.ibm.com">Stephan Laertz</a>
 * @author <a href='mailto:peter.fischer@de.ibm.com'>Peter Fischer</a>
 * @author Benjamin Mestrallet
 * @see GroupSession
 */
public interface UserSession {

  /**
   * Get ID of the user this session is bind to
   * 
   * @return User ID
   */
  public String getUserID();

  /**
   * Set the ID of the user this session is bind to
   * 
   * @param userID ID of the user
   */
  public void setUserID(String userID);

  /**
   * Get the group session for this group ID
   * 
   * @param groupID ID of the portlet application
   * @return The a group session for the provided group ID or a new groupSession
   */
  public GroupSessionMgr getGroupSession(String groupID) throws WSRPException;

  /**
   * Get all group session
   * 
   * @return Iterator with all group sessions for the given producer access
   *         point
   */
  public Iterator getAllGroupSessions();

  /**
   * Add a group session to the user session
   * 
   * @param groupSession A group session
   */
  public void addGroupSession(GroupSession groupSession);

  /**
   * Remove a group session from the user session
   * 
   * @param groupID ID of the portlet application
   */
  public void removeGroupSession(String groupID);

  /**
   * Remove all group sessions
   */
  public void removeAllGroupSessions();
}
