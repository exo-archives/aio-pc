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

import org.exoplatform.Constants;

/**
 * Jul 11, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UserDataConstraint.java,v 1.1 2004/07/13 02:31:13 tuan08 Exp $
 */
public class UserDataConstraint {
  private List description ;
	private String transportGuarantie ;
  // portlet api 2.0
  private String id ;

	public List getDescription() {
    if(description == null) return Constants.EMPTY_LIST ;
		return description;
	}

	public void setDescription(List description) {
		this.description = description;
	}

  public void addDescription(Description desc) {
  	if(this.description == null)  description = new ArrayList() ;
    this.description.add(desc);
  }

	public String getTransportGuarantie() {
		return transportGuarantie;
	}
	public void setTransportGuarantie(String transportGuarantie) {
		this.transportGuarantie = transportGuarantie;
	}

  public String getId() { return this.id ; }

  public void setId(String value) { this.id = value ; }
}
