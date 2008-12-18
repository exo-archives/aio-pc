package org.exoplatform.services.wsrp1.consumer;

import java.util.Map;

/**
 * This interface provides methods to query the consumer's urls. These methods
 * could be used to implement consumer url rewriting.
 * 
 * @author <a href="mailto:stefan.behl@de.ibm.com">Stefan Behl</a>
 * @author Benjamin Mestrallet
 */
public interface URLGenerator {

  /**
   * Creates a URL pointing to the consumer,triggering a
   * performBlockingInteraction call.
   */
  public String getBlockingActionURL(String baseURL, Map<String, String> params);

  /**
   * Creates a URL pointing to the consumer,triggering a getMarkup call.
   */
  public String getRenderURL(String baseURL, Map<String, String> params);

  /**
   * Creates a URL pointing to the consumer,triggering the consumer to fetch a
   * certain resource
   */
  public String getResourceURL(String baseURL, Map<String, String> params);

  /**
   * Creates a 'url' that the consumer can use to namespace tokens.
   */
  public String getNamespacedToken(String token);

}
