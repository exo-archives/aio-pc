package org.exoplatform.portlet.tapestry;

import java.util.Date;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.html.BasePage;

public class View extends BasePage {

  public void pageBeginRender(PageEvent event) {
  }

  public String getExoDate() {
    return (new Date()).toString();
  }

}
