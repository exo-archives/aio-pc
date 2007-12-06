package org.exoplatform.services.wsrp.consumer;

import java.util.Iterator;

/**
 * Interface for a consumer based group session.A group session
 * is used to hold portlet session objects of portlet instances
 * which belong to the same group of the same producer according to their
 * portlet description.
 *
 * @author Stephan Laertz
 * @author <a href='mailto:peter.fischer@de.ibm.com'>Peter Fischer</a>
 * @author Benjamin Mestrallet
 */
public interface GroupSession {

  /**
   * Get the ID of the group this group session belongs to.
   *
   * @return The group ID
   */
  public String getGroupID();

  /**
   * Get the portlet session object which is identified with
   * the givven instanceKey from the group session. If no portlet session
   * with that instanceKey exists it depends of the implementation wether
   * null or a newly created portlet session object is returned.
   *
   * @param instanceKey The key which identifies the portlet session object
   * @return The portlet session with the given key
   */
  public PortletSession getPortletSession(String instanceKey);

  /**
   * Get all portlet session objects currently stored in the group session.
   *
   * @return Iterator with all portlet session objects in the group session.
   */
  public Iterator getAllPortletSessions();

  /**
   * Check if the group session holds a portlet session with the given key.
   *
   * @return True if the group session holds a protlet session with the given key;
   *         false otherwise
   */
  public boolean existsPortletSession(String instanceKey);

  /**
   * Set the ID of the group this group session belongs to.
   *
   * @param groupID ID of the group
   */
  public void setGroupID(String groupID);

  /**
   * Add a portlet session to this group session. 
   */
  public void addPortletSession(PortletSession portletSession);

  /**
   * Remove the portlet session object with the given key from the
   * group session. Subsequent calls of getPortletSession with the same
   * key should either return null or a newly created object.
   *
   * @param instanceKey Key which identifies the portlet session object to be removed.
   */
  public void removePortletSession(String instanceKey);

  /**
   * Removes all portlet session objects from the group session. Consequently
   * this methods can be used to clear the group session.
   */
  public void removeAllPortletSessions();

}
