/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * @(#)TokenizedValidator.java	1.12 03/05/16
 */

/*
 * @(#)TokenizedValidator.java	1.12 05/16/03
 */

package com.sun.ts.tests.common.webclient.validation;

import java.util.StringTokenizer;
import java.io.OutputStreamWriter;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.webclient.Goldenfile;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * <pre>This class provides all of the functionality of the
 * WebValidatorBase class.  Additionally, it will compare
 * the server's response body with the test case's configured
 * goldenfile using a StringTokenizer.</pre>
 */
public class TokenizedValidator extends WebValidatorBase {

    /**
     * System property that will cause the specified goldenfile
     * to be written if it doesn't already exist.
     */
    private static final String RECORD_GF = "ts.record.gf";

    /**
     * Creates a new instance of TokenizedValidator
     */
    public TokenizedValidator() {
    }

/*
 * protected methods
 * ========================================================================
 */

    /**
     * Compare the server response and golenfile using a StringTokenizer.
     *
     * @return true if response and goldenfile are the same.
     * @throws IOException if an error occurs will processing the Goldenfile
     */
    protected boolean checkGoldenfile() throws IOException {
        String gf = null;
        String path = _case.getGoldenfilePath();
        String enc = _res.getResponseEncoding();

        if (path == null) {
            return true;
        }

        Goldenfile file = new Goldenfile(_case.getGoldenfilePath(), enc);

        try {
            gf = file.getGoldenFileAsString();
        } catch (IOException ioe) {
            TestUtil.logErr("[TokenizedValidator] Unexpected exception while accessing " +
            "goldenfile! " + ioe.toString());
            return false;
        }

        String response = _res.getResponseBodyAsString();
        StringTokenizer gfTokenizer = new StringTokenizer(gf);
        StringTokenizer resTokenizer = new StringTokenizer(response);
        int gfCount = gfTokenizer.countTokens();
        int resCount = resTokenizer.countTokens();

	    // Logic to handle the recording of goldenfiles.
        if (gf.equals("NO GOLDENFILE FOUND") &&
            Boolean.getBoolean(RECORD_GF)) {

            TestUtil.logTrace("[TokenizedValidator][INFO] RECORDING GOLDENFILE: " +
                              path);
            OutputStreamWriter out = new OutputStreamWriter(
            new FileOutputStream(path), enc);
            out.write(response);
            out.flush();
            out.close();
        }

        // If the token counts are the same, continue checking
        // each individual token, otherwise, immediately fail.
        if (gfCount == resCount) {
            while (gfTokenizer.hasMoreTokens()) {
                String exp = gfTokenizer.nextToken();
                String res = resTokenizer.nextToken();
                if (!exp.equals(res)) {
                    StringBuffer sb = new StringBuffer(255);
                    sb.append("[TokenizedValidator]: Server's response and ");
                    sb.append("goldenfile to not match!\n");
                    sb.append("\n            Goldenfile token: ").append(exp);
                    sb.append("\n            Response token:   ").append(res);
                    TestUtil.logErr(sb.toString());
                    dumpResponseInfo(response, gf);
                    return false;
                }
            }
        } else {
            TestUtil.logErr("[TokenizedValidator]: Token count between server response " +
                            "and goldenfile do not match.\n Response Token" +
                            "count: " + resCount + "\nGoldenfile Token count: " +
                            gfCount);

            dumpResponseInfo(response, gf);
            return false;
        }
        TestUtil.logTrace("[TokenizedValidator]: Server's response matches the " +
        "configured goldenfile.");
        return true;
    }

/*
 * private methods
 * ========================================================================
 */

    /**
     * Dumps the response from the server and the content of the Goldenfile/
     * @param serverResponse the response body from the server.
     * @param goldenFile the test goldenfile
     */
    private static void dumpResponseInfo(String serverResponse, String goldenFile) {
        StringBuffer sb = new StringBuffer(255);
        sb.append("\nServer Response (below):\n");
        sb.append("------------------------------------------\n");
        sb.append(serverResponse);
        sb.append("\n------------------------------------------\n");
        sb.append("\nGoldenfile (below):\n");
        sb.append("------------------------------------------\n");
        sb.append(goldenFile);
        sb.append("\n------------------------------------------\n");
        TestUtil.logErr(sb.toString());
    }
}
