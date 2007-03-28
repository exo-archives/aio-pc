package org.exoplatform.services.portletcontainer.test.portlet;

import java.io.IOException;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 * benjamin.mestrallet@exoplatform.com
 */
public class PortletWithPortalAccess extends GenericPortlet{

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
      throws PortletException, IOException {
    actionResponse.setRenderParameter("status", "Everything is ok");
  }

}
