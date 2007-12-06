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
package org.exoplatform.services.portletcontainer.config;

/**
 * Jul 7, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: Global.java,v 1.1 2004/07/08 19:11:45 tuan08 Exp $
 */
public class Global {
	private String name ;
	private String description ;
	private Integer majorVersion ;
	private Integer minorVersion ;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getMajorVersion() {
		return majorVersion.intValue();
	}

	public void setMajorVersion(int majorVersion) {
		this.majorVersion = new Integer(majorVersion);
	}

	public int getMinorVersion() {
		return minorVersion.intValue();
	}

	public void setMinorVersion(int minorVersion) {
		this.minorVersion = new Integer(minorVersion);
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
