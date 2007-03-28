/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Aug 20, 2003
 * Time: 12:51:20 AM
 */
package org.exoplatform.services.portletcontainer.impl.portletAPIImp.tags;

import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;
import javax.servlet.jsp.tagext.TagData;

public class DefineObjectsExtraInfo extends TagExtraInfo{

  public VariableInfo[] getVariableInfo(TagData tagData) {
    return new VariableInfo[] {
      new VariableInfo("portletConfig", "javax.portlet.PortletConfig", true, VariableInfo.AT_END ),
      new VariableInfo("renderRequest", "javax.portlet.RenderRequest", true, VariableInfo.AT_END),
      new VariableInfo("renderResponse", "javax.portlet.RenderResponse", true, VariableInfo.AT_END),
    };
  }


}
