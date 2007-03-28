/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 *  
 * Created on 14 janv. 2004
 */
package org.exoplatform.services.wsrp.exceptions;

/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public class WSRPException extends Exception {

  private String fault;

  public WSRPException() {
    this("unknown", null);
  }

  public WSRPException(String fault) {
    this(fault, null);
  }

  public WSRPException(String fault, Throwable t){      
    super("fault : " + fault, t);
    this.fault = fault;      
  }
  
  public String getFault() {
    return fault;
  }

}
