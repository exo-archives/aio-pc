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
package org.exoplatform.services.wsrp2.test;

import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.wsrp2.peristence.WSRPPersisterJCRrepository;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey
 *         Zavizionov</a>
 * @version $Id: $ Feb 3, 2009
 */
public class TestWSRPPersisterJCRrepository extends WSRPPersisterTestBase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    RepositoryService repositoryService = (RepositoryService) container.getComponentInstanceOfType(RepositoryService.class);
    assertNotNull(repositoryService);

    wsrpPersister = new WSRPPersisterJCRrepository(repositoryService);
  }

  @Override
  public void testPut() throws Exception {
    super.testPut();
  }

  @Override
  public void testRemove() throws Exception {
    super.testRemove();
  }

  @Override
  public void testRemoveEmpty() throws Exception {
    super.testRemoveEmpty();
  }

  @Override
  public void testPutAnother() throws Exception {
    super.testPutAnother();
  }

  @Override
  public void testRemoveAllEmpty() throws Exception {
    super.testRemoveAllEmpty();
  }

  @Override
  public void testRemoveAll() throws Exception {
    super.testRemoveAll();
  }

  @Override
  public void testLoadAll() throws Exception {
    super.testLoadAll();
  }

  @Override
  public void tearDown() throws Exception {
    super.tearDown();
  }

}
