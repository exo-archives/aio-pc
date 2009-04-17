<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page import="com.sun.ts.tests.portlet.ee.taglib.DefineObjectsVarComparator"%>
<portlet:defineObjects/>
<%
    String TEST_NAME = "DefineObjectsRenderTest";

	DefineObjectsVarComparator defineObjectsVarComparator = 
		new DefineObjectsVarComparator(TEST_NAME);
	
	defineObjectsVarComparator.checkRenderRequest(request, renderRequest);
	defineObjectsVarComparator.checkRenderResponse(request, renderResponse);
	defineObjectsVarComparator.checkPortletConfig(request, portletConfig);
	defineObjectsVarComparator.checkPortletSession(request, portletSession);
	defineObjectsVarComparator.checkPortletSessionScope(request, portletSessionScope);
	defineObjectsVarComparator.checkPortletPreferences(request, portletPreferences);
	defineObjectsVarComparator.checkPortletPreferencesValues(request, portletPreferencesValues);   
%>
<%= defineObjectsVarComparator.getResult() %>