/*
 * Copyright (C) 2003-2009 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */

package org.exoplatform.services.wsrp2.consumer;

/**
 * A consumer based session which represents a user session with a certain
 * producer. This user session contains one or more group sessions.
 * 
 * @author <a href='mailto:peter.fischer@de.ibm.com'>Peter Fischer</a>
 * @author Benjamin Mestrallet
 * @see GroupSession
 */
public interface UserSessionMgr extends UserSession, InitCookieInfo {

}
