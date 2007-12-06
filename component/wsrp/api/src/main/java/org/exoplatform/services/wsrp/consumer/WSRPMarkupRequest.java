package org.exoplatform.services.wsrp.consumer;

import org.exoplatform.services.wsrp.type.MarkupContext;

/**
 * This is the abstraction for a render request at a integrated
 * remote portlet at the consumer side.
 *
 * @author Benjamin Mestrallet
 */
public interface WSRPMarkupRequest extends WSRPBaseRequest {
  /**
   * Get the markup context if there is cached markup for this portlet instance
   * or null in case of an empty markup cache.
   *
   * @return The cached markup context
   */
  public MarkupContext getCachedMarkup();
}
