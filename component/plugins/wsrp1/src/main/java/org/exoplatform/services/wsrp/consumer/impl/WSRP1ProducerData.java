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

package org.exoplatform.services.wsrp.consumer.impl;

import org.exoplatform.commons.utils.IOUtil;
import org.exoplatform.services.wsrp.consumer.Producer;

/**
 * Author : Tuan Nguyen tuan08@users.sourceforge.net Date: Jun 14, 2003 Time:
 * 1:12:22 PM
 * 
 * @hibernate.class table="WSRP_PRODUCER"
 */
public class WSRP1ProducerData {
  private String             id_;

  transient private Producer producer_;

  public WSRP1ProducerData() {
  }

  /**
   * @hibernate.id generator-class="assigned"
   */
  public String getId() {
    return id_;
  }

  public void setId(String id) {
    id_ = id;
  }

  /**
   * @hibernate.property type="binary"
   */
  public byte[] getData() throws Exception {
    return IOUtil.serialize(producer_);
  }

  public void setData(byte[] data) throws Exception {
    producer_ = (Producer) IOUtil.deserialize(data);
  }

  public Producer getProducer() {
    return producer_;
  }

  public void setProducer(Producer p) {
    producer_ = p;
  }

}
