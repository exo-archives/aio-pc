<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>

<f:view locale="en">
	<h:form>
		<font>
			<h:outputText value="#{MyFacesUserBean.name}"/>.
		</font>
		<h:commandButton action="back" value="Back" />
	</h:form>
</f:view>