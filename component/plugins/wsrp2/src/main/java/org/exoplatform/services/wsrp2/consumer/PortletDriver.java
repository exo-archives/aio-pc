package org.exoplatform.services.wsrp2.consumer;

import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.type.BlockingInteractionResponse;
import org.exoplatform.services.wsrp2.type.DestroyPortletsResponse;
import org.exoplatform.services.wsrp2.type.HandleEventsResponse;
import org.exoplatform.services.wsrp2.type.MarkupResponse;
import org.exoplatform.services.wsrp2.type.PortletContext;
import org.exoplatform.services.wsrp2.type.PortletDescriptionResponse;
import org.exoplatform.services.wsrp2.type.PortletPropertyDescriptionResponse;
import org.exoplatform.services.wsrp2.type.PropertyList;
import org.exoplatform.services.wsrp2.type.ResourceResponse;
import org.exoplatform.services.wsrp2.type.ReturnAny;

/**
 * The portlet driver is a wrapper for all action which can be performed on an
 * portlet. There is one portlet driver for all instances of an portlet.
 * 
 * @author Benjamin Mestrallet
 */
public interface PortletDriver {

  /**
   * Get the portlet this driver is bound to.
   * 
   * @return The enity
   */
  public WSRPPortlet getPortlet();

  /**
   * This method is used to retrieve the markup generated by the portlet
   * instance.
   * 
   * @return The markup response generated by portlet
   */
  public MarkupResponse getMarkup(WSRPMarkupRequest markupRequest,
                                  UserSessionMgr userSession,
                                  String path) throws WSRPException;

  /**
   * This method is used to perform a blocking interaction on the portlet
   * instance.
   */
  public BlockingInteractionResponse performBlockingInteraction(WSRPInteractionRequest actionRequest,
                                                                UserSessionMgr userSession,
                                                                String path) throws WSRPException;

  /**
   * Clone the portlet
   * 
   * @return The new portlet context
   */
  public PortletContext clonePortlet(UserSessionMgr userSession) throws WSRPException;

  /**
   *
   **/
  public void initCookie(UserSessionMgr userSession) throws WSRPException;

  /**
   * Destroy the producer portlets specified in the entiyHandles array.
   */
  public DestroyPortletsResponse destroyPortlets(String[] portletHandles, UserSessionMgr userSession) throws WSRPException;

  /**
   * Inform the producer that the sessions specified in the sessionIDs array
   * will no longer be used by the consumer and can therefor be released.
   */
  public ReturnAny releaseSessions(String[] sessionIDs, UserSessionMgr userSession) throws WSRPException;

  public PortletDescriptionResponse getPortletDescription(UserSessionMgr userSession,
                                                          String[] desiredLocales) throws WSRPException;

  public PortletPropertyDescriptionResponse getPortletPropertyDescription(UserSessionMgr userSession) throws WSRPException;

  public PropertyList getPortletProperties(String[] names, UserSessionMgr userSession) throws WSRPException;

  public PortletContext setPortletProperties(PropertyList properties, UserSessionMgr userSession) throws WSRPException;

  // Below changes of WSRP2 spec

  public ResourceResponse getResource(WSRPResourceRequest resourceRequest,
                                      UserSessionMgr userSession,
                                      String path) throws WSRPException;

  public HandleEventsResponse handleEvents(WSRPEventsRequest eventRequest,
                                           UserSessionMgr userSession,
                                           String path) throws WSRPException;

}
