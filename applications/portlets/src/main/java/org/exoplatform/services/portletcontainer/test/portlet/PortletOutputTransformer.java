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
package org.exoplatform.services.portletcontainer.test.portlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.xml.transform.NotSupportedIOTypeException;
import org.exoplatform.services.xml.transform.html.HTMLTransformer;
import org.exoplatform.services.xml.transform.html.HTMLTransformerService;
import org.exoplatform.services.xml.transform.trax.Constants;
import org.exoplatform.services.xml.transform.trax.TRAXTemplates;
import org.exoplatform.services.xml.transform.trax.TRAXTransformer;
import org.exoplatform.services.xml.transform.trax.TRAXTransformerService;

/**
 * Created by The eXo Platform SAS . <p/> Url rewriter for XHTML script
 * href="original-url" will be rewrited as href="<portlet-url/>?<href-param/>=<original-url/>"
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id$
 */
public class PortletOutputTransformer {

  /**
   * param name value.
   */
  public static final String PARAM_NAME_VALUE = "url";

  // xsl param names

  /**
   * portal URI param name.
   */
  protected static final String PORTAL_URI = "portalURI"; // "/portal/faces/public/exo"

  /**
   * portlet URI param name.
   */
  protected static final String PORTLET_URI = "portletURI"; // "/category"

  /**
   * context path param name.
   */
  protected static final String PORTAL_CONTEXT_PATH = "portalContextPath"; // "/portal"

  /**
   * query string param name.
   */
  protected static final String PORTAL_QUERY_STRING = "portalQueryString"; // "portal:componentId=news&portal:windowState=minimized"

  /**
   * namespace param name.
   */
  protected static final String PARAM_NAMESPACE = "paramNamespace"; // "portlet:struts1_1:"

  /**
   * portal URI.
   */
  private String portalURI = ""; // "/portal/faces/public/exo"

  /**
   * portlet URI.
   */
  private String portletURI = ""; // "/portal/faces/public/exo"

  /**
   * context path.
   */
  private String portalContextPath = ""; // "/portal"

  /**
   * query string.
   */
  private String portalQueryString = ""; // "portal:componentId=news&portal:windowState=minimized"

  /**
   * namespace.
   */
  private String paramNamespace = ""; // "portlet:struts1_1:"

  /**
   * rewrite style.
   */
  public static final String HTML_URL_REWRITE_STYLE = Constants.XSLT_DIR + "/html-url-rewite.xsl";

  /**
   * templates.
   */
  private static TRAXTemplates templates;

  /**
   * @return templates
   * @throws TransformerException exception
   * @throws IOException exception
   */
  protected final TRAXTemplates getTemplates() throws TransformerException, IOException {
    if (templates == null) {
      InputStream inputStream = Thread.currentThread().getContextClassLoader().getResource(
          HTML_URL_REWRITE_STYLE).openStream();
      TRAXTransformerService traxService = (TRAXTransformerService) ExoContainerContext
          .getTopContainer().getComponentInstanceOfType(TRAXTransformerService.class);
      try {
        templates = traxService.getTemplates(new StreamSource(inputStream));
      } catch (NotSupportedIOTypeException ex) {
        new IOException(ex.getMessage());
      }
    }
    return templates;
  }

  /**
   * @param input InputStream source html
   * @param output OutputStream output html
   * @throws TransformerException exception
   * @throws InstantiationException exception
   * @throws IOException exception
   */
  public final void rewrite(final InputStream input, final OutputStream output) throws TransformerException,
      InstantiationException,
      IOException {

    HTMLTransformerService htmlService = (HTMLTransformerService) ExoContainerContext
        .getTopContainer().getComponentInstanceOfType(HTMLTransformerService.class);

    HTMLTransformer htmlTransformer = htmlService.getTransformer();
    TRAXTransformer traxTransformer = getTemplates().newTransformer();

    // set transform params
    traxTransformer.setParameter(PORTAL_URI, portalURI);
    traxTransformer.setParameter(PORTLET_URI, portletURI);
    traxTransformer.setParameter(PORTAL_CONTEXT_PATH, portalContextPath);
    traxTransformer.setParameter(PORTAL_QUERY_STRING, portalQueryString);
    traxTransformer.setParameter(PARAM_NAMESPACE, paramNamespace);

    try {
      htmlTransformer.initResult(traxTransformer.getTransformerAsResult());
      traxTransformer.initResult(new StreamResult(output));
      htmlTransformer.transform(new StreamSource(input));
    } catch (NotSupportedIOTypeException ex) {
      new IOException(ex.getMessage());
    }

  }

  /**
   * @param portalURI portal URI
   */
  public final void setPortalURI(final String portalURI) {
    this.portalURI = portalURI;
  }

  /**
   * @param portletURI portlet URI
   */
  public final void setPortletURI(final String portletURI) {
    this.portletURI = portletURI;
  }

  /**
   * @param portalContextPath context path
   */
  public final void setPortalContextPath(final String portalContextPath) {
    this.portalContextPath = portalContextPath;
  }

  /**
   * @param portalQueryString query string
   */
  public final void setPortalQueryString(final String portalQueryString) {
    this.portalQueryString = portalQueryString;
  }

  /**
   * @param paramNamespace namespace
   */
  public final void setParamNamespace(final String paramNamespace) {
    this.paramNamespace = paramNamespace;
  }

}
