/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * @(#)SunRIURL.java	1.18 03/04/18
 */


package com.sun.ts.lib.implementation.sun.common;

import java.net.*;
import java.util.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

/**
  * This is a J2EE Reference specific implementation of the TSURLInterface
  * which is to be used for J2EE-TS testing.  TS tests use this interface to
  * obtain the URL String to use to access a given web component.  If a given
  * J2EE Server implmentation requires that URLs be created in a different
  * manner, then this implementation can be replaced. 
  *
  * @author Kyle Grucci
  */
public class SunRIURL implements TSURLInterface
{
	private URL url = null;
	
	/**
	  * This method is called by TS tests to get the URL to use to access a
	  * given web component.
	  *
	  * @param       protocol - the name of the protocol.
	  * @param 		 host - the name of the host.
	  * @param 		 port - the port number.
	  * @param 		 file - the host file.
	  * @return      a valid URL object.
	  */
	public URL getURL(String protocol, String host, int port,
					 String file) throws MalformedURLException
	{
		try
		{
			url = new URL(protocol, host, port, file);      
		}
		catch(MalformedURLException e)
		{
			TestUtil.logErr("Failed during URL creation", e);
			throw e;
		}
		return url;
	}
	
	/**
	  * This method is called by TS tests to get the URL to use to access a
	  * given web component.
	  *
	  * @param       protocol - the name of the protocol.
	  * @param 		 host - the name of the host.
	  * @param 		 port - the port number.
	  * @param 		 file - the host file.
	  * @return      a valid URL as a String.
	  */
	public String getURLString(String protocol, String host, int port,
					 String file)
	{
		// Code to handle if dealing with a webservices endpoint 
  		// for DII (dynamic invocation). Need to create target 
 		// service endpoint URL string.
		String wsString = getWSURLString(protocol, host, port, file);
		if(wsString != null) {
		    TestUtil.logMsg("DII WSURLString="+wsString);
		    return wsString;
		}

		if (file.startsWith("/"))
		    return protocol + "://" + host + ":" + port + file;
		else
		    return protocol + "://" + host + ":" + port + "/" + file;
	}
	
	/**
	  * This method is called by TS tests to get the request string to use
	  * to access a given web component.
	  *
	  * @param 	 request - the request file.
	  * @return      a valid String object.
	  */
	public String getRequest(String request)
	{
	        return request;
	}  


	// Code to handle if dealing with a webservices endpoint 
	// for DII (dynamic invocation). Need to create target 
	// service endpoint URL string. The urlArray[][] contains
	// the target service endpoint properties and their 
        // corresponding values.

        private static String [][] urlArray = {
	 //jaxrpc/sharedwebservices/helloservice
         { "helloservice.endpoint.1", "/HelloService/ws4ee" },
	 { "helloservice.servlet.1",  "/HelloService/dummyservlet"},
    
	 //jaxrpc/ee/w2j/rpc/encoded/simpletest
         { "w2jsimpletest.endpoint.1", "/W2JRESimpleTest/ws4ee" },

	 //jaxrpc/ee/j2w/simpletest
         { "j2wst.endpoint.1", "/J2WST/ws4ee" },

	 // webservices/deploy/GenSvc
         { "gensvc.endpoint.1", "/GenSvc/ws4ee"},

	 // webservices/deploy/ServiceNPA
         { "servicenpa.endpoint.1", "/ServiceNPA/ws4ee"},

	 // webservices/deploy/ServicePW
         { "servicepw.endpoint.1", "/ServicePW/ws4ee"},
        };

    	private static Properties urlProps = new Properties();
	
    	static {
	    try {
                /* initialize urlProps */
                for (int i=0; i < urlArray.length; i++)
        	    urlProps.setProperty(urlArray[i][0], urlArray[i][1]);
            } catch(Exception ex) {}
    	}

	private String getWSURLString(String protocol, String host, int port,
				String file)
	{
            String urlPattern = urlProps.getProperty(file);
            if (urlPattern == null) 
		return null;
	    else
	    	return protocol + "://" + host + ":" + port + urlPattern;
	}
}
