/*
 * Copyright 2007 IBM Corporation
 */

package com.sun.ts.tests.portlet.common.util;

/**
 * Supporting class that checks a string for characters 
 * that are not converted to their corresponding
 * character entity codes. 
 * 
 * @author Oliver Spindler <olisp_suhl@yahoo.com>
 */
public class EscapeXmlTester {
	
	/**
	 * Checks the testString for the following characters that should
	 * be converted to their corresponding character entity codes: 
	 * <, >, &, ', "
	 * 
	 * @param testString the string to test
	 * @return true if the string does not contain unescaped characters, false otherwise
	 */
	public static boolean isXmlEscaped(String testString){
		boolean result=true;
		if(testString.contains("<")){
			result=false;
		}
		if(testString.contains(">")){
			result=false;
		}
		if(testString.contains("'")){
			result=false;
		}
		if(testString.contains("\"")){
			result=false;
		}
		if(testString.contains("&")&&!testString.matches("(?!.*&[^;]*&.*|.*&[^;]*$).*")){
			result=false;
		}
		return result;
	}
}
