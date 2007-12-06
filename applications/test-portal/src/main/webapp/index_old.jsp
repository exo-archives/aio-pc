<%@ page import="java.util.*"%>

<%
String resource = (String) session.getAttribute("resource");
if (resource != null) {
	%><%=resource%><%
	session.removeAttribute("resource");
  return;
}
%>

<html>
<meta HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=UTF-8">
<title>Portlet rendering</title>
<h1>Portlets</h1>
<body width="100%">



<%
ArrayList outsses = (ArrayList) session.getAttribute("outs");
if (request.getParameter("fis")!=null) {
  //submitted form
  session.removeAttribute("myatr");
  ArrayList myatr = new ArrayList();
  try {
    if (outsses != null) {
      for (int i = 1; i <= outsses.size(); i++) {
        if (request.getParameter("n"+i+"n")!=null)  {
          if (request.getParameter("n"+i+"n").equals("on")){
            // is on
            myatr.add("checked");
          } else {
            //isn't on
            myatr.add("");
          }
        } else {
          //is null
          myatr.add("");
        }
      }
    } else {
      //session.setAttribute("myatr", null);
    }
  } catch (Exception e1) {
    e1.printStackTrace();
    session.setAttribute("myatr", null);
  }
  session.setAttribute("myatr",myatr);
} else {
  //not submitted form
  ArrayList myatr0 = (ArrayList) session.getAttribute("myatr");
  if (myatr0 != null) {
    //there is session
    // all ok
  } else {
    //there isn't session
    // haven't session myatr
    ArrayList myatr = new ArrayList();
    try {
      if (outsses != null) {
        for (int i = 1; i <= outsses.size(); i++) {
          myatr.add("");
        }
      } else {
        //session.setAttribute("myatr", null);
      }
    } catch (Exception e1) {
      e1.printStackTrace();
      session.setAttribute("myatr", null);
    }
    session.setAttribute("myatr",myatr);
  }
}
%>

<%ArrayList ids2 = (ArrayList) session.getAttribute("fqTitles");%>
<%ArrayList titles2 = (ArrayList) session.getAttribute("titles");%>
<%ArrayList outs2 = (ArrayList) session.getAttribute("outs");%>
<%ArrayList myatr2 = (ArrayList) session.getAttribute("myatr"); %>
<%try {%>
  <%if (outs2 != null) {%>
        <form method="POST" name="checkPortlet" onSubmit="javascript:window.location.href">
        <input type="hidden" name="fis" id="fis" value="yes">
      <table width="100%" border="1">
    <tr><th valign="center" bgcolor="#D0F0D0" colspan="4"><h1>Please, select some of the following portlets</h1></th></tr>
      <tr><td align="center">Checked</td>
    <td align="center">Num</td>
    <td align="center">Portlet Id</td>
    <td align="center">Portlet Name</td></tr>
    <%for (int i = 1; i <= outs2.size(); i++) {%>
        <%String title2 = (String) titles2.get(i - 1);%>
        <%String id2 = (String) ids2.get(i - 1);%>
        <%String myatr2str = (String) myatr2.get(i - 1);%>
    <tr><td align="center">
    <input type="checkbox" name="n<%=i%>n" ID="n<%=i%>n" <%=myatr2str%> onClick="checkPortlet.submit()">
    </td><td align="center"><%=i%></td><td valign="center" bgcolor=""><%=id2%></td>
    <td valign="center" bgcolor=""><%=title2%></td></tr>
      <%}%>
  </table>
      </form>
  <%} else {%>
    <table width="100%" border="1"><tr><td>There's no portlet data to show</td></tr></table>
  <%}%>
<%} catch (Exception e1) {%>
<%  e1.printStackTrace();%>
<%}%>


<%ArrayList titles = (ArrayList) session.getAttribute("titles");%>
<%ArrayList outs = (ArrayList) session.getAttribute("outs");%>
<%ArrayList modes = (ArrayList) session.getAttribute("modes");%>
<%ArrayList portletapps = (ArrayList) session.getAttribute("portletapps");%>
<%ArrayList myatr = (ArrayList) session.getAttribute("myatr"); %>

<%String reqURL = new String(request.getRequestURL()+"?");%>
<%String reqContextPath = new String(request.getContextPath().substring(1,request.getContextPath().length())+":");%>

<%try {%>
  <%if (outs != null) {%>
    <%for (int i = 1; i <= outs.size(); i++) {%>
        <%String myatrstr = (String) myatr.get(i - 1);%>
      <%if (myatrstr=="checked") {%>
        <%String title = (String) titles.get(i - 1);%>
        <%String hs = (String) outs.get(i - 1);%>
        <%String mymode = (String) modes.get(i - 1);%>
        <%String portletapp = (String) portletapps.get(i - 1);%>
        <table width="100%" border="1">
        <tr><th valign="center" bgcolor="#D0F0D0"><h1><%=title%></h1>
          <%
          String resMode = reqURL;
          resMode += reqContextPath + "componentId=" + portletapp;
          resMode += "&" + reqContextPath + "type=action";
          resMode += "&" + reqContextPath + "isSecure=true";
          resMode += "&" + reqContextPath + "portletMode=";
          String[] ss2 = mymode.split("[.]");
          for (int ii = 0; ii < ss2.length; ii++) {
            %>
            <a href="<%=(resMode + ss2[ii] + "&" + reqContextPath + "windowState=normal")%>"><%=ss2[ii]%></a> 
            <%
          }
          %>  
        </th></tr>
        <tr><td><%=hs%></td></tr>
        </table>
      <%}%>
    <%}%>
  <%} else {%>
    <table width="100%" border="1"><tr><th>There's no portlet data to show</th></tr></table>
  <%}%>
<%} catch (Exception e1) {%>
  <%  e1.printStackTrace();%>
<%}%>
                    
</body>
</html>
<%//session.invalidate();%>
<%System.out.println("-------------------------------------------------------------");%>