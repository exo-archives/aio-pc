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
import java.util.Collection;
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

import org.apache.commons.lang.StringUtils;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.services.portletcontainer.PortletContainerException;
import org.exoplatform.services.portletcontainer.PortletContainerService;
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
import org.exoplatform.services.portletcontainer.plugins.pc.PCConstants;

/**
 * Created by The eXo Platform SAS .
 *
 * @author <a href="mailto:roman.pedchenko@exoplatform.com.ua">Roman Pedchenko</a>
 * @version $Id$
 */
public class PortalFramework {

  /**
   * Portal container instance.
   */
  private ExoContainer                  portalContainer     = null;

  /**
   * Portlet container instance.
   */
  private PortletContainerService       service             = null;

  /**
   * Metadata of all currently registered portlets.
   */
  private Map<String, PortletData>      allPortletMetaData  = null;

  /**
   * Map of all portlet windows.
   */
  private HashMap<String, WindowID2>    wins                = null;

  /**
   * Event delivery map. QName - name of Event, List - names of portlets to
   * deliver to.
   */
  private HashMap<QName, List<String>>  eventDelivery       = null;

  /**
   * Map of public parameters for portlets.
   * 
   * String is portlet handle
   * List<String> is SupportedPublicRenderParameter for that portlet handle
   */
  private HashMap<String, List<String>> publicParams        = null;

  /**
   * Portal service parameters extracted from http request.
   */
  private HashMap<String, String[]>     portalParams        = null;

  /**
   * Portlet parameters extracted from http request.
   */
  private HashMap<String, String[]>     portletParams       = null;

  /**
   * Property parameters extracted from http request.
   */
  private HashMap<String, String[]>     propertyParams      = null;

  /**
   * Render parameters set by processAction().
   */
  private Map<String, String[]>         renderParams        = null;

  /**
   * Render parameters set by processEvent().
   */
  private Map<String, String[]>         eventRenderParams   = null;

  /**
   * List of changed portlets requiring to be rerendered.
   */
  private HashSet<String>               changes             = null;

  /**
   * List of locales supported by remote browser. Extracted from http request.
   */
  private ArrayList<Locale>             locales             = null;

  /**
   * Actual http session.
   */
  private HttpSession                   httpSession         = null;

  /**
   * Target portlet for current request.
   */
  private String                        target              = null;

  /**
   * Action type requested by current request.
   */
  private int                           action              = -1;

  /**
   * Http content type requested by hosting portal.
   */
  private String                        cntType             = null;

  /**
   * List of events to deliver.
   */
  private List<EventInfo>               events              = null;

  /**
   * Redirect path if redirection is requested.
   */
  private String                        redirect            = null;

  /**
   * Resource content if we're in serveResource().
   */
  private byte[]                        resourceContent     = null;

  /**
   * Resource content type if we're in serveResource().
   */
  private String                        resourceContentType = null;

  /**
   * Http headers set in serveResource().
   */
  private Map<String, String>           resourceHeaders     = null;

  private String                        baseURL             = null;

