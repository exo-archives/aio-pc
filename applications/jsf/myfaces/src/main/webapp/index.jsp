<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>

<portlet:defineObjects />

<form action="<portlet:actionURL />" method="post" name="fm">
  <input name="command" type="hidden" value="">
  <input name="id" type="hidden" value="">
</form>

<f:view locale="en">
  <h:form>
    <h:inputText id="name" required="true" value="#{MyFacesUserBean.name}" />
    <font>
      <h:message for="name" />
    </font>
    <h:commandButton action="submit" value="Submit" />
  </h:form>
</f:view>


