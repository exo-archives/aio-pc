/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.EventResponse;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 *
 */

@XmlRootElement
public class Address implements Serializable {
	private String street;
	private String city;
	
	public void setStreet(String s) { street = s; }
	public String getStreet() { return street; }
	public void setCity(String c) { city = c; }
	public String getCity() { return city; }
}
