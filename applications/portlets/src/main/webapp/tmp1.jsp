<%out.println("erez ddd");%>
<%=new java.util.Date()%>
<%
RequestDispatcher rd=request.getRequestDispatcher("tmp2.jsp");
rd.include(request,response);
%>
