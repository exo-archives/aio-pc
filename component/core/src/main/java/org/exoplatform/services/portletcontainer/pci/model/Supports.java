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
package org.exoplatform.services.portletcontainer.pci.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Jul 11, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: Supports.java,v 1.1 2004/07/13 02:31:13 tuan08 Exp $
 */
public class Supports {
	private String	mimeType;
	private List<String>		portletMode;
	// portlet api 2.0
	private String	id;
  private List<String>    windowState;

  public Supports() {
  	portletMode = new ArrayList<String>() ;
    windowState = new ArrayList<String>();
  }

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public List<String> getPortletMode() {
		return portletMode;
	}

	public void setPortletMode(List<String> portletMode) {
		this.portletMode = portletMode;
	}

  public void addPortletMode(String mode) {
  	this.portletMode.add(mode) ;
  }

  public String getId() { return this.id ; }

  public void setId(String value) { this.id = value ; }

  public List<String> getWindowState() {
    return windowState;
  }

  public void setWindowState(List<String> windowState) {
    this.windowState = windowState;
  }

  public void addWindowState(String state) {
    this.windowState.add(state) ;
  }

}