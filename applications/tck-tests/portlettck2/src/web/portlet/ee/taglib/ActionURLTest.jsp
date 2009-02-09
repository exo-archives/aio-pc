<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page import="com.sun.ts.tests.portlet.common.util.tags.PortletURLTag" %>

<%! PortletURLTag urlTag = new PortletURLTag(); %>
<%= urlTag.getStartTag() %><portlet:actionURL>
  <portlet:param name="ActionURLTest" value="One"/>
  <portlet:param name="ActionURLTest" value="Two"/>
  <portlet:param name="ActionURLTest" value="Three"/>
</portlet:actionURL><%= urlTag.getEndTag() %>