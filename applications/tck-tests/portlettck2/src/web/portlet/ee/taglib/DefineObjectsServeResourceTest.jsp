<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page import="com.sun.ts.tests.portlet.ee.taglib.DefineObjectsVarComparator"%>
<portlet:defineObjects/>
<%
    String TEST_NAME = "DefineObjectsServeResourceTest";

	DefineObjectsVarComparator defineObjectsVarComparator = 
		new DefineObjectsVarComparator(TEST_NAME);
	
	defineObjectsVarComparator.checkResourceRequest(request, resourceRequest);
	defineObjectsVarComparator.checkResourceResponse(request, resourceResponse);
	defineObjectsVarComparator.checkPortletConfig(request, portletConfig);
	defineObjectsVarComparator.checkPortletSession(request, portletSession);
	defineObjectsVarComparator.checkPortletSessionScope(request, portletSessionScope);
	defineObjectsVarComparator.checkPortletPreferences(request, portletPreferences);
	defineObjectsVarComparator.checkPortletPreferencesValues(request, portletPreferencesValues);   
%>
<%= defineObjectsVarComparator.getResult() %>
