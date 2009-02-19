/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.portlet.PortletMode;
import javax.portlet.RenderResponse;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.services.portletcontainer.PCConstants;
import org.exoplatform.services.portletcontainer.pci.RenderOutput;
import org.exoplatform.services.portletcontainer.pci.model.Supports;

/**
 * Author : Mestrallet Benjamin benjmestrallet@users.sourceforge.net .
 * Date: Jul 29, 2003
 * Time: 5:52:27 PM
 */
public class RenderResponseImp extends MimeResponseImp implements RenderResponse {

  /**
   * Supported contents.
   */
  private final Collection<String> supportedContents;

  /**
   * @param resCtx response context
   */
  public RenderResponseImp(final ResponseContext resCtx) {
    super(resCtx);
    this.supportedContents = resCtx.getSupportedContents();
  }

  /**
   * Overridden method.
   *
   * @param s title
   * @see javax.portlet.RenderResponse#setTitle(java.lang.String)
   */
  public final void setTitle(final String s) {
    ((RenderOutput) getOutput()).setTitle(s);
  }

  /**
   * Overridden method.
   *
   * @param portletModes portlet modes
   * @see javax.portlet.RenderResponse#setNextPossiblePortletModes(java.util.Collection)
   */
  public final void setNextPossiblePortletModes(final Collection<PortletMode> portletModes) {
    ((RenderOutput) getOutput()).setNextPossiblePortletModes(portletModes);
  }

  /**
   * Overridden method.
   *
   * @param contentType
   * @see org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.MimeResponseImp#setContentType(java.lang.String)
   */
  public void setContentType(String contentType) {
    if (contentType != null)
      contentType = StringUtils.split(contentType, ';')[0];
    if (!isContentTypeSupported(contentType))
      throw new IllegalArgumentException("the content type : " + contentType + " is not supported.");
    super.setContentType(contentType);
  }

  /**
   * @param contentTypeToTest content type to test
   * @return is content type supported
   */
  private boolean isContentTypeSupported(final String contentTypeToTest) {
    Collection<String> c = getResponseContentTypes();
    for (String element : c)
      if (element.equals(contentTypeToTest))
        return true;
    return false;
  }

  /**
   * @return response content types
   */
  private Collection<String> getResponseContentTypes() {
    // TODO could be shared with PortletRequest.getResponseContentType()
    Collection<String> result = new ArrayList<String>();
    result.add(getResponseContentType());
    for (String element : supportedContents) {
      List<Supports> l = getPortletDatas().getSupports();
      for (int i = 0; i < l.size(); i++) {
        Supports supportsType = l.get(i);
        String mimeType = supportsType.getMimeType();
        if (element.equals(mimeType) && !element.equals(getInput().getMarkup())) {
          List<String> portletModes = supportsType.getPortletMode();
          for (String portletMode : portletModes)
            if (portletMode.equals(getInput().getPortletMode().toString())) {
              result.add(mimeType);
              break;
            }
        }
      }
    }
    return result;
  }

  /**
   * @return response content type
   */
  private String getResponseContentType() {
    List<Supports> l = getPortletDatas().getSupports();
    for (int i = 0; i < l.size(); i++) {
      Supports supportsType = l.get(i);
      String mimeType = supportsType.getMimeType();
      if (mimeType.equals(getInput().getMarkup())) {
        List<String> portletModes = supportsType.getPortletMode();
        for (String portletMode : portletModes)
          if (portletMode.equals(getInput().getPortletMode().toString()))
            return mimeType;
      }
    }
    return PCConstants.XHTML_MIME_TYPE;
  }

}
