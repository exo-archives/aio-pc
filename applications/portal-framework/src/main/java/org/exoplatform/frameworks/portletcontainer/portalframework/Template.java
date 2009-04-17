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
package org.exoplatform.frameworks.portletcontainer.portalframework;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.exoplatform.commons.xml.ExoXMLSerializer;
import org.exoplatform.commons.xml.ExoXPPParser;
import org.exoplatform.frameworks.portletcontainer.portalframework.layout.LayoutColumn;
import org.exoplatform.frameworks.portletcontainer.portalframework.layout.LayoutHtmlTag;
import org.exoplatform.frameworks.portletcontainer.portalframework.layout.LayoutHtmlText;
import org.exoplatform.frameworks.portletcontainer.portalframework.layout.LayoutItem;
import org.exoplatform.frameworks.portletcontainer.portalframework.layout.LayoutNode;
import org.exoplatform.frameworks.portletcontainer.portalframework.layout.LayoutPlt;
import org.exoplatform.frameworks.portletcontainer.portalframework.layout.LayoutZone;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * Created by The eXo Platform SAS .
 * @author <a href="mailto:roman.pedchenko@exoplatform.com.ua">Roman Pedchenko</a>
 * @version $Id: $
*/
public class Template {

  public interface ColumnIterator {
    boolean processColumn(LayoutColumn lc);
  }

  /**
   * @param tmpl input stream with template
   * @return list of required portlets
   * @throws Exception exception
   */
  public static List<LayoutPlt> getPortletList(List<LayoutItem> layout) throws Exception {
    List<LayoutPlt> plts = new ArrayList<LayoutPlt>();
    checkList(layout, plts);
    return plts;
  }

  private static void checkList(List<LayoutItem> layout, List<LayoutPlt> plts) {
    if (layout == null)
      return;
    for (Iterator<LayoutItem> i = layout.iterator(); i.hasNext();) {
      LayoutItem item = i.next();
      if (item instanceof LayoutPlt)
        plts.add((LayoutPlt) item);
      else if (item instanceof LayoutNode)
        checkList(((LayoutNode) item).getChildren(), plts);
    }
  }

  /**
   * @param tmpl input stream with template
   * @return layout columns
   * @throws Exception exception
   */
  public static List<LayoutItem> getPortletLayout(InputStream tmpl) throws Exception {
    if (tmpl == null)
      return null;
    List<LayoutItem> layout = new ArrayList<LayoutItem>();
    ExoXPPParser xpp = ExoXPPParser.getInstance();
    xpp.setInput(tmpl, "UTF8");
    xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
    xpp.mandatoryNode("layout");

    parseChildren(layout, xpp, false);
    tmpl.close();
    return layout;
  }

  private static boolean isTag(XmlPullParser xpp, String tag) throws XmlPullParserException, IOException {
    while (xpp.getEventType() != XmlPullParser.START_TAG && xpp.getEventType() != XmlPullParser.END_TAG
        && xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
      xpp.next();
    }
    if (xpp.getEventType() != XmlPullParser.START_TAG)
      return false;
    return xpp.getName().equals(tag);
  }

