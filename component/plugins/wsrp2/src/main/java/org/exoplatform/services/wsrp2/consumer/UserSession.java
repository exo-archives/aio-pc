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
