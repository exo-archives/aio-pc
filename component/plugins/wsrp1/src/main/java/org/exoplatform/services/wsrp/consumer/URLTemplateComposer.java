package org.exoplatform.services.wsrp.consumer;

/**
 * <p>
 * This interface provides methods to generate URL templates.
 * </p>
 * <p>
 * The generated templates will be transmitted to Producers (or respectively
 * portlets) that are willing to properly write URLs for a Consumer. (With
 * templates the Consumer indicates how it needs URLs formatted in order to
 * process them properly.)
 * </p>
 * 
 * @author <a href="mailto:stefan.behl@de.ibm.com">Stefan Behl</a>
 * @author Benjamin Mestrallet
 */
public interface URLTemplateComposer {

  public void setHost(String host);

  public void setPort(int port);

  /**
   * Creates a blocking action template. Includes tokens for url-type,
   * portletMode, navigationalState, interactionState and windowState to be
   * replaced by the producer.
   * 
   * @return String representing the entire template.
   * @param path
   */
  public String createBlockingActionTemplate(String path);

  /**
   * Creates a secure blocking action template. Includes tokens for url-type,
   * portletMode, navigationalState, interactionState, windowState and secureURL
   * to be replaced by the producer.
   * 
   * @return String representing the entire template.
   */
  public String createSecureBlockingActionTemplate(String path);

  /**
   * Creates a render template. Includes tokens for url-type, portletMode,
   * navigationalState, interactionState and windowState to be replaced by the
   * producer.
   * 
   * @return String representing the entire template.
   */
  public String createRenderTemplate(String path);

  /**
   * Creates a secure render template. Includes tokens for url-type,
   * portletMode, navigationalState, interactionState, windowState and secureURL
   * to be replaced by the producer.
   * 
   * @return String representing the entire template.
   */
  public String createSecureRenderTemplate(String path);

  /**
   * Creates a resource template. Includes tokens for url-type, rewriteResource
   * and url to be replaced by the producer.
   * 
   * @return String representing the entire template.
   */
  public String createResourceTemplate(String path);

  /**
   * Creates a secure resource template. Includes tokens for url-type, url,
   * rewriteResource, and secureURL to be replaced by the producer.
   * 
   * @return String representing the entire template.
   */
  public String createSecureResourceTemplate(String path);

  /**
   * Creates a default template. Includes tokens for url-type, portletMode,
   * navigationalState, interactionState, windowState, url, rewriteResource and
   * secureURL to be replaced by the producer.
   * 
   * @return String representing the entire template.
   */
  public String createDefaultTemplate(String path);

  /**
   * Creates a secure default template. Includes tokens for url-type,
   * portletMode, navigationalState, interactionState, windowState, url,
   * rewriteResource and secureURL to be replaced by the producer.
   * 
   * @return String representing the entire template.
   */
  public String createSecureDefaultTemplate(String path);

  /**
   * Get the consumers namespace prefix which is used by the portlet to
   * namespace tokens which need to be unique on a aggregated page.
   * 
   * @return The namespace prefix of the consumer.
   */
  public String getNamespacePrefix();
}
