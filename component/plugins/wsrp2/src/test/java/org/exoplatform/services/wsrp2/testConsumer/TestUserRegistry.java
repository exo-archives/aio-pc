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

package org.exoplatform.services.wsrp2.testConsumer;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 5 févr. 2004
 * Time: 18:31:05
 */

public class TestUserRegistry extends BaseTest {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    System.out.println(">>>>>>>>>>>>>>> TestUserRegistry.setUp()");
  }

  public void testAddUser() {
    userRegistry.addUser(createUser("userID"));
    assertTrue(userRegistry.getAllUsers().hasNext());
    assertNotNull(userRegistry.getUser("userID"));
  }

  public void testRemoveUser() {
    userRegistry.addUser(createUser("userID"));
    userRegistry.addUser(createUser("userID2"));
    userRegistry.addUser(createUser("userID3"));
    userRegistry.removeUser("userID3");
    assertNull(userRegistry.getUser("userID3"));
    userRegistry.removeAllUsers();
    assertFalse(userRegistry.getAllUsers().hasNext());
  }

}
