<%@ page buffer="8kb" autoFlush="false"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ page import="com.sun.ts.tests.portlet.ee.taglib.DefineObjectsVarComparator"%>
<portlet:defineObjects/>
<% 
    String TEST_NAME = "DefineObjectsProcessActionTest";

	DefineObjectsVarComparator defineObjectsVarComparator = 
		new DefineObjectsVarComparator(TEST_NAME);
	
	defineObjectsVarComparator.checkActionRequest(request, actionRequest);
	defineObjectsVarComparator.checkActionResponse(request, actionResponse);
	defineObjectsVarComparator.checkPortletConfig(request, portletConfig);
	defineObjectsVarComparator.checkPortletSession(request, portletSession);
	defineObjectsVarComparator.checkPortletSessionScope(request, portletSessionScope);
	defineObjectsVarComparator.checkPortletPreferences(request, portletPreferences);
	defineObjectsVarComparator.checkPortletPreferencesValues(request, portletPreferencesValues);
	
	session.setAttribute("result", defineObjectsVarComparator.getResult());
	
	//make sure that there are no characters or empty lines after out.clearBuffer()!!
	out.clearBuffer();%>