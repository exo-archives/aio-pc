/*
* Copyright 2001-2004 The eXo platform SARL All rights reserved.
* Please look at license.txt in info directory for more license detail.
*/

package org.exoplatform.services.portletcontainer.impl.portletAPIImp.pool;


import javax.portlet.*;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.helpers.*;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 30 janv. 2004
 * Time: 15:03:19
 */

public class PortletObjectsWrapper {

  private ActionRequest actionRequest;
  private ActionResponse actionResponse;
  private RenderRequest renderRequest;
  private RenderResponse renderResponse;
  private CustomRequestWrapper customRequestWrapper;
  private CustomResponseWrapper customResponseWrapper;
  private PortletSession portletSession;
  private SharedSessionWrapper sharedSessionWrapper;

  public ActionRequest getActionRequest() {
    return actionRequest;
  }

  public void setActionRequest(ActionRequest actionRequest) {
    this.actionRequest = actionRequest;
  }

  public ActionResponse getActionResponse() {
    return actionResponse;
  }

  public void setActionResponse(ActionResponse actionResponse) {
    this.actionResponse = actionResponse;
  }

  public RenderRequest getRenderRequest() {
    return renderRequest;
  }

  public void setRenderRequest(RenderRequest renderRequest) {
    this.renderRequest = renderRequest;
  }

  public RenderResponse getRenderResponse() {
    return renderResponse;
  }

  public void setRenderResponse(RenderResponse renderResponse) {
    this.renderResponse = renderResponse;
  }

  public CustomRequestWrapper getCustomRequestWrapper() {
    return customRequestWrapper;
  }

  public void setCustomRequestWrapper(CustomRequestWrapper customRequestWrapper) {
    this.customRequestWrapper = customRequestWrapper;
  }

  public CustomResponseWrapper getCustomResponseWrapper() {
    return customResponseWrapper;
  }

  public void setCustomResponseWrapper(CustomResponseWrapper customResponseWrapper) {
    this.customResponseWrapper = customResponseWrapper;
  }

  public PortletSession getPortletSession() {
    return portletSession;
  }

  public void setPortletSession(PortletSession portletSession) {
    this.portletSession = portletSession;
  }

  public SharedSessionWrapper getSharedSessionWrapper() {
    return sharedSessionWrapper;
  }

  public void setSharedSessionWrapper(SharedSessionWrapper sharedSessionWrapper) {
    this.sharedSessionWrapper = sharedSessionWrapper;
  }

}