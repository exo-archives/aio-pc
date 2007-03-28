/*
 * Created by IntelliJ IDEA.
 * User: azer
 * Date: 25 janv. 2004
 * Time: 17:47:02
 */
package org.exoplatform.services.wsrp.producer;

import org.exoplatform.services.portletcontainer.pci.PortletData;
import org.exoplatform.services.wsrp.exceptions.WSRPException;
import org.exoplatform.services.wsrp.producer.impl.helpers.WSRPHttpSession;
import org.exoplatform.services.wsrp.type.CacheControl;
import org.exoplatform.services.wsrp.type.Templates;
import org.exoplatform.services.wsrp.type.UserContext;

public interface TransientStateManager {

  //public static int SESSION_TIME_PERIOD = 900; //seconds

  public WSRPHttpSession resolveSession(String sessionID, String user, Integer sessiontimeperiod) throws WSRPException;
  public void releaseSession(String sessionID);

  public CacheControl getCacheControl(PortletData portletDatas)
      throws WSRPException;
  public boolean validateCache(String validateTag) throws WSRPException;

  public Templates getTemplates(WSRPHttpSession session);
  public void storeTemplates(Templates templates, WSRPHttpSession session);

  public UserContext reolveUserContext(UserContext userContext, WSRPHttpSession session);
}