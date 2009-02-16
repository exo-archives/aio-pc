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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.portlet.Event;
import javax.portlet.PortletMode;
import javax.portlet.ResourceURL;
import javax.portlet.WindowState;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.namespace.QName;

import org.exoplatform.Constants;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.services.portletcontainer.PCConstants;
import org.exoplatform.services.portletcontainer.PortletContainerException;
import org.exoplatform.services.portletcontainer.PortletContainerService;
import org.exoplatform.services.portletcontainer.PortletProcessingException;
import org.exoplatform.services.portletcontainer.pci.ActionInput;
import org.exoplatform.services.portletcontainer.pci.ActionOutput;
import org.exoplatform.services.portletcontainer.pci.EventInput;
import org.exoplatform.services.portletcontainer.pci.EventOutput;
import org.exoplatform.services.portletcontainer.pci.Output;
import org.exoplatform.services.portletcontainer.pci.PortletData;
import org.exoplatform.services.portletcontainer.pci.RenderInput;
import org.exoplatform.services.portletcontainer.pci.RenderOutput;
import org.exoplatform.services.portletcontainer.pci.ResourceInput;
import org.exoplatform.services.portletcontainer.pci.ResourceOutput;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:roman.pedchenko@exoplatform.com.ua">Roman Pedchenko</a>
 * @version $Id$
 */
public class PortalFramework {

  /**
   * Thread local instance store.
   */
  private static ThreadLocal<PortalFramework> instance                = new ThreadLocal<PortalFramework>();

  /**
   * Portal container instance.
   */
  private ExoContainer                        portalContainer         = null;

  /**
   * Portlet container instance.
   */
  private PortletContainerService             service                 = null;

  /**
   * Metadata of all currently registered portlets.
   */
  private Map<String, PortletData>            allPortletMetaData      = null;

  /**
   * Map of all portlet windows.
   */
  private HashMap<String, WindowID2>          wins                    = null;

  /**
   * Event delivery map. QName - name of Event, List - names of portlets to
   * deliver to.
   */
  private HashMap<QName, List<String>>        eventDelivery           = null;

  /**
   * Map of public parameters for portlets. String is portlet handle List is
   * SupportedPublicRenderParameter for that portlet handle
   */
  private HashMap<String, List<String>>       publicParams            = null;

  /**
   * Portal service parameters extracted from http request.
   */
  private HashMap<String, String[]>           portalParams            = null;

  /**
   * Portlet parameters extracted from http request.
   */
  private HashMap<String, String[]>           portletParams           = null;

  /**
   * Property parameters extracted from http request.
   */
  private HashMap<String, String[]>           propertyParams          = null;

  /**
   * Render parameters set by processAction().
   */
  private Map<String, String[]>               renderParams            = null;

  /**
   * Render parameters set by processEvent().
   */
  private Map<String, String[]>               eventRenderParams       = null;

  /**
   * Property parameters extracted from http request.
   */
  private HashMap<String, String[]>           publicRenderParams      = null;

  /**
   * List of changed portlets requiring to be rerendered.
   */
  private HashSet<String>                     changes                 = null;

  /**
   * List of locales supported by remote browser. Extracted from http request.
   */
  private ArrayList<Locale>                   locales                 = null;

  /**
   * Actual http session.
   */
  private HttpSession                         httpSession             = null;

  /**
   * Target portlet for current request.
   */
  private String                              target                  = null;

  /**
   * Action type requested by current request.
   */
  private int                                 action                  = -1;

  /**
   * Http content type requested by hosting portal.
   */
  private String                              cntType                 = null;

  /**
   * List of events to deliver.
   */
  private List<EventInfo>                     events                  = null;

  /**
   * Redirect path if redirection is requested.
   */
  private String                              redirect                = null;

  /**
   * Resource content if we're in serveResource().
   */
  private byte[]                              resourceContent         = null;

  /**
   * Resource content type if we're in serveResource().
   */
  private String                              resourceContentType     = null;

  /**
   * Http headers set in serveResource().
   */
  private Map<String, String>                 resourceHeaders         = null;

  /**
   * Http status set in serveResource().
   */
  private int                                 resourceStatus          = 0;

  /**
   * base URL value for URLs.
   */
  private String                              baseURL                 = null;

  /**
   * Portal pages map. Items of the map are List<String> objects that represent
   * portal pages and elements of the arrays represent individual portlets that
   * are positioned at a page, by their window IDs. Keys of the map are page
   * names or any other string keys.
   */
  private Map<String, List<String>>           pages                   = null;

  /**
   * Default portal page name.
   */
  private static final String                 DEFAULT_PAGE            = "default";

  /**
   * Current portal page selected.
   */
  private String                              currentPage             = DEFAULT_PAGE;

  /**
   * Current portal layout.
   */
  private String                              desktopLayout           = "layout2";

  private static String[]                     defaultPortalParamNames = {
      Constants.RESOURCE_ID_PARAMETER, Constants.CACHELEVEL_PARAMETER,
      Constants.COMPONENT_PARAMETER, PCConstants.REMOVE_PUBLIC_STRING, Constants.TYPE_PARAMETER,
      Constants.PORTLET_MODE_PARAMETER, Constants.WINDOW_STATE_PARAMETER,
      Constants.SECURE_PARAMETER                                     };

  private static String[]                     portalParamNames        = null;

  /**
   * Constructor.
   * 
   * @param cnt portal container
   */
  public PortalFramework(final ExoContainer cnt) {
    portalContainer = cnt;
  }

  public static void setInstance(PortalFramework framework) {
    instance.set(framework);
  }

  public static PortalFramework getInstance() {
    return instance.get();
  }

  public static void setPortalParamNames(String[] names) {
    PortalFramework.portalParamNames = names;
  }

