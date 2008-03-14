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

import javax.portlet.ResourceResponse;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.services.portletcontainer.pci.ResourceOutput;

/**
 * Created by The eXo Platform SAS.
 * Author : Roman Pedchenko roman.pedchenko@exoplatform.com.ua
 */
public class ResourceResponseImp extends MimeResponseImp implements ResourceResponse {

  /**
   * @param resCtx response context
   */
  public ResourceResponseImp(final ResponseContext resCtx) {
    super(resCtx);
  }

  /**
   * Overridden method.
   *
   * @param contentType content type
   * @see org.exoplatform.services.portletcontainer.plugins.pc.portletAPIImp.MimeResponseImp#setContentType(java.lang.String)
   */
  public void setContentType(String contentType) {
    if ((contentType != null) && (contentType.indexOf(';') >= 0)) {
      String s = StringUtils.split(contentType, ';')[1].trim();
      contentType = StringUtils.split(contentType, ';')[0].trim();
      if (s.toLowerCase().startsWith("charset="))
        setCharacterEncoding(s.substring(8));
    }
    super.setContentType(contentType);
  }

  /**
   * Overridden method.
   *
   * @param charset charset
   * @see javax.servlet.ServletResponseWrapper#setCharacterEncoding(java.lang.String)
   */
  public void setCharacterEncoding(final String charset) {
    ((ResourceOutput) getOutput()).setCharacterEncoding(charset);
  }

}
