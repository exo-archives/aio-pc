<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page import="javax.servlet.jsp.JspException" %>
<%@ page import="com.sun.ts.tests.portlet.common.util.ResultWriter" %>

<%
    String TEST_NAME = "ActionURLIllegalPortletModeTest";
    ResultWriter resultWriter = new ResultWriter(TEST_NAME);

    try {
%>
        <portlet:actionURL portletMode="anObviouslyIllegalPortletMode">
        </portlet:actionURL>
<%
        resultWriter.setStatus(ResultWriter.FAIL);

        resultWriter.addDetail("JspException was not thrown when the "
                               + "specified portlet mode was illegal.");
    } catch (JspException e) {
        resultWriter.setStatus(ResultWriter.PASS);
    }
%>
<%= resultWriter.toString() %>