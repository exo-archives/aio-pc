/*
* Copyright 2001-2004 The eXo platform SARL All rights reserved.
* Please look at license.txt in info directory for more license detail.
*/

package org.exoplatform.services.portletcontainer.impl.portletAPIImp.pool;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.*;
import org.exoplatform.services.portletcontainer.impl.portletAPIImp.helpers.*;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 30 janv. 2004
 * Time: 14:59:22
 */

public class PortletObjectsWrapperFactory extends BasePoolableObjectFactory{

  private static PortletObjectsWrapperFactory ourInstance = null; //new PortletObjectsWrapperFactory();
  protected ExoContainer cont;

  public PortletObjectsWrapperFactory(ExoContainer cont) {
    super();
    this.cont = cont;
  }

  public static void createInstance(ExoContainer cont) {
    if (ourInstance == null)
      ourInstance = new PortletObjectsWrapperFactory(cont);
  }

  public static PortletObjectsWrapperFactory getInstance() {
    return ourInstance;
  }

  public PortletObjectsWrapper createObject(){
    PortletObjectsWrapper portletObjectsWrapper = new PortletObjectsWrapper();
    portletObjectsWrapper.setActionRequest(new ActionRequestImp(cont, new EmptyRequest()));
    portletObjectsWrapper.setActionResponse(new ActionResponseImp(cont, new EmptyResponse()));
    portletObjectsWrapper.setCustomRequestWrapper(new CustomRequestWrapper(new EmptyRequest()));
    portletObjectsWrapper.setCustomResponseWrapper(new CustomResponseWrapper(new EmptyResponse()));
    portletObjectsWrapper.setRenderRequest(new RenderRequestImp(cont, new EmptyRequest()));
    portletObjectsWrapper.setRenderResponse(new RenderResponseImp(cont, new EmptyResponse()));
    portletObjectsWrapper.setPortletSession(new PortletSessionImp(cont));
    portletObjectsWrapper.setSharedSessionWrapper(new SharedSessionWrapper(new EmptySession()));
    return portletObjectsWrapper;
  }

  public Object makeObject() throws Exception {
    return createObject();
  }

  public void passivateObject(Object o) throws Exception {
    PortletObjectsWrapper portletObjectsWrapper = (PortletObjectsWrapper) o;
    ((ActionRequestImp)portletObjectsWrapper.getActionRequest()).
        emptyActionRequest();
    ((ActionResponseImp) portletObjectsWrapper.getActionResponse()).
        emptyActionResponse();
    portletObjectsWrapper.getCustomRequestWrapper().emptyCustomRequestWrapper();
    portletObjectsWrapper.getCustomResponseWrapper().emptyResponseWrapper();
    ((RenderRequestImp)portletObjectsWrapper.getRenderRequest()).emptyRenderRequest();
    ((RenderResponseImp)portletObjectsWrapper.getRenderResponse()).emptyPortletResponse();
    ((RenderResponseImp)portletObjectsWrapper.getRenderResponse()).emptyPortletResponse();
    portletObjectsWrapper.getSharedSessionWrapper().emptySharedSessionWrapper();
  }

}