/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.ts.tests.portlet.api.javax_portlet.RenderResponse;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.sun.ts.tests.portlet.common.util.ResultWriter;

/**
 * This class tests for a valid Java identifier returned by the
 * getNamespace() method.
 */
public class ValidIdentifierTestPortlet extends GenericPortlet {
    public static final String TEST_NAME = "ValidIdentifierTest";

    public void render(RenderRequest request, RenderResponse response)
        throws PortletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        String token = response.getNamespace();

        if (isValidIdentifier(resultWriter, token)) {
            resultWriter.setStatus(ResultWriter.PASS);
        }

        out.println(resultWriter.toString());
    }

    /*
     * An identifier is an unlimited-length sequence of Java letters
     * and Java digits, the first of which must be a Java letter.  An
     * identifier cannot have the same spelling (Unicode character
     * sequence) as a keyword, boolean literal, or the null literal.
     */
    protected boolean isValidIdentifier(ResultWriter resultWriter, String token) {
        if ((token == null) || (token.length() == 0)) {
            resultWriter.setStatus(ResultWriter.FAIL);
            resultWriter.addDetail("getNamespace() returned null.");
            return false;
        }

        if (!Character.isJavaIdentifierStart(token.charAt(0))) {
            resultWriter.setStatus(ResultWriter.FAIL);

            resultWriter.addDetail("First character in value returned "
                                   + "by getNamespace() is not "
                                   + "permissible as a valid first "
                                   + "character for a Java identifier: "
                                   + token);

            return false;
        }

        for (int i = 1; i < token.length(); i++) {
            if (!Character.isJavaIdentifierPart(token.charAt(i))) {
                resultWriter.setStatus(ResultWriter.FAIL);

                resultWriter.addDetail("Character number " + i + " in "
                                       + "value returned by "
                                       + "getNamespace() is not "
                                       + "permissible as a valid "
                                       + "character for a Java "
                                       + "identifier: " + token);

                return false;
            }
        }

        if (token.equals("abstract") || token.equals("boolean")
            || token.equals("break") || token.equals("byte")
            || token.equals("case") || token.equals("catch")
            || token.equals("char") || token.equals("class")
            || token.equals("const") || token.equals("continue")
            || token.equals("default") || token.equals("do")
            || token.equals("double") || token.equals("else")
            || token.equals("extends") || token.equals("final")
            || token.equals("finally") || token.equals("float")
            || token.equals("for") || token.equals("goto")
            || token.equals("if") || token.equals("implements")
            || token.equals("import") || token.equals("instanceof")
            || token.equals("int") || token.equals("interface")
            || token.equals("long") || token.equals("native")
            || token.equals("new") || token.equals("package")
            || token.equals("private") || token.equals("protected")
            || token.equals("public") || token.equals("return")
            || token.equals("short") || token.equals("static")
            || token.equals("strictfp") || token.equals("super")
            || token.equals("switch") || token.equals("synchronized")
            || token.equals("this") || token.equals("throw")
            || token.equals("throws") || token.equals("transient")
            || token.equals("try") || token.equals("void")
            || token.equals("volatile") || token.equals("while")
            || token.equals("true") || token.equals("false")
            || token.equals("null")) {

            resultWriter.setStatus(ResultWriter.FAIL);

            resultWriter.addDetail("Value returned by getNamespace() "
                                   + "is a reserved keyword: " + token);

            return false;
        }

        return true;
    }
}