  /**
   * Constructor.
   *
   * @param cnt portal container
   */
  public PortalFramework(final ExoContainer cnt) {
    portalContainer = cnt;
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
  public final ArrayList<String> getPortletNames() {
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
  protected final Collection<PortletMode> getPortletModes(final String pan,
                                                          final String pn) {
    return service.getPortletModes(pan, pn, cntType);
  }

  /**
   * Returns collection of supported window states for specified portlet.
   *
   * @param pan portlet application name
   * @param pn portlet name
   * @return collection of <code>javax.portlet.WindowState</code>
   */
  protected final Collection<WindowState> getWindowStates(final String pan,
                                                          final String pn) {
    return service.getWindowStates(pan, pn, cntType);
  }

  /**
   * Returns display name of specified portlet.
   *
   * @param plt portlet name
   * @return display name of a portlet
   */
  protected final String getPortletDisplayName(final String plt) {
    final PortletData portlet = allPortletMetaData.get(plt);
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
  protected final void setParams(final HttpServletRequest httpRequest,
                                 final String currCntType) {
    this.cntType = currCntType;
    portalParams = new HashMap<String, String[]>();
    portletParams = new HashMap<String, String[]>();
    renderParams = null;
    propertyParams = null;
    events = new ArrayList<EventInfo>();
    redirect = null;
    changes = new HashSet<String>();
    locales = new ArrayList<Locale>();
    resourceContent = null;
    resourceContentType = null;
    resourceHeaders = null;

    for (final Enumeration<Locale> e = httpRequest.getLocales(); e.hasMoreElements();)
      locales.add(e.nextElement());

    Helper.parseParams(httpRequest, portalParams, portletParams, propertyParams);
    target = Helper.string0(portalParams.get(org.exoplatform.Constants.COMPONENT_PARAMETER));
    action = Helper.getActionType(Helper.string0(portalParams.get(org.exoplatform.Constants.TYPE_PARAMETER)));

    if (portalParams.get(org.exoplatform.Constants.CACHELEVEL_PARAMETER) == null)
      portalParams.put(org.exoplatform.Constants.CACHELEVEL_PARAMETER, new String[] { ResourceURL.PAGE });

    // confirm that target is valid to pattern "APPLICATION_NAME/PORTLET_NAME"
    if (target != null)
      if (target.split("/").length >= 3)
        target = target.split("/")[0] + "/" + target.split("/")[1];

    if (target == null)
      action = PCConstants.renderInt;

    if (!portalParams.isEmpty() && portalParams.containsKey(org.exoplatform.Constants.WINDOW_STATE_PARAMETER)) {
      if (portalParams.get(org.exoplatform.Constants.WINDOW_STATE_PARAMETER).equals(WindowState.MINIMIZED)) {
        final Set<String> set = wins.keySet();
        final Iterator<String> it = set.iterator();
        while (it.hasNext()) {
          final String key = it.next();
          final WindowID2 window = wins.get(key);
          window.setWindowState(WindowState.MINIMIZED);
        }
      }
    }

    final String[] pn = StringUtils.split(target, "/");

    if (target != null) {

      final WindowID2 win = wins.get(target);
      // set MODE
      final PortletMode portletMode = Helper.getPortletMode(Helper.string0(portalParams.get(org.exoplatform.Constants.PORTLET_MODE_PARAMETER)),
                                                            getPortletModes(pn[0], pn[1]));
      // set STATE
      final WindowState windowState = Helper.getWindowState(Helper.string0(portalParams.get(org.exoplatform.Constants.WINDOW_STATE_PARAMETER)),
                                                            getWindowStates(pn[0], pn[1]));

      if (portletMode != null) {
        changes.add(target);
        win.setPortletMode(portletMode);
      }

      if (windowState != null) {
        changes.add(target);
        win.setWindowState(windowState);
      }
    }
  }

  /**
   * Initializes event delivery and public parameter mapsm updates or creates
   * (if necessary) portlet window objects.
   */
  protected final void initMaps() {
    allPortletMetaData = service.getAllPortletMetaData();
    buildEventDeliveryAndPublicParamsMaps(allPortletMetaData);
    createOrUpdateWins(allPortletMetaData);
  }

  /**
   * [Re]creates WindowID map for newly appeared portlets it creates new
   * WindowID instance for already used ones it just copies it for deleted ones
   * it just abandons appropriate instances.
   *
   * @param allPortlets map of portlet metadata objects
   */
  protected final void createOrUpdateWins(final Map<String, PortletData> allPortlets) {
    final HashMap<String, WindowID2> newWins = new HashMap<String, WindowID2>();
    if (wins == null)
      wins = new HashMap<String, WindowID2>();
    final Set<String> keys = allPortlets.keySet();
    final Iterator<String> i = keys.iterator();
    while (i.hasNext()) {
      final String key = i.next();
      final String portletName = key;// was: = key.replace('.', '/');
      final String[] ss = StringUtils.split(portletName, "/");

      if (wins.get(portletName) == null) {
        final WindowID2 windowID = new WindowID2();
        windowID.setOwner(org.exoplatform.Constants.ANON_USER);
        windowID.setPortletApplicationName(ss[0]);
        windowID.setPortletName(ss[1]);
        windowID.setPersistenceId(ss[0] + "II" + ss[1]);
        windowID.setPortletMode(PortletMode.VIEW);
        windowID.setWindowState(WindowState.NORMAL);
        windowID.setUniqueID(portletName.hashCode() + "_" + httpSession.getId());
        newWins.put(portletName, windowID);
      } else
        newWins.put(portletName, wins.get(portletName));
    }
    wins = newWins;
  }

  /**
   * Generates maps for event and public parameters delivery.
   *
   * @param allPortlets map of portlet metadata objects
   */
  protected final void buildEventDeliveryAndPublicParamsMaps(final Map<String, PortletData> allPortlets) {
    eventDelivery = new HashMap<QName, List<String>>();
    publicParams = new HashMap<String, List<String>>();
    final Iterator<String> i = allPortlets.keySet().iterator();
    while (i.hasNext()) {
      final String pname = i.next();
      final PortletData portlet = allPortlets.get(pname);
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
        dl.add(pname); // here put portlet name for event list
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
   * Returns value of 'javax.portlet.escapeXml' portlet container runtime option
   * for specified portlet.
   *
   * @param portlet portlet name
   * @return either 'javax.portlet.escapeXml' portlet container runtime option
   *         is set
   */
  protected final boolean getPortletEscapeXml(final String portlet) {
    try {
      return allPortletMetaData.get(portlet).getEscapeXml();
    } catch (final Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Checks whether an event that is about to be delivered has its payload type
   * appropriate to declared.
   *
   * @param event event to check its payload
   * @return whether payload type valid or not
   */
  protected final boolean checkEventValueType(final EventInfo event) {
    if (event.getEvent() == null)
      return false;
    try {
      final String[] hs = StringUtils.split(event.getTarget(), "/");
      return service.isEventPayloadTypeMatches(hs[0], event.getEvent().getQName(), event.getEvent().getValue());
    } catch (final Exception e) {
      e.printStackTrace();
      return false;
    }
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
        // TODO: PLT.15.2.4.1 Declaration in the deployment descriptor
        // Event parameter names
        // the event-definition element are allowed to end with a �.� character
        // to indicate the
        // 30 portlet is willing to process or publish any event whose name
        // starts with the characters
        // before the �.� character.
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
   * Creates ResourceInput instance and fills it with appropriate values.
   *
   * @param ctx servlet context
   * @return resource input object
   */
  protected final ResourceInput createResourceInput() {
    final ResourceInput resourceInput = new ResourceInput();
    final WindowID2 win = wins.get(target);
    resourceInput.setLocales(locales);
    resourceInput.setEscapeXml(getPortletEscapeXml(target));
    resourceInput.setInternalWindowID(win);
    resourceInput.setBaseURL(baseURL + target);
    resourceInput.setUserAttributes(new HashMap<String, String>());
    resourceInput.setMarkup(cntType);
    resourceInput.setRenderParameters(portletParams);
    if (win.getRenderParams() != null)
      Helper.appendParams(resourceInput.getRenderParameters(), win.getRenderParams());
    resourceInput.setPortletMode(win.getPortletMode());
    resourceInput.setWindowState(win.getWindowState());
    resourceInput.setResourceID(Helper.string0(portalParams.get(org.exoplatform.Constants.RESOURCE_ID_PARAMETER)));
    resourceInput.setCacheability(Helper.string0(portalParams.get(org.exoplatform.Constants.CACHELEVEL_PARAMETER)));
    return resourceInput;
  }

  /**
   * Creates ActionInput instance and fills it with appropriate values.
   *
   * @param ctx servlet context
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
    actionInput.setRenderParameters(portletParams);
    portletParams = new HashMap<String, String[]>();
    actionInput.setPortletMode(win.getPortletMode());
    actionInput.setWindowState(win.getWindowState());
    actionInput.setStateChangeAuthorized(true);
    return actionInput;
  }

  /**
   * Creates EventInput instance and fills it with appropriate values.
   *
   * @param ctx servlet context
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
    eventInput.setRenderParameters(new HashMap<String, String[]>());
    //    Helper.appendParams(eventInput.getRenderParameters(), portletParams);
    if (target != null && target.equals(eventTarget) && renderParams != null)
      Helper.appendParams(eventInput.getRenderParameters(), renderParams);
    eventInput.setPortletMode(win.getPortletMode());
    eventInput.setWindowState(win.getWindowState());
    eventInput.setEvent(event.getEvent());
    return eventInput;
  }

  /**
   * Creates RenderInput instance and fills it with appropriate values.
   *
   * @param ctx servlet context
   * @param plt name of portlet to render
   * @return render input object
   */
  protected final RenderInput createRenderInput(final String plt) {
    final RenderInput renderInput = new RenderInput();
    final WindowID2 win = wins.get(plt);
    renderInput.setLocales(locales);
    renderInput.setEscapeXml(getPortletEscapeXml(plt));
    renderInput.setInternalWindowID(win);
    renderInput.setBaseURL(baseURL + plt);
    renderInput.setUserAttributes(new HashMap<String, String>());
    renderInput.setMarkup(cntType);
    final List<String> pubs = publicParams.get(plt);
    final HashSet<String> pubNames;
    if (pubs == null)
      pubNames = new HashSet<String>();
    else
      pubNames = new HashSet<String>(pubs);
    renderInput.setPublicParamNames(pubNames);
    renderInput.setRenderParameters(new HashMap<String, String[]>());
    if (target != null && target.equals(plt)) {
      Helper.appendParams(renderInput.getRenderParameters(), portletParams);
      if (renderParams != null)
        Helper.appendParams(renderInput.getRenderParameters(), renderParams);
      win.setRenderParams(renderInput.getRenderParameters());
    } else {
      Helper.appendParams(renderInput.getRenderParameters(), fillPublicParams(plt, portletParams));
      if (win.getRenderParams() != null)
        Helper.appendParams(renderInput.getRenderParameters(), win.getRenderParams());
    }
    renderInput.setPortletMode(win.getPortletMode());
    renderInput.setWindowState(win.getWindowState());
    //renderInput.setCacheability(Helper.string0(portalParams.get(org.exoplatform.Constants.CACHELEVEL_PARAMETER)));
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
   * @throws PortletContainerException
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
   * Includes the full cycle of portlet container user http request processing,
   * collects data returned by portlets and returns it to a caller.
   *
   * @param ctx servlet context
   * @param httpRequest http servlet request
   * @param httpResponse http servlet response
   * @param markupType portal mark up type
   * @param requestedPortlets list of requested portlet names
   * @return array of <code>PortletInfo</code> objects
   */
  public final ArrayList<PortletInfo> processRequest(final ServletContext ctx,
                                                     final HttpServletRequest httpRequest,
                                                     final HttpServletResponse httpResponse,
                                                     final String markupType,
                                                     final ArrayList<String> requestedPortlets) {

    baseURL = httpRequest.getRequestURI() + "?" + org.exoplatform.Constants.COMPONENT_PARAMETER + "=";

    setParams(httpRequest, markupType);

    // actions processing

    resourceContent = null;
    resourceContentType = markupType;

    if (getAction() == PCConstants.resourceInt) {

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
          httpResponse.setCharacterEncoding(o.getCharacterEncoding());
        } catch (final Exception oe) {
        }
      } catch (final Exception e1) {
        System.out.println(" !!!!!!!!!!!! error invoking serveResource in " + getTarget() + ": " + e1);
        e1.printStackTrace();
        System.out.println(" !!!!!!!!!!!! trying to continue...");
      }
      // if resource is requested we must render nothing
      return null;
    }

    if (getAction() == PCConstants.actionInt) {

      // processing action

      final ActionInput actionInput = createActionInput();
      try {
        processAction(httpRequest, httpResponse, actionInput);
      } catch (final Exception e2) {
        System.out.println(" !!!!!!!!!!!! error processing action of portlet " + getTarget() + ": " + e2);
        e2.printStackTrace();
        System.out.println(" !!!!!!!!!!!! trying to continue...");
      }

      // if redirect is requested we must render nothing
      if (getRedirect() != null)
        return null;
    }

    // processing events

    dispatchEvents(ctx, httpRequest, httpResponse);

    // processing render

    // recollecting portlets
    initMaps();

    // collecting portlets to render

    final Iterator<String> totalPlts = getPortletNames().iterator();

    final ArrayList<PortletInfo> portletInfos = new ArrayList<PortletInfo>();
    while (totalPlts.hasNext()) {
      final String portlet = totalPlts.next();
      final PortletInfo portletinfo = Helper.createPortletInfo(this, portlet);
      portletInfos.add(portletinfo);
      if (requestedPortlets == null || !requestedPortlets.contains(portlet))
        continue;
      final RenderInput renderInput = createRenderInput(portlet);
      try {
        final RenderOutput o = render(httpRequest, httpResponse, renderInput);

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
          final char[] cnt = o.getContent();
          if (cnt != null)
            portletinfo.setOut(new String(cnt));
          else
            portletinfo.setOut("");
        } catch (final Exception oe) {
          portletinfo.setOut("");
          System.out.println(" !!!!!!!!!!!! error getContent portlet " + portlet + ": " + oe);
        }
      } catch (final Exception e1) {
        System.out.println(" !!!!!!!!!!!! error rendering portlet " + portlet + ": " + e1);
        e1.printStackTrace();
        System.out.println(" !!!!!!!!!!!! trying to continue...");
      }
    }
    return portletInfos;
  }

}
