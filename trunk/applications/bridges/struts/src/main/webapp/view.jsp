<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://portals.apache.org/bridges/struts/tags-portlet-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="shtml"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
  <html:html locale="true">
  <html:xhtml />
  <head>
    <title>Struts Bridge - Simple Portlet</title>
  </head>
  <body>     
    <p>
      <b>Struts Bridge - Simple Portlet - VIEW mode</b>
    </p>
    <p>
      <img src="<html:rewrite page="/images/banner-left-390x69.jpg" resourceURL="true"/>"/><br />
      image with bridges taglib url generated 'html:rewrite' (resourceURL)
    </p>
    <% int urlhostindex = request.getRequestURL().indexOf(":") + 3; %>
    <% int urlqueryindex =request.getRequestURL().substring(urlhostindex).indexOf("/"); %>        
    <% String urlprefix = request.getRequestURL().substring(0, urlhostindex + urlqueryindex) + request.getContextPath(); %>        
    <p>
      <img src="<%=urlprefix + "/images/banner-left-390x69.jpg" %>"/><br />
      image with simple img tag
    </p>
    <p>
      <html:link page="/View.do">No parameters</html:link> (renderRequest)
    </p>
    <p>
      <html:link page="/View.do?abc=123">Parameter abc=123 set by url</html:link> (renderRequest)<br />
    </p>
    <p>
      <html:link page="/ViewSubmit.do?abc=456" actionURL="true">Parameter abc=456 set by url</html:link> (actionRequest)<br />
    </p>
    <html:form action="/ViewSubmit.do">
      Parameter abc set by form (actionRequest)<br />
      <html:text property="abc" value="789" />
      <html:submit />
    </html:form>
    <div style="width: 30%; ">
      <logic:present parameter="abc">
        <bean:parameter id="abc" name="abc"/>
        <h1>abc=<bean:write name="abc"/></h1>
      </logic:present>
      <logic:notPresent parameter="abc">
        <h1>abc is not present</h1>
      </logic:notPresent>
    </div>
    <br><br>


<html:form action="/file.do" enctype="multipart/form-data" method="post" focus="file">
<table border="0" cellpadding="0" cellspacing="0">
<tr>
    <td>
        File Location
    </td>
    <td style="padding-left: 10px;"></td>
    <td>
        <shtml:file property="file" />
    </td>
</tr>
</table>
<br>
<html:submit>Upload File</html:submit>
</html:form>
    <div style="width: 30%; ">
      <logic:present parameter="file">
        <bean:parameter id="file" name="file"/>
        <h1>file=<bean:write name="file"/></h1>
      </logic:present>
      <logic:notPresent parameter="file">
        <h1>file is not present</h1>
      </logic:notPresent>
    </div>

    
  </body>
</html:html>
