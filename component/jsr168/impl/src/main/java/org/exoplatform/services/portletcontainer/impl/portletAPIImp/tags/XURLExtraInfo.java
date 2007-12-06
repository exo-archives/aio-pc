/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Aug 20, 2003
 * Time: 3:04:57 PM
 */
package org.exoplatform.services.portletcontainer.impl.portletAPIImp.tags;

import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;
import javax.servlet.jsp.tagext.TagData;

public class XURLExtraInfo extends TagExtraInfo{

  public VariableInfo[] getVariableInfo(TagData tagData) {
    String var = tagData.getAttributeString("var");
    if(var != null){
      return new VariableInfo[] {            
            new VariableInfo((String)tagData.getAttribute("var"),                       
                             "java.lang.String",
                             true,
                             VariableInfo.AT_END )
                             };  
    }
    return new VariableInfo[] {};
  }
}
