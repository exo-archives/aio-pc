package org.exoplatform.services.wsrp.consumer;


import java.util.Iterator;
import org.exoplatform.services.wsrp.type.SessionContext;

/**
 * Defines a session object at the consumer-side
 * to store remote portlet related information that
 * are needed to interact with the portlet.
 * Since a session context is not mandatory in WSRP
 * a consumer portlet instance key is taken to
 * uniquely identify a portlet session.
 *
 * @author Benjamin Mestrallet
 */
public interface PortletSession {
  /**
   * Get the portlet handle which uniquely identifies the
   * portlet session at the consumer side.
   *
   * @return The portlet handle
   */
  public String getPortletHandle();

  /**
   * Set the portlet handle of the portlet
   * this session belongs to.
   *
   * @param portletHandle The portlet handle
   */
  public void setPortletHandle(String portletHandle);

  /**
   * Get the WSRP session context of
   * the portlet instance. If no session context was set
   * from the producer this method returns null.
   *
   * @return The the session context if set from the producer
   *         or null otherwise.
   */
  public SessionContext getSessionContext();

  /**
   * Set the session context of the portlet instance.
   *
   * @param sessionContext The session context.
   */
  public void setSessionContext(SessionContext sessionContext);

  /**
   * Get the <code>SimplePortletWindowSession</code> of the portlet window with the given ID.
   *
   * @param windowID The ID of the portlet window
   * @return The <code>PorletWindowSession</code> with the given ID.
   */
  public PortletWindowSession getPortletWindowSession(String windowID);

  /**
   * Get all window session which belong to the portlet session
   *
   * @return An Iterator of <code>SimplePortletWindowSession</code> objects.
   */
  public Iterator getAllPorletWindowSessions();

  /**
   * Remove the porlet window session with the given window id.
   *
   * @param windowID The ObjectID of the portlet window whichs session shoul dbe removed
   * @return The portlet window session which has been removed or null if the session did not exist.
   */
  public PortletWindowSession removePortletWindowSession(String windowID);

  /**
   * Remove all portlet window sessions which  belong to this portlet session.
   */
  public void removeAllPortletWindowSessions();
}
