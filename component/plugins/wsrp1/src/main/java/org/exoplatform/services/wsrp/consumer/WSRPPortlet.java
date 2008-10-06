package org.exoplatform.services.wsrp.consumer;

import org.exoplatform.services.wsrp.type.PortletContext;

/**
 * Defines a consumer-side representation of a remote portlet. A portlet is
 * uniquely identified by its portlet key. Consumer configured portlets are the
 * result of clone operations on existing portlets (parents).
 * 
 * @author Stephan Laertz
 * @author Benjamin Mestrallet
 */
public interface WSRPPortlet {

  /**
   * Get the portlet key of the portlet. The key can be used to reference to the
   * portlet.
   * 
   * @return a portlet key object
   */
  public PortletKey getPortletKey();

  /**
   * Set the portlet key of the portlet.
   * 
   * @param portletKey The portlet key of the portlet
   */
  public void setPortletKey(PortletKey portletKey);

  /**
   * Get the portlet context object which contains information about the portlet
   * state.
   * 
   * @return the portlet context object of the portlet.
   */
  public PortletContext getPortletContext();

  /**
   * Set the portlet context of the portlet.
   * 
   * @param portletContext The portlet context of the portlet
   */
  public void setPortletContext(PortletContext portletContext);

  /**
   * Checks if a portlet is consumer configured portlet.
   * 
   * @return True if the result <code>getParent()</code> is not equal to the
   *         portlet handle of the portlet key.
   */
  public boolean isConsumerConfigured();

  /**
   * Get the portlet handle of the parent portlet. If the portlet is not a
   * consumer configured portlet the handle returned by this method should be
   * the same as the handle in the portlet key returned by
   * <code>getPortletKey</code>.
   * 
   * @return the portlet handle of the parent portlet.
   */
  public String getParent();

  /**
   * Set the portlet handle of the parent portlet. If the supplied handle is not
   * equal to the handle in the portlet key returned by
   * <code>getPortletKey</code> this method makes the portlet a consumer
   * configured portlet.
   * 
   * @param portletHandle the portlet handle of the parent portlet
   */
  public void setParent(String portletHandle);
}
