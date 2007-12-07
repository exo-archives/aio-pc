package org.exoplatform.services.wsrp2.consumer;

import org.exoplatform.services.wsrp2.type.MarkupContext;
import org.exoplatform.services.wsrp2.type.NamedString;
import org.exoplatform.services.wsrp2.type.ResourceContext;

/**
 * The portlet window session is a session which is unique for every window of a
 * portlet instance.
 */
public interface PortletWindowSession {

  /**
   * Get the ID of the portlets window this session belongs to.
   * 
   * @return The ID of the portlet window.
   */
  public String getWindowID();

  /**
   * Set the ID of the portlets window this sessions belongs to.
   * 
   * @param windowID The ID of the portlet window.
   */
  public void setWindowID(String windowID);

  /**
   * Get the markup context which has been cached. This might be useful to
   * retrieve the markup which was returned
   * <code>performBlockingInteraction</code> calls in order to save an
   * additional <code>getMarkup</code> call.
   * 
   * @return The cached markup context or null in case the cache is empty.
   */
  public MarkupContext getCachedMarkup();

  /**
   * Update the cache which holds the markup context. This might be useful to
   * store the markup which was returned by
   * <code>performBlockingInteraction</code> calls in order to save an
   * additional <code>getMarkup</code> call. Updateing the cache with a null
   * value clears the markup cache.
   */
  public void updateMarkupCache(MarkupContext markupContext);

  /**
   * Get the portlet session this window session belongs to.
   * 
   * @return The <code>PortletSession</code> this window session belongs to.
   */
  public PortletSession getPortletSession();

  public void setNavigationalState(String navigationalState);

  public String getNavigationalState();

  // WSRP 2

  public ResourceContext getCachedResource();

  public void updateResourceCache(ResourceContext resourceContext);

  public void setNavigationalValues(NamedString[] navigationalValues);

  public NamedString[] getNavigationalValues();

  public void setInteractionState(String interactionState);

  public String getInteractionState();

  public void setResourceState(String resourceState);

  public String getResourceState();

}
