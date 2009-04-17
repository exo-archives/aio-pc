<%@ page language="java" %>
<%@ page contentType="text/html" %>
<%String contextPath  =  request.getContextPath();%>
<%String loginAction = contextPath + "/j_security_check";%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
  <title>eXo Platform Test Portal - Authorisation required</title>
  <link rel="stylesheet" type='text/css' href="<%=contextPath%>/exopcstyle.css" />
</head>
<body bgcolor='#FFFFFF' onLoad="document.getElementById('j_username').focus()">
    <!--  h1>eXo Platform Test Portal - Authorisation required </h1  -->
    <table>
    <tr>
        <td><img src="<%=contextPath%>/img/SigninPaper162x207.jpg"></td>
        <td>    Enter your login and password to view requested content: <br>
             <form name="loginForm" method="post" action="<%=loginAction%>">
             <input name="j_username" id="j_username" value="admin"/>
             <input type="password" name="j_password" id="j_password"  value="admin"/>
             <input name="submit" type="submit" value="Login">
           </form>
        </td>
    </tr>
    </table>
</body>  
</html>

