package org.exoplatform.services.wsrp.consumer;

/**
 * Defines the information which uniquely identifies an portlet provided by a
 * producer. The portlet key does not identify a use of an portlet
 * 
 * @author <a href='mailto:Stephan.Laertz@de.ibm.com'>Stephan Laertz</a>
 * @author Benjamin Mestrallet
 */
public interface PortletKey {

  /**
   * Get the portlet handle which identifies an portlet in the scope of one
   * producer
   * 
   * @return The portlet handle
   */
  public String getPortletHandle();

  /**
   * Set the portlet handle which identifies an portlet in the scope of one
   * producer
   * 
   * @param portletHandle The portlet handle
   */
  public void setPortletHandle(String portletHandle);

  /**
   * Get the ID of the producer providing the portlet
   * 
   * @return The ID of the producer
   */
  public String getProducerId();

  /**
   * Set the ID of the producer providing the portlet
   * 
   * @param id The ID of the producer
   */
  public void setProducerId(String id);
}
