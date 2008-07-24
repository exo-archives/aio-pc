/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * Copyright 2006 IBM Corporation
 */

package com.sun.ts.tests.portlet.api.javax_portlet.PortletSession;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletSession;
import java.util.Enumeration;
import java.util.Vector;
import javax.portlet.PortletException;
import java.io.IOException;
import java.io.PrintWriter;

import com.sun.ts.tests.portlet.common.util.ResultWriter;


/**
 * 	A test for PortletSession.getAttributeNames() method
 * 
 * @author <a href="mailto:schieck@inf.uni-jena.de">Kay Schieck</a>
 * @since v2.0
 */


public class GetAttributeNamesTestPortlet extends GenericPortlet {

    public static String TEST_NAME = "GetAttributeNamesTest";


    public void render(RenderRequest request, RenderResponse response ) throws PortletException, java.io.IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        PortletSession session = request.getPortletSession( true );

        // Binding values with the Session
        session.setAttribute( "object", "JSP", PortletSession.PORTLET_SCOPE );
        session.setAttribute( "object", "PORTLET", PortletSession.PORTLET_SCOPE);
        session.setAttribute( "object1", "JAVA",PortletSession.PORTLET_SCOPE );

        int count = 0;
        int expectedCount = 2;
        String expectedResult1 = "object";
        boolean expectedResult1Found = false;
        String expectedResult2 = "object1";
        boolean expectedResult2Found = false;
        Enumeration enumeration = session.getAttributeNames();

        if(enumeration.hasMoreElements() ) {
            Vector v = new Vector();

            while(enumeration.hasMoreElements() ) {
                String name =(String ) enumeration.nextElement();

                if(name.equalsIgnoreCase( expectedResult1 ) ) {
                    if(!expectedResult1Found ) {
                        count++;
                        expectedResult1Found = true;
                    } else {
                        resultWriter.setStatus(ResultWriter.FAIL);
                        resultWriter.addDetail( "    PortletSession.getAttributeNames() method return the same name twice" );
                        resultWriter.addDetail( "    The name already received was " + expectedResult1 );
                    }
                } else if(name.equalsIgnoreCase( expectedResult2 ) ) {
                    if(!expectedResult2Found ) {
                        count++;
                        expectedResult2Found = true;
                    } else {
                        resultWriter.setStatus(ResultWriter.FAIL);
                        resultWriter.addDetail( "    PortletSession.getAttributeNames() method return the same name twice ");
                        resultWriter.addDetail( "    The name already received was " + expectedResult2 );
                    }
                } else {
                    v.add( name );
                }

            }

            if(count != expectedCount ) {
                resultWriter.setStatus(ResultWriter.FAIL);
                resultWriter.addDetail( "    PortletSession.getAttributeNames() method did not return the correct number of names " );
                resultWriter.addDetail( "    Expected count = " + expectedCount );
                resultWriter.addDetail( "    Actual count = " + count  );
                resultWriter.addDetail( "    The expected names received were :" );

                if(expectedResult1Found ) {
                    resultWriter.addDetail( expectedResult1 );
                }

                if(expectedResult2Found ) {
                    resultWriter.addDetail( expectedResult2 );
                }

                resultWriter.addDetail( "    Other names received were :" );

                for(int i = 0;i <= v.size() - 1;i++ ) {
                    resultWriter.addDetail( "     " + v.elementAt( i ).toString()  );
                }
            } else {
                resultWriter.setStatus(ResultWriter.PASS);
            }
        } else {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail( "    PortletRequest.getAttributeNames() an empty enumeration " );

        }
        out.println(resultWriter.toString());
    }
}