  private static void skipTillTagEnd(XmlPullParser xpp, String tag) throws XmlPullParserException, IOException {
    while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
      if (xpp.getEventType() == XmlPullParser.END_TAG && xpp.getName().equals(tag))
        break;
      xpp.next();
    }
  }

  private static void nextNode(XmlPullParser xpp) throws XmlPullParserException, IOException {
    if (xpp.getEventType() != XmlPullParser.END_DOCUMENT)
      xpp.next();
  }

  private static void parseChildren(List<LayoutItem> list, ExoXPPParser xpp, boolean insideHtml) throws XmlPullParserException,
      IOException,
      Exception {
    while (xpp.getEventType() != XmlPullParser.END_DOCUMENT && xpp.getEventType() != XmlPullParser.END_TAG) {
      if (xpp.getEventType() == XmlPullParser.TEXT) {
        if (insideHtml)
          list.add(new LayoutHtmlText(xpp.getText()));
      } else if (xpp.getPrefix() != null) {
        if (xpp.getPrefix().equals("h")) {
          if (xpp.getEventType() == XmlPullParser.START_TAG) {
            Map<String, String> attrs = new HashMap<String, String>();
            for (int i = 1; i <= xpp.getAttributeCount(); i++)
              attrs.put(xpp.getAttributeName(i - 1), xpp.getAttributeValue(i - 1));
            LayoutHtmlTag h1 = new LayoutHtmlTag(xpp.getName(), attrs);
            list.add(h1);
            nextNode(xpp);
            if (xpp.getEventType() != XmlPullParser.END_TAG) {
              List<LayoutItem> ch = new ArrayList<LayoutItem>();
              parseChildren(ch, xpp, true);
              h1.setChildren(ch);
            }
          }
        }
      } else if (isTag(xpp, "header")) {
        list.add(new LayoutItem(LayoutItem.HEADER));
        skipTillTagEnd(xpp, "header");
      } else if (isTag(xpp, "footer")) {
        list.add(new LayoutItem(LayoutItem.FOOTER));
        skipTillTagEnd(xpp, "footer");
      } else if (isTag(xpp, "pages")) {
        list.add(new LayoutItem(LayoutItem.PAGES));
        skipTillTagEnd(xpp, "pages");
      } else if (isTag(xpp, "zone")) {
        LayoutZone zone = new LayoutZone();
        list.add(zone);
        nextNode(xpp);
        while (isTag(xpp, "col")) {
          LayoutColumn col = new LayoutColumn(xpp.getAttributeValue(null, "id"), xpp.getAttributeValue(null, "width"),
              xpp.getAttributeValue(null, "default") != null);
          zone.addChild(col);
          nextNode(xpp);
          while (isTag(xpp, "portlet")) {
            LayoutPlt lp = new LayoutPlt(xpp.getAttributeValue(null, "app"), xpp.getAttributeValue(null, "name"),
                xpp.getAttributeValue(null, "id"));
            col.addChild(lp);
            skipTillTagEnd(xpp, "portlet");
            nextNode(xpp);
          }
          skipTillTagEnd(xpp, "col");
          nextNode(xpp);
        }
        skipTillTagEnd(xpp, "zone");
      }
      nextNode(xpp);
    }
  }

  public static void saveLayoutAs(List<LayoutItem> layout, OutputStream tmpl) throws IOException {
    ExoXMLSerializer ser = ExoXMLSerializer.getInstance();
    ser.setOutput(tmpl, "utf8");
    ser.startDocument("utf8", false);
    ser.setPrefix("", "http://www.exoplatform.org/pc2/lp");
    ser.setPrefix("h", "http://www.w3.org/1999/xhtml");
    ser.startTag(null, "layout");
    saveList(layout, ser);
    ser.endTag(null, "layout");
    ser.flush();
    tmpl.close();
  }

  private static void saveList(List<LayoutItem> layout, ExoXMLSerializer ser) throws IOException {
    if (layout == null)
      return;
    for (Iterator<LayoutItem> i = layout.iterator(); i.hasNext();) {
      LayoutItem item = i.next();
      if (item instanceof LayoutPlt) {
        LayoutPlt i1 = (LayoutPlt) item;
        ser.startTag(null, "portlet");
        ser.attribute(null, "app", i1.getApp());
        ser.attribute(null, "name", i1.getName());
        ser.attribute(null, "id", i1.getId());
        ser.endTag(null, "portlet");
      } else if (item instanceof LayoutZone) {
        LayoutZone i1 = (LayoutZone) item;
        ser.startTag(null, i1.getName());
        saveList(i1.getChildren(), ser);
        ser.endTag(null, i1.getName());
      } else if (item instanceof LayoutColumn) {
        LayoutColumn i1 = (LayoutColumn) item;
        ser.startTag(null, i1.getName());
        ser.attribute(null, "id", i1.getId());
        if (!i1.getWidth().equals(""))
          ser.attribute(null, "width", i1.getWidth());
        if (i1.isDefaultCol())
          ser.attribute(null, "default", "true");
        saveList(((LayoutNode) item).getChildren(), ser);
        ser.endTag(null, i1.getName());
      } else if (item instanceof LayoutHtmlTag) {
        LayoutHtmlTag i1 = (LayoutHtmlTag) item;
        if (i1 instanceof LayoutHtmlText)
          ser.text(i1.getName());
        else {
          ser.startTag(null, "h:" + i1.getName());
          for (Map.Entry<String, String> attr : i1.getAttrs().entrySet())
            ser.attribute(null, attr.getKey(), attr.getValue());
          saveList(((LayoutNode) item).getChildren(), ser);
          ser.endTag(null, "h:" + i1.getName());
        }
      } else {
        ser.startTag(null, item.getName());
        ser.endTag(null, item.getName());
      }
    }
  }

  public static void delLayoutPltById(List<LayoutItem> layout, String id) {
    delLayoutPltById2(null, layout, id);
  }

  private static void delLayoutPltById2(LayoutNode parent, List<LayoutItem> children, String id) {
    if (children == null)
      return;
    for (Iterator<LayoutItem> i = children.iterator(); i.hasNext();) {
      LayoutItem item = i.next();
      if (item instanceof LayoutPlt && ((LayoutPlt) item).getId().equals(id)) {
        if (parent != null)
          parent.removeChild(item);
        else
          children.remove(item);
        return;
      } else if (item instanceof LayoutNode)
        delLayoutPltById2(((LayoutNode) item), ((LayoutNode) item).getChildren(), id);
    }
  }

  public static void addPortletToLayout(List<LayoutItem> layout, final LayoutPlt newPlt) {
    if (layout == null)
      return;
    iterateByLayoutColumns(layout,
        new ColumnIterator() {
          public boolean processColumn(LayoutColumn llc) {
            if (llc.isDefaultCol()) {
              llc.addChild(newPlt);
              return true;
            }
            return false;
          }
    });
System.out.println("framework/template: can't find any column to add portlet");
  }

  private static LayoutColumn tempLc = null;

  public static LayoutColumn findLayoutColumnById(List<LayoutItem> layout, final String id) {
    iterateByLayoutColumns(layout,
        new ColumnIterator() {
          public boolean processColumn(LayoutColumn llc) {
            if (llc.getId().equals(id)) {
              tempLc = llc;
              return true;
            }
            return false;
          }
    });
    return tempLc;
  }

  protected static boolean iterateByLayoutColumns(List<LayoutItem> layout, ColumnIterator ci) {
    if (ci == null)
      return true;
    if (layout == null)
      return false;
    for (Iterator<LayoutItem> i = layout.iterator(); i.hasNext();) {
      LayoutItem item = i.next();
      if (item instanceof LayoutColumn) {
        if (ci.processColumn((LayoutColumn) item))
          return true;
      } else if (item instanceof LayoutNode)
        if (iterateByLayoutColumns(((LayoutNode) item).getChildren(), ci))
          return true;
    }
    return false;
  }

  public static void changeLayoutWith(List<LayoutItem> layout, String newLayout) {
    String[] cols = newLayout.split("\n");
    for (String col : cols) {
      String[] colData = col.split(":");
      LayoutColumn lc = findLayoutColumnById(layout, colData[0].trim());
      if (lc != null) {
        if (colData[1] != null && !colData[1].trim().equals(""))
          for (String plt : colData[1].trim().split(" "))
            moveLayoutPortlet(layout, plt.trim(), lc);
      }
    }
  }

  private static void moveLayoutPortlet(List<LayoutItem> layout, final String pltId, final LayoutColumn lc) {
    iterateByLayoutColumns(layout,
        new ColumnIterator() {
          public boolean processColumn(LayoutColumn llc) {
            if (llc.getChildren() != null)
              for (Iterator<LayoutItem> i = llc.getChildren().iterator(); i.hasNext(); ) {
                LayoutPlt lp = (LayoutPlt) i.next();
                if (lp.getId().equals(pltId)) {
                  llc.removeChild(lp);
                  lc.addChild(lp);
                  return true;
                }
              }
            return false;
          }
    });
  }

}