  /**
   * Returns name of parent portal.
   * 
   * @return name of parent portal
   */
  public final String getPortalName() {
    return portalContainer.getContext().getName();
  }

  /**
   * Returns name of target portlet after parsing incoming http request.
   * 
   * @return name of target portlet
   */
  protected final String getTarget() {
    return target;
  }

  /**
   * Returns name of requested action after parsing incoming http request.
   * 
   * @return name of requested action
   */
  public final int getAction() {
    return action;
  }

  /**
   * Returns redirect path (if any) after processing of the portlet request.
   * 
   * @return redirect path or null
   */
  public final String getRedirect() {
    return redirect;
  }

  /**
   * Returns set of portlets that were changed after event processing.
   * 
   * @return set of portlet names. The returned set may be empty
   */
  public final HashSet<String> getChanges() {
    return changes;
  }

  /**
   * Returns set of all portlets currently registered in the portlet container.
   * 
   * @return set of portlet names
   */
  public final List<String> getPortletNames() {
    ArrayList<String> list = new ArrayList<String>(allPortletMetaData.keySet());
    if (list != null)
      java.util.Collections.sort(list);
    return list;
  }

  /**
   * Returns data generated by serveResource().
   * 
   * @return portlet resource binary data
   */
  public final byte[] getResourceContent() {
    return resourceContent;
  }

  /**
   * Returns serveResource() generated data content type.
   * 
   * @return portlet resource content type
   */
  public final String getResourceContentType() {
    return resourceContentType;
  }

  /**
   * Returns http headers set by serveResource().
   * 
   * @return http headers map
   */
  public final Map<String, String> getResourceHeaders() {
    return resourceHeaders;
  }

  /**
   * Returns http status set by serveResource().
   * 
   * @return status
   */
  public final int getResourceStatus() {
    return resourceStatus;
  }

  /**
   * @return portal layout
   */
  public String getDesktopLayout() {
    return desktopLayout;
  }

  /**
   * @param layout portal layout
   */
  public void setDesktopLayout(String layout) {
    desktopLayout = layout;
  }

  /**
   * Makes framework initialization for every http request.
   * 
   * @param actualHttpSession actual http session
   */
  public final void init(final HttpSession actualHttpSession) {
    service = (PortletContainerService) portalContainer.getComponentInstanceOfType(PortletContainerService.class);
    this.httpSession = actualHttpSession;
    initMaps();
  }

  /**
   * Returns collection of supported portlet modes for specified portlet.
   * 
   * @param pan portlet application name
   * @param pn portlet name infer
   * @return collection of <code>javax.portlet.PortletMode</code>
   */
  protected final Collection<PortletMode> getPortletModes(final String pan, final String pn) {
    return service.getPortletModes(pan, pn, cntType);
  }

  /**
   * Returns collection of supported window states for specified portlet.
   * 
   * @param pan portlet application name
   * @param pn portlet name
   * @return collection of <code>javax.portlet.WindowState</code>
   */
  protected final Collection<WindowState> getWindowStates(final String pan, final String pn) {
    return service.getWindowStates(pan, pn, cntType);
  }

  /**
   * Returns display name of specified portlet.
   * 
   * @param plt portlet name
   * @return display name of a portlet
   */
  protected final String getPortletDisplayName(final String plt) {
    final WindowID2 wid = getWindowID(plt);
    final PortletData portlet = allPortletMetaData.get(wid.getPortletApplicationName() + "/"
        + wid.getPortletName());
    if (portlet.getDisplayName() == null)
      return null;
    if (portlet.getDisplayName().size() == 0)
      return null;
    return (portlet.getDisplayName().get(0)).getDisplayName();
  }

  /**
   * Returns WindowID object for specified portlet.
   * 
   * @param plt portlet name
   * @return WindowID
   */
  protected final WindowID2 getWindowID(final String plt) {
    return wins.get(plt);
  }

  /**
   * Returns map of portal parameters after parsing incoming http request.
   * 
   * @return map of mixed <code>java.lang.String</code> and
   *         <code>java.lang.String[]</code>
   */
  protected final HashMap<String, String[]> getPortalParams() {
    return portalParams;
  }

  /**
   * Returns map of portlet parameters after parsing incoming http request.
   * 
   * @return map of mixed <code>java.lang.String</code> and
   *         <code>java.lang.String[]</code>
   */
  protected final HashMap<String, String[]> getPortletParams() {
    return portletParams;
  }

