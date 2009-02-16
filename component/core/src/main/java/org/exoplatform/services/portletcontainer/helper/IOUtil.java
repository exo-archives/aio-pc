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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.ExoLogger;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey
 *         Zavizionov</a>
 * @version $Id: $ Aug 29, 2008
 */
public class IOUtil {
  private Log log = ExoLogger.getLogger(getClass().getName());

  static public byte[] serialize(Object... obj) throws Exception {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    ObjectOutputStream out = new ObjectOutputStream(bytes);
    for (Object object : obj) {
      out.writeObject(object);
    }
    out.close();
    byte[] result = bytes.toByteArray();
    return result;
  }

  static public Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
    if (bytes == null)
      return null;
    ByteArrayInputStream is = new ByteArrayInputStream(bytes);
    ObjectInputStream in = new ObjectInputStream(is);
    Object obj = in.readObject();
    in.close();
    return obj;
  }

  static public Object[] deserializeAll(byte[] bytes, int count) throws Exception {
    if (bytes == null)
      return null;
    List<Object> result = new ArrayList<Object>();
    ByteArrayInputStream is = new ByteArrayInputStream(bytes);
    ObjectInputStream in = new ObjectInputStream(is);
    for (int j = 0; j < count; j++) {
      Object obj = in.readObject();
      result.add(obj);
    }
    in.close();
    return result.toArray();
  }
  
  static public Object deserializeMore(byte[] bytes, int index) throws Exception {
    if (bytes == null)
      return null;
    ByteArrayInputStream is = new ByteArrayInputStream(bytes);
    ObjectInputStream in = new ObjectInputStream(is);
    Object obj = null;
    for (int j = 0; j <= index; j++) {
      obj = in.readObject();
    }
    in.close();
    return obj;
  }

}
