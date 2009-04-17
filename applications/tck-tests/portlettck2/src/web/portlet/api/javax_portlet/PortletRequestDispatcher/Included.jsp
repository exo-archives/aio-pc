<%@ page import="com.sun.ts.tests.portlet.common.util.ResultWriter" %>
<%
    String TEST_NAME = "IncludeJSPTest";
    ResultWriter resultWriter = new ResultWriter(TEST_NAME);
    String expectedResult = TEST_NAME;
    String result = (String)request.getAttribute(TEST_NAME);

    if ((result != null) && result.equals(expectedResult)) {
        resultWriter.setStatus(ResultWriter.PASS);
    } else {
        resultWriter.setStatus(ResultWriter.FAIL);
        resultWriter.addDetail("Expected result = " + expectedResult);
        resultWriter.addDetail("Actual result = " + result);
    }
%>
<%= resultWriter.toString() %>