  /**
   * Parses incoming http request parameters, sets up requested action, target
   * portlet (if any) for the requested action, and changes portlet mode and
   * window state for the target portlet if requested.
   * 
   * @param httpRequest actual portal http request
   * @param currCntType portal content type
   */
  protected final void setParams(final HttpServletRequest httpRequest, final String currCntType) {
    this.cntType = currCntType;
    portalParams = new HashMap<String, String[]>();
    portletParams = new HashMap<String, String[]>();
    renderParams = null;
    propertyParams = null;
    if (publicRenderParams == null)
      publicRenderParams = new HashMap<String, String[]>();
    events = new ArrayList<EventInfo>();
    redirect = null;
    changes = new HashSet<String>();
    locales = new ArrayList<Locale>();
    resourceContent = null;
    resourceContentType = null;
    resourceHeaders = null;
    resourceStatus = 0;

    for (final Enumeration<Locale> e = httpRequest.getLocales(); e.hasMoreElements();)
      locales.add(e.nextElement());

    Helper.parseParams(httpRequest,
                       defaultPortalParamNames,
                       portalParamNames,
                       portalParams,
                       portletParams,
                       propertyParams);
    target = Helper.string0(portalParams.get(Constants.COMPONENT_PARAMETER));
    fixPublicRenderParams(portalParams.get(PCConstants.REMOVE_PUBLIC_STRING));
    action = Helper.getActionType(Helper.string0(portalParams.get(Constants.TYPE_PARAMETER)));

    if (portalParams.get(Constants.CACHELEVEL_PARAMETER) == null)
      portalParams.put(Constants.CACHELEVEL_PARAMETER, new String[] { ResourceURL.PAGE });

    if (target == null)
      action = PCConstants.RENDER_INT;
    if (target != null && action == PCConstants.RENDER_INT)
      Helper.separatePublicParams(portletParams, publicRenderParams, publicParams.get(target));
    if (!portalParams.isEmpty() && portalParams.containsKey(Constants.WINDOW_STATE_PARAMETER)) {
      if (portalParams.get(Constants.WINDOW_STATE_PARAMETER).equals(WindowState.MINIMIZED)) {
        final Set<String> set = wins.keySet();
        final Iterator<String> it = set.iterator();
        while (it.hasNext()) {
          final String key = it.next();
          final WindowID2 window = wins.get(key);
          window.setWindowState(WindowState.MINIMIZED);
        }
      }
    }

    if (target != null) {
      final WindowID2 win = wins.get(target);
      if (win != null) {

        // set MODE
        final PortletMode portletMode = Helper.getPortletMode(Helper.string0(portalParams.get(Constants.PORTLET_MODE_PARAMETER)),
                                                              getPortletModes(win.getPortletApplicationName(),
                                                                              win.getPortletName()));

        // set STATE
        final WindowState windowState = Helper.getWindowState(Helper.string0(portalParams.get(Constants.WINDOW_STATE_PARAMETER)),
                                                              getWindowStates(win.getPortletApplicationName(),
                                                                              win.getPortletName()));

        if (portletMode != null) {
          changes.add(target);
          win.setPortletMode(portletMode);
        }

        if (windowState != null) {
          changes.add(target);
          win.setWindowState(windowState);
        }
      } else {
        target = null;
      }
    }
  }

  /**
   * Initializes event delivery and public parameter mapsm updates or creates
   * (if necessary) portlet window objects.
   */

  protected final void initMaps() {
    allPortletMetaData = service.getAllPortletMetaData();
    eventDelivery = new HashMap<QName, List<String>>();
    publicParams = new HashMap<String, List<String>>();
    if (pages == null) {
      pages = new HashMap<String, List<String>>();
      pages.put(DEFAULT_PAGE, new ArrayList<String>());
    }

    if (wins == null) {
      wins = new HashMap<String, WindowID2>();
    } else {
      checkUnavailablePortletsAndRemove();
      buildEventDeliveryAndPublicParamsMaps(wins);
    }
  }

  /**
   * Remove unavailable portlets in container and remove them in 'wins' and
   * 'pages'.
   */
  private void checkUnavailablePortletsAndRemove() {
    for (Iterator<String> winKey = wins.keySet().iterator(); winKey.hasNext();) {
      String id = winKey.next();
      WindowID2 win = wins.get(id);
      if (!allPortletMetaData.keySet().contains(win.getPortletApplicationName() + "/"
          + win.getPortletName())) {
        winKey.remove();
        removePortletFromPages(id);
      }
    }
  }

//  /**
//   * [Re]creates WindowID map for newly appeared portlets it creates new
//   * WindowID instance for already used ones it just copies it for deleted ones
//   * it just abandons appropriate instance.
//   *
//   * @param allPortlets map of portlet metadata objects
//   */
//  protected final void createOrUpdateWins(final Map<String, PortletData> allPortlets) {
//    final HashMap<String, WindowID2> newWins = new HashMap<String, WindowID2>();
//    if (wins == null)
//      wins = new HashMap<String, WindowID2>();
//    final Set<String> keys = allPortlets.keySet();
//    final Iterator<String> i = keys.iterator();
//    while (i.hasNext()) {
//      String winKey = i.next();
//      final String[] ss = winKey.split("/");
//      winKey = winKey.hashCode() + "_" + httpSession.getId();
//
//      if (wins.get(winKey) == null) {
//        final WindowID2 windowID = new WindowID2();
//        windowID.setOwner(Constants.ANON_USER);
//        windowID.setPortletApplicationName(ss[0]);
//        windowID.setPortletName(ss[1]);
//        windowID.setPersistenceId(ss[0] + "II" + ss[1]);
//        windowID.setPortletMode(PortletMode.VIEW);
//        windowID.setWindowState(WindowState.NORMAL);
//        windowID.setUniqueID(winKey);
//        newWins.put(winKey, windowID);
//      } else
//        newWins.put(winKey, wins.get(winKey));
//    }
//    wins = newWins;
//  }
//

  /**
   * Adds a requested portlet by creating a WindowID2 instance for it.
   * 
   * @param appName portlet application name
   * @param portletName portlet name
   * @return unique Id
   * @throws PortletNotFoundException requested portlet isn't registered on the
   *           portlet container
   */
  public final String addPortlet(final String appName, final String portletName) throws PortletNotFoundException {
    String key = "";
    // Block to get a unique key element. Checking hashmap, until it returns null.
    do {
      key = appName + "_" + portletName + "_" + (new Date().toString()).hashCode() + "_"
      + httpSession.getId();
//      key = "" + (appName + "/" + portletName + "/" + (new Date().toString())).hashCode() + "_"
//          + httpSession.getId();
    } while (wins.get(key) != null);

    return addPortletWithId(appName, portletName, key);
  }

