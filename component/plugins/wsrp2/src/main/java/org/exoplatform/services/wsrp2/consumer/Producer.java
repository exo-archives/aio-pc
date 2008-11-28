package org.exoplatform.services.wsrp2.consumer;

import java.net.URL;
import java.util.List;

import org.exoplatform.services.wsrp2.consumer.adapters.ports.WSRPV2MarkupPortTypeAdapter;
import org.exoplatform.services.wsrp2.consumer.adapters.ports.WSRPV2PortletManagementPortTypeAdapter;
import org.exoplatform.services.wsrp2.consumer.adapters.ports.WSRPV2RegistrationPortTypeAdapter;
import org.exoplatform.services.wsrp2.consumer.adapters.ports.WSRPV2ServiceDescriptionPortTypeAdapter;
import org.exoplatform.services.wsrp2.exceptions.WSRPException;
import org.exoplatform.services.wsrp2.type.Deregister;
import org.exoplatform.services.wsrp2.type.ModifyRegistration;
import org.exoplatform.services.wsrp2.type.PortletDescription;
import org.exoplatform.services.wsrp2.type.Register;
import org.exoplatform.services.wsrp2.type.RegistrationContext;
import org.exoplatform.services.wsrp2.type.RegistrationData;
import org.exoplatform.services.wsrp2.type.RegistrationState;
import org.exoplatform.services.wsrp2.type.ReturnAny;
import org.exoplatform.services.wsrp2.type.ServiceDescription;

/**
 * A consumer representation of a WSRP-producer providing WSRP-portlets.
 * Generally a producer can expose up to four WSRP-Interfaces. These interfaces
 * are Markup-, Service Description-,Registration- and Portlet Management
 * Interface. Whereas the Registration- and Portlet Management Interface are
 * optional.
 * 
 * @author Stephan Laertz
 * @author Benjamin Mestrallet
 */
public interface Producer {

  public List<String> getDesiredLocales();

  public void setDesiredLocales(List<String> desiredLocales);

  /**
   * Get the name of the producer.
   * 
   * @return The name of the producer
   */
  public String getName();

  /**
   * Set the name of the producer.
   * 
   * @param name The name of the producer
   */
  public void setName(String name);

  /**
   * Get the ID of the producer.
   * 
   * @return The ID of the producer
   */
  public String getID();

  /**
   * Set the ID of the producer to he given value.
   * 
   * @param id ID of the producer.
   */
  public void setID(String id);

  /**
   * Get a description of the producer.
   * 
   * @return A description of the producer
   */
  public String getDescription();

  /**
   * Set a description of the producer.
   * 
   * @param description Some descriptive information about the producer
   */
  public void setDescription(String description);

  /**
   * Get the URL of the producers service description Adapter.
   * 
   * @return URL of the service description Adapter.
   */
//  public String getServiceDescriptionAdapterEndpoint();

  /**
   * Set the URL of the producers service description Adapter.
   * 
   * @param url of the service description Adapter.
   */
//  public void setServiceDescriptionAdapterEndpoint(String url);

  /**
   * Get the producers service description Adapter.
   * 
   * @return service description Adapter.
   */
  public WSRPV2ServiceDescriptionPortTypeAdapter getServiceDescriptionAdapter();

  /**
   * Get the URL of the producers markup Adapter.
   * 
   * @return URL of the markup Adapter.
   */
//  public String getMarkupAdapterEndpoint();
  
  public WSRPV2MarkupPortTypeAdapter getMarkupAdapter();

  /**
   * Set the URL of the producers markup Adapter.
   * 
   * @param url of the markup Adapter.
   */
//  public void setMarkupAdapterEndpoint(String url);

  /**
   * Get the URL of the producers portlet management Adapter.
   * 
   * @return URL of the portlet management Adapter.
   */
//  public String getPortletManagementAdapterEndpoint();

  /**
   * Set the URL of the producers portlet management Adapter.
   * 
   * @param url of the portlet management Adapter.
   */
//  public void setPortletManagementAdapterEndpoint(String url);

  /**
   * Get the producers portlet management Adapter.
   * 
   * @return portlet management Adapter.
   */
  public WSRPV2PortletManagementPortTypeAdapter getPortletManagementAdapter();

  /**
   * Get the URL of the producers registration Adapter.
   * 
   * @return URL of the registration Adapter.
   */
//  public String getRegistrationAdapterEndpoint();

  /**
   * Set the URL of the producers registration Adapter.
   */
//  public void setRegistrationAdapterEndpoint(String url);

  /**
   * Get the producers registration Adapter.
   * 
   * @return registration Adapter.
   */
  public WSRPV2RegistrationPortTypeAdapter getRegistrationAdapter();

  /**
   * Indicates wether or not the producer requires consumer registration.
   * 
   * @return True if consumer registration is required.
   */
  public boolean isRegistrationRequired();

  /**
   * Get the registration data the consumer uses to register at this producer.
   * 
   * @return The consumer registration data
   */
  public RegistrationData getRegistrationData();

  /**
   * Set the registration the consumer uses the register at this producer.
   * 
   * @param regData The registration data which is used to register at this
   *          producer
   */
  public void setRegistrationData(RegistrationData regData);

  /**
   * Get the service description of the producer
   * 
   * @param newRequest If set to true a new request is send to the producer
   *          otherwise a cached service description is used if available
   * @return Service description of the producer
   */
  public ServiceDescription getServiceDescription(boolean newRequest) throws WSRPException;

  /**
   * Same as getServiceDescription(false)
   */
  public ServiceDescription getServiceDescription() throws WSRPException;

  /**
   * Get the portlet description of the portlet with the given handle or null if
   * the producer doesn't know an portlet with this handle.
   * 
   * @param portletHandle The portlet handle of the portlet
   * @return The portlet description of the portlet with the given handle
   */
  public PortletDescription getPortletDescription(String portletHandle) throws WSRPException;

  /**
   * Get the current registration context of the consumer registered at this
   * producer or null if no registration is required or happend so far.
   * 
   * @return The current registration context of the consumer at this producer
   *         or null.
   */
  public RegistrationContext getRegistrationContext() throws WSRPException;

  /**
   * Method establishes a relationship between consumer and producer. <p/>
   * 
   * @param registrationData Data which is used to register the consumer
   * @return The registration context received by the producer
   */
  public RegistrationContext register(Register register) throws WSRPException;

  /**
   * Can be used to modify the relationship between consumer and producer. <p/>
   * 
   * @param registrationData The new registration data
   * @return New registration context
   */
  public RegistrationState modifyRegistration(ModifyRegistration modifyRegistration) throws WSRPException;

  /**
   * End an existing consumer producer relationship and remove the registration
   * context
   * 
   * @return Can be anything
   */
  public ReturnAny deregister(Deregister deregister) throws WSRPException;

  /**
   * Check wether the optional registration Adapter is supported
   * 
   * @return true if a registration Adapter endpoint URL is set
   */
  public boolean isRegistrationAdapterSupported();

  /**
   * Check wether the optional portlet management Adapter is supported
   * 
   * @return true if a portlet management Adapter endpoint URL is set
   */
  public boolean isPortletManagementInferfaceSupported();

  /**
   * Get producer URL.
   * 
   * @return
   */
  public URL getUrl();

}
