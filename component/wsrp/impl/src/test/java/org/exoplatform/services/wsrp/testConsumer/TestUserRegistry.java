/*
* Copyright 2001-2004 The eXo platform SARL All rights reserved.
* Please look at license.txt in info directory for more license detail.
*/

package org.exoplatform.services.wsrp.testConsumer;

/*
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 5 févr. 2004
 * Time: 18:31:05
 */

public class TestUserRegistry extends BaseTest{

  public void testAddUser(){
    userRegistry.addUser(createUser("userID"));
    assertTrue(userRegistry.getAllUsers().hasNext());
    assertNotNull(userRegistry.getUser("userID"));
  }

  public void testRemoveUser(){
    userRegistry.addUser(createUser("userID"));
    userRegistry.addUser(createUser("userID2"));
    userRegistry.addUser(createUser("userID3"));
    userRegistry.removeUser("userID3");
    assertNull(userRegistry.getUser("userID3"));
    userRegistry.removeAllUsers();
    assertFalse(userRegistry.getAllUsers().hasNext());
  }

}