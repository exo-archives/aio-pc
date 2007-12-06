package org.exoplatform.services.wsrp2.consumer;

import org.exoplatform.services.wsrp2.type.NamedString;
import org.exoplatform.services.wsrp2.type.StateChange;
import org.exoplatform.services.wsrp2.type.UploadContext;

/**
 * This is the abstraction for a interaction request at a integrated
 * remote portlet at the consumer side.
 *
 * @author Benjamin Mestrallet
 */
public interface WSRPInteractionRequest  extends WSRPBaseRequest {

  public String getInteractionState();

  public NamedString[] getFormParameters();

  public UploadContext[] getUploadContexts();

  public StateChange getPortletStateChange();
  
}
