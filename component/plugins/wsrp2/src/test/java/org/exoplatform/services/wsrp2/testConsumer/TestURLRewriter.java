/*
 * Copyright (C) 2003-2009 eXo Platform SAS.
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

import org.exoplatform.services.wsrp2.exceptions.WSRPException;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:alexey.zavizionov@exoplatform.com.ua">Alexey
 *         Zavizionov</a>
 * @version $Id: $ Mar 19, 2009
 */

public class TestURLRewriter extends BaseTest {

  String toRewriteOnConsumer = "wsrp_rewrite?portal:type=render&amp;portal:portletMode=wsrp:help&amp;"
                                 + "wsrp-navigationalState=rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAh3CAAAAAsAAAAAeA**"
                                 + "&amp;portal:windowState=wsrp:normal&amp;portal:isSecure=false/wsrp_rewrite";

  String expectedOnConsumer  = "baseURL&wsrp-navigationalState=rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAh3CAAAAAsAAAAAeA**&portal:isSecure=false&portal:type=render&portal:portletMode=help&portal:windowState=normal";

  String toRewriteOnProducer = "http://localhost:8080/portal/private/?portal:componentId=producer2-1725896432_12023eb9cac@demos_HelloWorld_92668751_-304808578_439329280&portal:type=blockingAction&portal:isSecure=false&wsrp-portletHandle=demos/HelloWorld&wsrp-userContextKey=dummyUserContextKey&wsrp-portletInstanceKey=&wsrp-sessionID=23ebcd5c7f000001000850f8d47e8851&wsrp-pageState=&wsrp-portletStates=&wsrp-fragmentID=&wsrp-extensions=&wsrp-interactionState=23ebea857f0000010073e11ab735bbcb&portal:portletMode=wsrp:edit&portal:windowState=wsrp:maximized&wsrp-navigationalState=23ebea7b7f0000010073e11a2d0e01f4&wsrp-navigationalValues=&action_param_2=action+param+test+2&action_param_1=action+param+test";

  String expectedOnProducer  = "http://localhost:8080/portal/private/?portal:componentId=producer2-1725896432_12023eb9cac@demos_HelloWorld_92668751_-304808578_439329280&portal:type=action&portal:isSecure=false&wsrp-portletHandle=demos/HelloWorld&wsrp-userContextKey=dummyUserContextKey&wsrp-portletInstanceKey=&wsrp-sessionID=23ebcd5c7f000001000850f8d47e8851&wsrp-pageState=&wsrp-portletStates=&wsrp-fragmentID=&wsrp-extensions=&wsrp-interactionState=23ebea857f0000010073e11ab735bbcb&portal:portletMode=edit&portal:windowState=maximized&wsrp-navigationalState=23ebea7b7f0000010073e11a2d0e01f4&wsrp-navigationalValues=&action_param_2=action+param+test+2&action_param_1=action+param+test";
  
  String baseURLOnProducer = "http://localhost:8080/portal/private/?portal:componentId=producer2-1725896432_12023eb9cac@demos_HelloWorld_92668751_-304808578_439329280";

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    log();
  }

  public void testRewriteOnConsumer() throws WSRPException {
    log();
    String result = urlRewriter.rewriteURLs("baseURL", toRewriteOnConsumer);

    assertNotNull(result);
    assertEquals(expectedOnConsumer, result);
  }

  public void testRewriteOnConsumer2() throws WSRPException {
    log();

    // POSTFIX TEST
    String result = urlRewriter.rewriteURLs("baseURL", toRewriteOnConsumer + "bla");

    assertNotNull(result);
    assertEquals(expectedOnConsumer + "bla", result);

    // PREFIX TEST
    result = urlRewriter.rewriteURLs("baseURL", "bla" + toRewriteOnConsumer);

    assertNotNull(result);
    assertEquals("bla" + expectedOnConsumer, result);

    // BOTH TEST
    result = urlRewriter.rewriteURLs("baseURL", "bla" + toRewriteOnConsumer + "bla");

    assertNotNull(result);
    assertEquals("bla" + expectedOnConsumer + "bla", result);

  }

  public void testRewriteOnProducer() throws WSRPException {
    log();
    String result = urlRewriter.rewriteURLAfterTemplateProcessing(baseURLOnProducer, toRewriteOnProducer);

    assertNotNull(result);
    assertEquals(expectedOnProducer, result);
  }

  public void testRewriteOnProducer2() throws WSRPException {
    log();
    // POSTFIX TEST
    String result = urlRewriter.rewriteURLAfterTemplateProcessing(baseURLOnProducer, toRewriteOnProducer
        + "bla");

    assertNotNull(result);
    assertEquals(expectedOnProducer + "bla", result);

    // PREFIX TEST
    result = urlRewriter.rewriteURLAfterTemplateProcessing(baseURLOnProducer, "bla" + toRewriteOnProducer);

    assertNotNull(result);
    assertEquals("bla" + expectedOnProducer, result);

    // BOTH TEST
    result = urlRewriter.rewriteURLAfterTemplateProcessing(baseURLOnProducer, "bla" + toRewriteOnProducer
        + "bla");

    assertNotNull(result);
    assertEquals("bla" + expectedOnProducer + "bla", result);
  }

}
