<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>

<f:view locale="en">
  <f:loadBundle basename="Language" var="text" />

  <h:form>
    <font>
      <h:outputText value="#{text.welcome}" />, <h:outputText value="#{SunFacesUserBean.name}"/>.
    </font>
    <h:commandButton action="back" value="Back" />
  </h:form>
</f:view>