  /**
   * Adds a requested portlet by creating a WindowID2 instance for it with given
   * window ID.
   * 
   * @param appName portlet application name
   * @param portletName portlet name
   * @param windowId window ID
   * @return unique Id
   * @throws PortletNotFoundException requested portlet isn't registered on the
   *           portlet container
   */
  public final String addPortletWithId(final String appName,
                                       final String portletName,
                                       final String windowId) throws PortletNotFoundException {
    PortletData pd = allPortletMetaData.get(appName + "/" + portletName);
    if (pd == null)
      throw new PortletNotFoundException("Requested portlet isn't registered on the portlet container: "
          + appName + "/" + portletName);

    final WindowID2 windowID = new WindowID2();
    windowID.setOwner("portal#" + Constants.ANON_USER);
    windowID.setPortletApplicationName(appName);
    windowID.setPortletName(portletName);
    windowID.setPersistenceId(appName + "II" + portletName);
    windowID.setPortletMode(PortletMode.VIEW);
    windowID.setWindowState(WindowState.NORMAL);
    windowID.setUniqueID(windowId);
    wins.put(windowId, windowID);
    return windowId;
  }

  /**
   * @param id window ID
   * @return portlet window
   */
  public final WindowID2 getPortletWindowById(final String id) {
    return wins.get(id);
  }

  /**
   * Removes a requested portlet by deleting its WindowID2 instance.
   * 
   * @param key portlet unique key
   */
  public final void removePortlet(final String key) {
    if (wins.get(key) != null)
      wins.remove(key);
    removePortletFromPages(key);
  }

  /**
   * @return list of portlets currently added to the portal
   */
  public List<String> getAllPortlets() {
    ArrayList<String> al = new ArrayList<String>();
    if (wins != null)
      al.addAll(wins.keySet());
    return al;
  }

  /**
   * @param page page key
   * @return list of portlets currently added to the specified portal page
   */
  public List<String> getPagePortlets(String page) {
    return pages.get(page);
  }

  /**
   * @return list of portlets currently added to the current portal page
   */
  public List<String> getPagePortlets() {
    return pages.get(getCurrentPage());
  }

  /**
   * @param page page key
   * @param portlets list of portlets the specified portal page to contain
   */
  public void setPagePortlets(String page, List<String> portlets) {
    pages.put(page, portlets);
  }

  /**
   * @param portlets list of portlets the current portal page to contain
   */
  public void setPagePortlets(List<String> portlets) {
    pages.put(getCurrentPage(), portlets);
  }

  /**
   * @param page page key
   * @param portlet portlet key
   */
  public void addPortletToPage(String page, String portlet) {
    if (pages.get(page) == null)
      pages.put(page, new ArrayList<String>());
    pages.get(page).add(portlet);
  }

  /**
   * @param portletUniqueId portlet key
   */
  public void addPortletToPage(String portletUniqueId) {
    if (pages.get(getCurrentPage()) == null)
      pages.put(getCurrentPage(), new ArrayList<String>());
    pages.get(getCurrentPage()).add(portletUniqueId);
  }

  /**
   * @param id portlet window id
   */
  public void removePortletFromPages(String id) {
    for (Iterator<String> i = getPortalPages(); i.hasNext();)
      pages.get(i.next()).remove(id);
  }

  /**
   * @return iterator by page names
   */
  public Iterator<String> getPortalPages() {
    return pages.keySet().iterator();
  }

  /**
   * @return the currentPage
   */
  public String getCurrentPage() {
    return currentPage;
  }

  /**
   * @param currentPage the currentPage to set
   */
  public void setCurrentPage(String currentPage) {
    this.currentPage = currentPage;
  }

  /**
   * @param currentPage the currentPage to set
   */
  public void addPortalPage(String page) {
    if (getPagePortlets(page) == null)
      setPagePortlets(page, new ArrayList<String>());
    setCurrentPage(page);
  }

  /**
   * @param currentPage the currentPage to set
   */
  public void delPortalPage(String page) {
    if (pages.size() <= 1)
      return;
    setPagePortlets(page, null);
    setCurrentPage(getPortalPages().next());
  }

  /**
   * Generates maps for event and public parameters delivery.
   * 
   * @param portlets map of portlet metadata objects
   */
  protected final void buildEventDeliveryAndPublicParamsMaps(final Map<String, WindowID2> portlets) {
    final Iterator<String> i = portlets.keySet().iterator();
    while (i.hasNext()) {
      final String pname = i.next();
      final WindowID2 win = portlets.get(pname);
      final PortletData portlet = allPortletMetaData.get(win.getPortletApplicationName() + "/"
          + win.getPortletName());
      // public params
      final List<String> srp = portlet.getSupportedPublicRenderParameter();
      if (srp != null)
        publicParams.put(pname, srp);
      // event delivery
      final List<QName> spe = portlet.getSupportedProcessingEvent();
      if (spe == null)
        continue;
      final Iterator<QName> i1 = spe.iterator();
      while (i1.hasNext()) {
        final QName ename = i1.next();
        List<String> dl = eventDelivery.get(ename);
        if (dl == null) {
          dl = new ArrayList<String>();
          eventDelivery.put(ename, dl);
        }
        dl.add(pname); // here put portlet names in list for current QName
      }
    }
  }

  /**
   * For specified portlet adds public params that are supported by the portlet.
   * 
   * @param pname portlet name
   * @param params actual parameters map
   * @return map with parameters that are available to specified portlet
   */
  protected final HashMap<String, String[]> fillPublicParams(final String pname,
                                                             final HashMap<String, String[]> params) {
    final HashMap<String, String[]> result = new HashMap<String, String[]>();
    final List<String> srp = publicParams.get(pname);
    if (srp != null) {
      final Iterator<String> i = params.keySet().iterator();
      while (i.hasNext()) {
        final String rname = i.next();
        if (srp.contains(rname))
          result.put(rname, params.get(rname));
      }
    }
    return result;
  }

  /**
   * Does actual removal of public render parameters that a portlet ordered.
   * 
   * @param o target output
   */
  private void fixPublicRenderParams(final Output o) {
    if (o.getRemovedPublicRenderParameters() == null)
      return;
    for (Iterator<String> i = o.getRemovedPublicRenderParameters().iterator(); i.hasNext();) {
      String name = i.next();
      publicRenderParams.remove(name);
    }
  }

