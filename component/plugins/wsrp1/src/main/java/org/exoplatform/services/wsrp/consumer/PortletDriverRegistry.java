package org.exoplatform.services.wsrp.consumer;

import java.util.Iterator;
import org.exoplatform.services.wsrp.exceptions.WSRPException;

/**
 * This interface defines a registry which can be used to store portlet driver
 * objects.
 * 
 * @author Stephan Laertz
 * @author Benjamin Mestrallet
 */
public interface PortletDriverRegistry {

  /**
   * Get an portlet driver for the given portlet. If there is no portlet driver
   * object cached a new portlet driver will be created and returned.
   * 
   * @param portlet The portlet the returned portlet driver is bind to
   * @return The portlet driver for this portlet
   */
  public PortletDriver getPortletDriver(WSRPPortlet portlet) throws WSRPException;

  /**
   * Get all cached portlet drivers.
   * 
   * @return Iterator with all portlet drivers in the registry
   */
  public Iterator getAllPortletDrivers();
}
