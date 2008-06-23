<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<portlet:defineObjects/>

<a href="<portlet:renderURL copyCurrentRenderParameters="true">
	<portlet:param name="param1" value="value1" />
</portlet:renderURL>">url1</a>

<a href="<portlet:renderURL copyCurrentRenderParameters="true">
	<portlet:param name="param2" value="value2" />
</portlet:renderURL>">url2</a>

<a href="<portlet:renderURL copyCurrentRenderParameters="true">
	<portlet:param name="param3" value="value3" />
</portlet:renderURL>">url3</a>

<a href="<portlet:resourceURL>
	<portlet:param name="param4" value="value4" />
</portlet:resourceURL>">url4</a>
