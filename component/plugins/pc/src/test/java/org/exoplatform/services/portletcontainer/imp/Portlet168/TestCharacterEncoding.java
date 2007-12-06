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
package org.exoplatform.services.portletcontainer.imp.Portlet168;

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
