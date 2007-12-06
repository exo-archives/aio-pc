/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.portletcontainer.imp;

import java.io.UnsupportedEncodingException;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 25 ao�t 2004
 */
public class TestCharacterEncoding extends BaseTest{
  public TestCharacterEncoding(String s) {
    super(s);
  }

  public void testSetCharacterEncoding() throws UnsupportedEncodingException {
    String s  = "\u00e1zov portletu";
    System.out.println("STRING "+s);

    s = "\u0160t\u00fdl";
    System.out.println("STRING "+s);

    s = "\u8D1F\u8F7D\u6D4B\u8BD5";
    System.out.println(new String(s.getBytes(), "UTF-8"));

    s = "\u5355\u5143\u6D4B\u8BD5cc";
    System.out.println(new String(s.getBytes(), "UTF-8"));

    System.out.println("�tre aim� et d�t�st� to�t � la fois c'�st �a");

    s = "Septemberov\u00fd \u010dl\u00e1nok";
    System.out.println(s);
    System.out.println(new String(s.getBytes(), "UTF-8"));

    s = "�l�nok Spr�vy Testy za�a�enia";
    System.out.println(new String(s.getBytes(), "ISO-8859-2"));    
  }
}
