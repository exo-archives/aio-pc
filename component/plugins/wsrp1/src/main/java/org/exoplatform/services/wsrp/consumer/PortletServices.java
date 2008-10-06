package org.exoplatform.services.wsrp.consumer;

import org.exoplatform.services.wsrp.intf.WSRP_v1_Markup_PortType;
import org.exoplatform.services.wsrp.intf.WSRP_v1_PortletManagement_PortType;

/**
 * WSRP portlet services are all wsrp service interfaces which are session
 * dependent. With the implemented version 0.91 of the WSRP specification the
 * markup interface and the portlet management interface depend on using the
 * same session cookie in an clustered environment. <p/> This interface provides
 * a vehicle to store and retrieve the portTypes of these interfaces.
 * 
 * @author Stephan Laertz
 * @author Benjamin Mestrallet
 */
public interface PortletServices {

  /**
   * Set the portType object of the portlet management service.
   * 
   * @param portletManagementPortType Port type object which describes the
   *          operations of the producers portlet mamagement service endpoint
   */
  public void setWSRPPortletManagementService(WSRP_v1_PortletManagement_PortType portletManagementPortType);

  /**
   * Get a port type object describing the operations of a produces portlet
   * management service endpoint.
   * 
   * @return Portlet management port type object
   */
  public WSRP_v1_PortletManagement_PortType getWSRPPortletManagementService();

  /**
   * Set the portType object of the markup service.
   * 
   * @param markupPortType Port type object which describes the operations of
   *          the producers markup service endpoint
   */
  public void setWSRPMarkupService(WSRP_v1_Markup_PortType markupPortType);

  /**
   * Get a port type object describing the operations of a produces markup
   * service endpoint.
   * 
   * @return Markup port type object
   */
  public WSRP_v1_Markup_PortType getWSRPMarkupService();

  /**
   * Get the URL of the producers markup and portlet management service
   * interface. Since both services can depend on a producer session they need
   * to have the same accessPoint.
   * 
   * @return The URL of the producers markup and portlet management service
   *         interface.
   */
  public String getPortletServicesURL();
}
