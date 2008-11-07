package org.exoplatform.services.wsrp2.consumer;

import java.util.List;

import org.exoplatform.services.wsrp2.type.NamedString;
import org.exoplatform.services.wsrp2.type.StateChange;
import org.exoplatform.services.wsrp2.type.UploadContext;

/**
 * This is the abstraction for a interaction request at a integrated remote
 * portlet at the consumer side.
 * 
 * @author Benjamin Mestrallet
 */
public interface WSRPInteractionRequest extends WSRPBaseRequest {

  public String getInteractionState();

  public List<NamedString> getFormParameters();

  public List<UploadContext> getUploadContexts();

  public StateChange getPortletStateChange();

}
