/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.wsrp.consumer.impl;

import org.exoplatform.commons.utils.IOUtil;
import org.exoplatform.services.wsrp.consumer.Producer;
/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Jun 14, 2003
 * Time: 1:12:22 PM
 *
 * @hibernate.class  table="WSRP_PRODUCER"
 */
public class ProducerData {
  private String id_ ;
  transient private Producer producer_ ;
  
  public ProducerData() {
  }
  
  /**
   * @hibernate.id  generator-class="assigned"
   **/
  public String getId() { return id_ ; }
  public void   setId(String id) { id_ = id ; }
  
  /**
   * @hibernate.property type="binary"
   **/
  public byte[] getData() throws Exception { 
    return IOUtil.serialize(producer_) ; 
  } 
  
  public void setData(byte[] data) throws Exception { 
    producer_ = (Producer) IOUtil.deserialize(data) ; 
  }  

  public Producer getProducer() { return producer_  ; }
  public void     setProducer(Producer p) { producer_ = p ; }

}