  /**
   * Does actual removal of public render parameters that a portlet ordered.
   * 
   * @param nl list of names
   */
  private void fixPublicRenderParams(final String[] nl) {
    if (nl == null)
      return;
    for (String name : nl)
      publicRenderParams.remove(name);
  }

  /**
   * Returns value of 'javax.portlet.escapeXml' portlet container runtime option
   * for specified portlet.
   * 
   * @param plt portlet name
   * @return either 'javax.portlet.escapeXml' portlet container runtime option
   *         is set
   */
  protected final boolean getPortletEscapeXml(final String plt) {
    try {
      WindowID2 wid = getWindowID(plt);
      return allPortletMetaData.get(wid.getPortletApplicationName() + "/" + wid.getPortletName())
                               .getEscapeXml();
    } catch (final Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Checks whether an event that is about to be delivered has its payload type
   * appropriate to declared.
   * 
   * @param eventInfo event to check its payload
   * @return whether payload type valid or not
   */
  protected final boolean checkEventValueType(final EventInfo eventInfo) {
    if (eventInfo.getEvent() == null)
      return false;
    if (eventInfo.getEvent().getValue() == null)
      return true;
    if (eventInfo.getTarget() == null)
      return false;
    try {
      WindowID2 win = wins.get(eventInfo.getTarget());
      if (win != null)
        return service.isEventPayloadTypeMatches(win.getPortletApplicationName(),
                                                 eventInfo.getEvent().getQName(),
                                                 eventInfo.getEvent().getValue());
      return false;
    } catch (final Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * Adds events for delivery just after checking of their type.
   * 
   * @param newEvents list of new events to add to delivery queue
   */
  protected final void addEvents(final List<Event> newEvents) {
    if (newEvents != null) {
      for (final Iterator<Event> newEventsIterator = newEvents.iterator(); newEventsIterator.hasNext();) {
        final Event event = newEventsIterator.next();
        List<String> portletsNames = eventDelivery.get(event.getQName());
        if (portletsNames == null)
          continue;
        for (final Iterator<String> portletsNamesIterator = portletsNames.iterator(); portletsNamesIterator.hasNext();) {
          final String pname = portletsNamesIterator.next();
          final EventInfo eventInfo = new EventInfo(event, pname);
          if (checkEventValueType(eventInfo))
            events.add(eventInfo); // here put event for process
        }
      }
    }
  }

  // --- input objects creators ---

  /**
   * Generates set of public parameters names for a specified portlet.
   * 
   * @param plt portlet name
   * @return set of public portlet names
   */
  private ArrayList<String> getPublicNamesSet(final String plt) {
    final List<String> pubs = publicParams.get(plt);
    final ArrayList<String> pubNames;
    if (pubs == null)
      pubNames = new ArrayList<String>();
    else
      pubNames = new ArrayList<String>(pubs);
    return pubNames;
  }

  /**
   * Creates ResourceInput instance and fills it with appropriate values.
   * 
   * @return resource input object
   */
  protected final ResourceInput createResourceInput() {
    final ResourceInput resourceInput = new ResourceInput();
    final WindowID2 win = wins.get(target);
    resourceInput.setLocales(locales);
    resourceInput.setEscapeXml(getPortletEscapeXml(target));
    resourceInput.setInternalWindowID(win);
    resourceInput.setBaseURL(appendParamsToUrl(baseURL));
    resourceInput.setUserAttributes(new HashMap<String, String>());
    resourceInput.setMarkup(cntType);
    resourceInput.setPublicParamNames(getPublicNamesSet(target));
    resourceInput.setRenderParameters(new HashMap<String, String[]>());
    Helper.appendParams(resourceInput.getRenderParameters(), portletParams);
    Helper.appendParams(resourceInput.getRenderParameters(), publicRenderParams);
    resourceInput.setPropertyParams(propertyParams);
    resourceInput.setPortletMode(win.getPortletMode());
    resourceInput.setWindowState(win.getWindowState());
    resourceInput.setResourceID(Helper.string0(portalParams.get(Constants.RESOURCE_ID_PARAMETER)));
    resourceInput.setCacheability(Helper.string0(portalParams.get(Constants.CACHELEVEL_PARAMETER)));
    return resourceInput;
  }

  /**
   * Creates ActionInput instance and fills it with appropriate values.
   * 
   * @return action input object
   */
  protected final ActionInput createActionInput() {
    final ActionInput actionInput = new ActionInput();
    final WindowID2 win = wins.get(target);
    actionInput.setLocales(locales);
    actionInput.setEscapeXml(getPortletEscapeXml(target));
    actionInput.setInternalWindowID(win);
    actionInput.setBaseURL(baseURL + target);
    actionInput.setUserAttributes(new HashMap<String, String>());
    actionInput.setMarkup(cntType);
    actionInput.setPublicParamNames(getPublicNamesSet(target));
    actionInput.setRenderParameters(new HashMap<String, String[]>());
    Helper.appendParams(actionInput.getRenderParameters(), portletParams);
    Helper.appendParams(actionInput.getRenderParameters(), publicRenderParams);
    portletParams = new HashMap<String, String[]>();
    actionInput.setPortletMode(win.getPortletMode());
    actionInput.setWindowState(win.getWindowState());
    actionInput.setStateChangeAuthorized(true);
    return actionInput;
  }

  /**
   * Creates EventInput instance and fills it with appropriate values.
   * 
   * @param event event to deliver
   * @return event input object
   */
  protected final EventInput createEventInput(final EventInfo event) {
    final String eventTarget = event.getTarget();
    changes.add(eventTarget);
    final EventInput eventInput = new EventInput();
    final WindowID2 win = wins.get(eventTarget);
    eventInput.setLocales(locales);
    eventInput.setEscapeXml(getPortletEscapeXml(eventTarget));
    eventInput.setInternalWindowID(win);
    eventInput.setBaseURL(baseURL + eventTarget);
    eventInput.setUserAttributes(new HashMap<String, String>());
    eventInput.setMarkup(cntType);
    eventInput.setPublicParamNames(getPublicNamesSet(eventTarget));
    eventInput.setRenderParameters(new HashMap<String, String[]>());
    if (target != null && target.equals(eventTarget)) {
      Helper.appendParams(eventInput.getRenderParameters(), renderParams);
      Helper.appendParams(eventInput.getRenderParameters(), publicRenderParams);
    } else {
      Helper.appendParams(eventInput.getRenderParameters(), win.getRenderParams());
      Helper.appendParams(eventInput.getRenderParameters(), fillPublicParams(eventTarget,
                                                                             publicRenderParams));
    }
    eventInput.setPortletMode(win.getPortletMode());
    eventInput.setWindowState(win.getWindowState());
    eventInput.setEvent(event.getEvent());
    return eventInput;
  }

  /**
   * Creates RenderInput instance and fills it with appropriate values.
   * 
   * @param plt name of portlet to render
   * @return render input object
   */
  protected final RenderInput createRenderInput(final String plt) {
    final RenderInput renderInput = new RenderInput();
    final WindowID2 win = wins.get(plt);
    renderInput.setLocales(locales);
    renderInput.setEscapeXml(getPortletEscapeXml(plt));
    renderInput.setInternalWindowID(win);
    renderInput.setBaseURL(appendParamsToUrl(baseURL + plt));
    renderInput.setUserAttributes(new HashMap<String, String>());
    renderInput.setMarkup(cntType);
    renderInput.setPublicParamNames(getPublicNamesSet(plt));
    renderInput.setRenderParameters(new HashMap<String, String[]>());
    if (target != null && target.equals(plt)) {
      Helper.appendParams(renderInput.getRenderParameters(), publicRenderParams);
      Helper.appendParams(renderInput.getRenderParameters(), portletParams);
      Helper.appendParams(renderInput.getRenderParameters(), renderParams);
      win.setRenderParams(renderInput.getRenderParameters());
    } else {
      Helper.appendParams(renderInput.getRenderParameters(), fillPublicParams(plt,
                                                                              publicRenderParams));
      Helper.appendParams(renderInput.getRenderParameters(), win.getRenderParams());
    }
    renderInput.setPortletMode(win.getPortletMode());
    renderInput.setWindowState(win.getWindowState());
    //renderInput.setCacheability(Helper.string0(portalParams.get(Constants.CACHELEVEL_PARAMETER)));
    return renderInput;
  }

  // --- portlet method callers ---

  /**
   * Calls serveResource() for target portlet and parses its output.
   * 
   * @param httpRequest http servlet request
   * @param httpResponse http servlet response
   * @param resourceInput resource input object
   * @return resource output object
   * @throws PortletContainerException exception
   */
  protected final ResourceOutput serveResource(final HttpServletRequest httpRequest,
                                               final HttpServletResponse httpResponse,
                                               final ResourceInput resourceInput) throws PortletContainerException {
    final ResourceOutput o = service.serveResource(httpRequest, httpResponse, resourceInput);
    return o;
  }

  /**
   * Calls processAction() for target portlet and parses its output.
   * 
   * @param httpRequest http servlet request
   * @param httpResponse http servlet response
   * @param actionInput action input object
   * @return action output object
   * @throws PortletContainerException
   */
  protected final ActionOutput processAction(final HttpServletRequest httpRequest,
                                             final HttpServletResponse httpResponse,
                                             final ActionInput actionInput) throws PortletContainerException {
    final ActionOutput o = service.processAction(httpRequest, httpResponse, actionInput);
    if (o.hasError()) {
      renderParams = new HashMap<String, String[]>();
      return o;
    }
    fixPublicRenderParams(o);
    renderParams = o.getRenderParameters();
    if (renderParams == null)
      renderParams = new HashMap<String, String[]>();
    addEvents(o.getEvents());
    redirect = (String) o.getProperties().get(Output.SEND_REDIRECT);
    final WindowID2 win = wins.get(target);
    if (o.getNextMode() != null)
      win.setPortletMode(o.getNextMode());
    if (o.getNextState() != null)
      win.setWindowState(o.getNextState());
    return o;
  }

  /**
   * Calls processEvent() for target portlet and parses its output.
   * 
   * @param httpRequest http servlet request
   * @param httpResponse http servlet response
   * @param eventInput event input object
   * @param event event to deliver
   * @return event output object
   * @throws PortletContainerException
   */
  protected final EventOutput processEvent(final HttpServletRequest httpRequest,
                                           final HttpServletResponse httpResponse,
                                           final EventInput eventInput,
                                           final EventInfo event) throws PortletContainerException {
    final EventOutput o = service.processEvent(httpRequest, httpResponse, eventInput);
    eventRenderParams = o.getRenderParameters();
    if (o.hasError()) {
      eventRenderParams = new HashMap<String, String[]>();
      return o;
    }
    if (eventRenderParams == null)
      eventRenderParams = new HashMap<String, String[]>();
    // move parameters from eventRenderParams to publicRenderParams, which are public according publicParams.
    Helper.separatePublicParams(eventRenderParams,
                                publicRenderParams,
                                publicParams.get(event.getTarget()));
    // remove param in publicRenderParams according output
    fixPublicRenderParams(o);
    addEvents(o.getEvents());
    final WindowID2 win = wins.get(event.getTarget());
    if (o.getNextMode() != null)
      win.setPortletMode(o.getNextMode());
    if (o.getNextState() != null)
      win.setWindowState(o.getNextState());
    return o;
  }

  /**
   * Calls render() for target portlet and parses its output.
   * 
   * @param httpRequest http servlet request
   * @param httpResponse http servlet response
   * @param renderInput render input object
   * @return render output object
   * @throws PortletContainerException
   */
  protected final RenderOutput render(final HttpServletRequest httpRequest,
                                      final HttpServletResponse httpResponse,
                                      final RenderInput renderInput) throws PortletContainerException {
    final RenderOutput o = service.render(httpRequest, httpResponse, renderInput);
    return o;
  }

  // --- ---

  /**
   * Subsequently delivers generated events.
   * 
   * @param ctx servlet context
   * @param httpRequest http servlet request
   * @param httpResponse http servlet response
   */
  protected final void dispatchEvents(final ServletContext ctx,
                                      final HttpServletRequest httpRequest,
                                      final HttpServletResponse httpResponse) {
    final boolean hasToDelRenderParams = events.size() > 0;
    while (events.size() > 0) {
      final EventInfo event = events.get(0);
      events.remove(0);
      final EventInput eventInput = createEventInput(event);
      try {
        processEvent(httpRequest, httpResponse, eventInput, event);
      } catch (final Exception e) {
        e.printStackTrace();
      }
    }
    if (hasToDelRenderParams)
      renderParams = eventRenderParams;
  }

  // --- ---

  /**
   * The same as processRequest() but for the currently selected page.
   * 
   * @param ctx servlet context
   * @param httpRequest http servlet request
   * @param httpResponse http servlet response
   * @param markupType portal mark up type
   * @return array of <code>PortletInfo</code> objects
   */
  public final ArrayList<PortletInfo> processRequestForCurrentPage(final ServletContext ctx,
                                                                   final HttpServletRequest httpRequest,
                                                                   final HttpServletResponse httpResponse,
                                                                   final String markupType) {
    return processRequestForPage(ctx, httpRequest, httpResponse, markupType, getCurrentPage());
  }

  /**
   * The same as processRequest() but for a specified page.
   * 
   * @param ctx servlet context
   * @param httpRequest http servlet request
   * @param httpResponse http servlet response
   * @param markupType portal mark up type
   * @param page page key
   * @return array of <code>PortletInfo</code> objects
   */
  public final ArrayList<PortletInfo> processRequestForPage(final ServletContext ctx,
                                                            final HttpServletRequest httpRequest,
                                                            final HttpServletResponse httpResponse,
                                                            final String markupType,
                                                            final String page) {
    return processRequest(ctx, httpRequest, httpResponse, markupType, getPagePortlets(page));
  }

  /**
   * Includes the full cycle of portlet container user http request processing,
   * collects data returned by portlets and returns it to a caller.
   * 
   * @param ctx servlet context
   * @param httpRequest http servlet request
   * @param httpResponse http servlet response
   * @param markupType portal mark up type
   * @param list list of requested portlet names
   * @return array of <code>PortletInfo</code> objects
   */
  public final ArrayList<PortletInfo> processRequest(final ServletContext ctx,
                                                     final HttpServletRequest httpRequest,
                                                     final HttpServletResponse httpResponse,
                                                     final String markupType,
                                                     final List<String> list) {

    preRenderRequest(ctx, httpRequest, httpResponse, markupType);

    if (getAction() == PCConstants.RESOURCE_INT)
      return null;

    if (getRedirect() != null)
      return null;

    // collecting portlets to render

//    final Iterator<String> totalPlts = getPortletNames().iterator();

    final ArrayList<PortletInfo> portletInfos = new ArrayList<PortletInfo>();
    for (Iterator<String> reqPlts = wins.keySet().iterator(); reqPlts.hasNext();) {
      final String portlet = reqPlts.next();
      if (list == null || !list.contains(portlet))
        continue;
      portletInfos.add(renderPortlet(portlet));
    }
    return portletInfos;
  }

  public String getPortalPortletModeUrl(String id, String mode) {
    String url = baseURL + id + "&" + Constants.SECURE_PARAMETER + "=true&"
        + Constants.PORTLET_MODE_PARAMETER + "=" + mode;
    return appendParamsToUrl(url);
  }

  public String getPortalWindowStateUrl(String id, String state) {
    String url = baseURL + id + "&" + Constants.SECURE_PARAMETER + "=true&"
        + Constants.WINDOW_STATE_PARAMETER + "=" + state;
    return appendParamsToUrl(url);
  }

  /**
   * @param url
   * @return
   */
  private String appendParamsToUrl(String url) {
    if (portalParamNames != null) {
      List<String> portalParamNamesList = Arrays.asList(portalParamNames);
      for (Iterator<String> i = portalParams.keySet().iterator(); i.hasNext();) {
        String n = i.next();
        if (portalParamNamesList.contains(n)) {
          String[] pvs = portalParams.get(n);
          for (String pv : pvs)
            url += "&" + n + "=" + pv;
        }
      }
    }
    return url;
  }

  
  
  private HttpServletRequest     presavedHttpRequest;

  private HttpServletResponse    presavedHttpResponse;

  private ArrayList<PortletInfo> portletInfos;

  /**
   * @param ctx
   * @param httpRequest
   * @param httpResponse
   * @param markupType
   */
  public void preRenderRequest(final ServletContext ctx,
                               final HttpServletRequest httpRequest,
                               final HttpServletResponse httpResponse,
                               final String markupType) {
    baseURL = httpRequest.getRequestURI() + "?" + Constants.COMPONENT_PARAMETER + "=";

    setParams(httpRequest, markupType);

    // actions processing

    resourceContent = null;
    resourceContentType = markupType;

    if (getAction() == PCConstants.RESOURCE_INT) {

      // processing resource

      final ResourceInput resourceInput = createResourceInput();
      try {
        final ResourceOutput o = serveResource(httpRequest, httpResponse, resourceInput);
        try {
          resourceContent = o.getBinContent();
          if (o.getContentType() != null)
            resourceContentType = o.getContentType();
          Map<String, String> headers = o.getHeaderProperties();
          if (headers.size() > 0)
            resourceHeaders = headers;
          resourceStatus = o.getStatus();
          httpResponse.setCharacterEncoding(o.getCharacterEncoding());
        } catch (final Exception oe) {
        }
      } catch (final Exception e1) {
        System.out.println(" !!!!!!!!!!!! error invoking serveResource in " + getTarget() + ": "
            + e1);
        e1.printStackTrace();
        System.out.println(" !!!!!!!!!!!! trying to continue...");
      }
      // if resource is requested we must render nothing
      return;
    }

    if (getAction() == PCConstants.ACTION_INT) {

      // processing action

      final ActionInput actionInput = createActionInput();
      try {
        processAction(httpRequest, httpResponse, actionInput);
      } catch (final Exception e2) {
        System.out.println(" !!!!!!!!!!!! error processing action of portlet " + getTarget() + ": "
            + e2);
        e2.printStackTrace();
        System.out.println(" !!!!!!!!!!!! trying to continue...");
      }

      // if redirect is requested we must render nothing
      if (getRedirect() != null)
        return;
    }

    // processing events

    dispatchEvents(ctx, httpRequest, httpResponse);

    // recollecting portlets
    initMaps();

    // presave current request/response objects
    presavedHttpRequest = httpRequest;
    presavedHttpResponse = httpResponse;
  }

  /**
   * @param httpRequest
   * @param httpResponse
   * @param portlet
   * @return
   */
  public PortletInfo renderPortlet(String portlet) {
    PortletInfo portletinfo = Helper.createPortletInfo(this, portlet);
    portletinfo.setToRender(true);
    RenderInput renderInput = createRenderInput(portlet);
    try {
      RenderOutput o = render(presavedHttpRequest, presavedHttpResponse, renderInput);

      if (o.getNextPossiblePortletModes() != null) {
        // TODO Do not delete it!
        ArrayList<String> modes = new ArrayList<String>();
        Collection<PortletMode> portletModes = o.getNextPossiblePortletModes();
        for (PortletMode portletMode : portletModes) {
          modes.add(portletMode.toString());
        }
        portletinfo.setModes(modes);
      }

      portletinfo.setSessionMap(o.getSessionMap());
      try {
        char[] cnt = o.getContent();
        if (cnt != null)
          portletinfo.setOut(new String(cnt));
        else
          portletinfo.setOut("");
      } catch (Throwable oe) {
        portletinfo.setOut("");
        System.out.println(" !!!!!!!!!!!! error getContent portlet " + portlet + ": " + oe);
      }
    } catch (PortletProcessingException ppe) {
      portletinfo.setOut(generatePortletErrorMarkup(portletinfo, ppe));
    } catch (Throwable e1) {
      System.out.println(" !!!!!!!!!!!! error rendering portlet " + portlet + ": " + e1);
      e1.printStackTrace();
      System.out.println(" !!!!!!!!!!!! trying to continue...");
    }
    return portletinfo;
  }

  private String generatePortletErrorMarkup(PortletInfo portletinfo, PortletProcessingException ppe) {
    String markup = "<h3>Portlet <i>" + portletinfo.getTitle() + "</i> has failed: \"" + ppe.getMessage() + "\"</h3>";
    return markup;
  }

  public Map<String, Object> getRenderedPortletInfos() {
    Map<String, Object> sessionInfo = new HashMap<String, Object>();
    ArrayList<PortletInfo> pinfos = this.getPortletInfos();
    if (pinfos != null)
      for (PortletInfo portletInfo : pinfos) {
        sessionInfo.put(portletInfo.getName().split("/")[0], portletInfo.getSessionMap());
      }
    return sessionInfo;
  }

  /**
   * Method createOrUpdatePortletWindows came from PortletFilter.
   */
  public void createOrUpdatePortletWindows() {
    for (String pn : getPortletNames()) {
      String[] ss = pn.split("/");
      // search new portlet on a page
      if (!foundInPagePortlets(ss[0], ss[1])) {
        // adds new portlet to the page
        try {
          addPortletToPage(addPortlet(ss[0], ss[1]));
        } catch (PortletNotFoundException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * Method foundInPortlets came from PortletFilter.
   */
  private boolean foundInPagePortlets(String app, String plt) {
    for (Iterator<String> i = getPagePortlets().iterator(); i.hasNext();) {
      WindowID2 win = getPortletWindowById(i.next());
      if (win.getPortletApplicationName().equals(app) && win.getPortletName().equals(plt)) {
        return true;
      }
    }
    return false;
  }

  public void processRender(List<String> portlets2render) {
    this.portletInfos = processRenderList(portlets2render);
  }

  private ArrayList<PortletInfo> processRenderList(List<String> portlets2render) {
    ArrayList<PortletInfo> portletInfos = new ArrayList<PortletInfo>();
    for (Iterator<String> pagePortletsIterator = getPagePortlets().iterator(); pagePortletsIterator.hasNext();) {
      String plt = pagePortletsIterator.next();
      if (portlets2render != null && portlets2render.contains(plt)) {
        PortletInfo pinfo = renderPortlet(plt);
        portletInfos.add(pinfo);
      } else {
        PortletInfo pinfo = Helper.createPortletInfo(this, plt);
        portletInfos.add(pinfo);
      }
    }
    return portletInfos;
  }

  public ArrayList<PortletInfo> getPortletInfos() {
    return portletInfos;
  }

  /**
   * @return the presavedHttpRequest
   */
  public HttpServletRequest getPresavedHttpRequest() {
    return presavedHttpRequest;
  }

  /**
   * @return the presavedHttpResponse
   */
  public HttpServletResponse getPresavedHttpResponse() {
    return presavedHttpResponse;
  }

}
