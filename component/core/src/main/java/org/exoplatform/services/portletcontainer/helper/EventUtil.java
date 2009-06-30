/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
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
package org.exoplatform.services.portletcontainer.helper;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

import org.exoplatform.services.log.Log;
import org.exoplatform.services.log.ExoLogger;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey
 *         Zavizionov</a>
 * @version $Id: $ Aug 29, 2008
 */
public class EventUtil extends IOUtil {
  private Log log = ExoLogger.getLogger(getClass());

  static public Object deserializeInContextClassloader(byte[] bytes) throws Exception {
    if (bytes == null)
      return null;
    ByteArrayInputStream is = new ByteArrayInputStream(bytes);
    ObjectInputStream in = new ObjectInputStream(is) {
      protected Class resolveClass(ObjectStreamClass objectStreamClass) throws ClassNotFoundException {
        return Class.forName(objectStreamClass.getName(), true, Thread.currentThread()
                                                                      .getContextClassLoader());
      }
    };
    Object obj = in.readObject();
    in.close();
    return obj;
  }

}
