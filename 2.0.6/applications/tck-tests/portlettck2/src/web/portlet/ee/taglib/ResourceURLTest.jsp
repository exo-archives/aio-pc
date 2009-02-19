<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page import="com.sun.ts.tests.portlet.common.util.tags.PortletURLTag" %>

<%! PortletURLTag urlTag = new PortletURLTag(); %>
<%= urlTag.getStartTag() %><portlet:resourceURL>
  <portlet:param name="ResourceURLTest" value="One"/>
  <portlet:param name="ResourceURLTest" value="Two"/>
  <portlet:param name="ResourceURLTest" value="Three"/>
</portlet:resourceURL><%= urlTag.getEndTag() %>