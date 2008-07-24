/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * @(#)MethodFactory.java	1.8 03/05/30
 */

/*
 * @(#)MethodFactory.java	1.8 05/30/03
 */

package com.sun.ts.tests.common.webclient.http;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.OptionsMethod;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpMethod;
import com.sun.ts.lib.porting.TSURL;
import com.sun.ts.lib.util.TestUtil;

/**
 * Simple factory class which returns HttpMethod implementations
 * based on a request line.
 * <p>For example, a request line of <tt>GET /index.jsp HTTP/1.0</tt>
 * would return an HttpMethod implementation that handles GET requests
 * using HTTP/1.0.
 * </p>
 */

public class MethodFactory {

    /**
     * HTTP GET
     */
    private static final String GET_METHOD = "GET";

    /**
     * HTTP POST
     */
    private static final String POST_METHOD = "POST";

    /**
     * HTTP HEAD
     */
    private static final String HEAD_METHOD = "HEAD";

    /**
     * HTTP PUT
     */
    private static final String PUT_METHOD = "PUT";

    /**
     * HTTP DELETE
     */
    private static final String DELETE_METHOD = "DELETE";

    /**
     * HTTP OPTIONS
     */
    private static final String OPTIONS_METHOD = "OPTIONS";

    /**
     * TSURL implementation
     */
    private static final TSURL TS_URL = new TSURL();
    /**
     * Private constructor as all interaction with this class
     * is through the getInstance() method.
     */
    private MethodFactory() {
    }

/*
 * public methods
 * ========================================================================
 */

    /**
     * Returns the approriate request method based on the provided
     * request string.  The request must be in the format of
     * METHOD URI_PATH HTTP_VERSION, i.e. GET /index.jsp HTTP/1.1.
     *
     * @return HttpMethod based in request.
     */
    public static HttpMethod getInstance(String request) {
        StringTokenizer st = new StringTokenizer(request);
        String method = null;
        String query = null;
        String uri = null;
        String version = null;
        try {
            method = st.nextToken();
            uri = TS_URL.getRequest(st.nextToken());
            version = st.nextToken();
        } catch (NoSuchElementException nsee) {
            throw new IllegalArgumentException("Request provided: " + request +
                                               " is malformed.");
        }

        // check to see if there is a query string appended
        // to the URI
        int queryStart = uri.indexOf('?');
        if (queryStart != -1) {
            query = uri.substring(queryStart + 1);
            uri = uri.substring(0, queryStart);
        }

        HttpMethodBase req = null;

        if (method.equals(GET_METHOD)) {
            req = new GetMethod(uri);
        } else if (method.equals(POST_METHOD)) {
            req = new PostMethod(uri);
        } else if (method.equals(PUT_METHOD)) {
            req = new PutMethod(uri);
        } else if (method.equals(DELETE_METHOD)) {
            req = new DeleteMethod(uri);
        } else if (method.equals(HEAD_METHOD)) {
            req = new HeadMethod(uri);
        } else if (method.equals(OPTIONS_METHOD)) {
            req = new OptionsMethod(uri);
        } else {
            throw new IllegalArgumentException("Invalid method: " + method);
        }

        setHttpVersion(version, req);

        if (query != null) {
            req.setQueryString(query);
        }

        return req;
    }

/*
 * private methods
 * ========================================================================
 */

    /**
     * Sets the HTTP version for the method in question.
     *
     * @param version HTTP version to use for this request
     * @param method method to adjust HTTP version
     */
    private static void setHttpVersion(String version, HttpMethodBase method) {
        final String oneOne = "HTTP/1.1";
        method.setHttp11(version.equals(oneOne) ? true : false);
    }
}
