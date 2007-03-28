package org.exoplatform.services.wsrp.consumer;

import org.exoplatform.services.wsrp.type.NamedString;

/**
 * This is the abstraction for a interaction request at a integrated
 * remote portlet at the consumer side.
 *
 * @author Benjamin Mestrallet
 */
public interface InteractionRequest extends WSRPBaseRequest {
  /**
   * Get all the interaction state
   *
   * @return Interaction state
   */
  public String getInteractionState();

  /**
   * Get all name/value pairs aa result of processing an form
   *
   * @return Array of name/value pairs
   */
  public NamedString[] getFormParameters();
}
