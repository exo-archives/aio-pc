/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.wsrp.producer.impl;

import org.exoplatform.commons.utils.IOUtil;
/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Jun 14, 2003
 * Time: 1:12:22 PM
 *
 * @hibernate.class  table="WSRP_STATE"
 */
public class StateData {
  private String id_ ;
  private String type_ ;
  transient private Object object_ ;
  
  public StateData() {
  }    

  /**
   * @hibernate.id  generator-class="assigned" unsaved-value="null"
   ***/
  public String   getId() { return id_ ; }
  public void     setId(String s) { id_ = s ; }

  /**
   * @hibernate.property
   **/
  public String   getDataType() { return type_ ; }
  public void     setDataType(String s) { type_ = s ; }

  /**
   * @hibernate.property type="binary"
   **/
  public byte[] getData() throws Exception { 
    return IOUtil.serialize(object_) ; 
  }   
  public void setData(byte[] data) throws Exception { 
    object_ = IOUtil.deserialize(data) ; 
  }

  public Object getDataObject() { return object_  ; }
  public void setDataObject(Object o) { object_ = o ; }

}
