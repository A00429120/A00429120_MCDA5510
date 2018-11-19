<%@page contentType="text/html;charset=UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<HTML>
<HEAD>
<TITLE>Result</TITLE>
</HEAD>
<BODY>
<H1>Result</H1>

<jsp:useBean id="sampleAssignment3Proxyid" scope="session" class="assignment3.com.web.service.Assignment3Proxy" />
<%
if (request.getParameter("endpoint") != null && request.getParameter("endpoint").length() > 0)
sampleAssignment3Proxyid.setEndpoint(request.getParameter("endpoint"));
%>

<%
String method = request.getParameter("method");
int methodID = 0;
if (method == null) methodID = -1;

if(methodID != -1) methodID = Integer.parseInt(method);
boolean gotMethod = false;

try {
switch (methodID){ 
case 2:
        gotMethod = true;
        java.lang.String getEndpoint2mtemp = sampleAssignment3Proxyid.getEndpoint();
if(getEndpoint2mtemp == null){
%>
<%=getEndpoint2mtemp %>
<%
}else{
        String tempResultreturnp3 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(getEndpoint2mtemp));
        %>
        <%= tempResultreturnp3 %>
        <%
}
break;
case 5:
        gotMethod = true;
        String endpoint_0id=  request.getParameter("endpoint8");
            java.lang.String endpoint_0idTemp = null;
        if(!endpoint_0id.equals("")){
         endpoint_0idTemp  = endpoint_0id;
        }
        sampleAssignment3Proxyid.setEndpoint(endpoint_0idTemp);
break;
case 10:
        gotMethod = true;
        assignment3.com.web.service.Assignment3 getAssignment310mtemp = sampleAssignment3Proxyid.getAssignment3();
if(getAssignment310mtemp == null){
%>
<%=getAssignment310mtemp %>
<%
}else{
%>
<TABLE>
<TR>
<TD COLSPAN="3" ALIGN="LEFT">returnp:</TD>
</TABLE>
<%
}
break;
case 15:
        gotMethod = true;
        String id_1id=  request.getParameter("id18");
            java.lang.String id_1idTemp = null;
        if(!id_1id.equals("")){
         id_1idTemp  = id_1id;
        }
        java.lang.String get15mtemp = sampleAssignment3Proxyid.get(id_1idTemp);
if(get15mtemp == null){
%>
<%=get15mtemp %>
<%
}else{
        String tempResultreturnp16 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(get15mtemp));
        %>
        <%= tempResultreturnp16 %>
        <%
}
break;
case 20:
        gotMethod = true;
        String id_2id=  request.getParameter("id23");
            java.lang.String id_2idTemp = null;
        if(!id_2id.equals("")){
         id_2idTemp  = id_2id;
        }
        String cardNumber_3id=  request.getParameter("cardNumber25");
            java.lang.String cardNumber_3idTemp = null;
        if(!cardNumber_3id.equals("")){
         cardNumber_3idTemp  = cardNumber_3id;
        }
        String expDate_4id=  request.getParameter("expDate27");
            java.lang.String expDate_4idTemp = null;
        if(!expDate_4id.equals("")){
         expDate_4idTemp  = expDate_4id;
        }
        String nameOnCard_5id=  request.getParameter("nameOnCard29");
            java.lang.String nameOnCard_5idTemp = null;
        if(!nameOnCard_5id.equals("")){
         nameOnCard_5idTemp  = nameOnCard_5id;
        }
        String unitPrice_6id=  request.getParameter("unitPrice31");
            java.lang.String unitPrice_6idTemp = null;
        if(!unitPrice_6id.equals("")){
         unitPrice_6idTemp  = unitPrice_6id;
        }
        String quantity_7id=  request.getParameter("quantity33");
            java.lang.String quantity_7idTemp = null;
        if(!quantity_7id.equals("")){
         quantity_7idTemp  = quantity_7id;
        }
        java.lang.String create20mtemp = sampleAssignment3Proxyid.create(id_2idTemp,cardNumber_3idTemp,expDate_4idTemp,nameOnCard_5idTemp,unitPrice_6idTemp,quantity_7idTemp);
if(create20mtemp == null){
%>
<%=create20mtemp %>
<%
}else{
        String tempResultreturnp21 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(create20mtemp));
        %>
        <%= tempResultreturnp21 %>
        <%
}
break;
case 35:
        gotMethod = true;
        String id_8id=  request.getParameter("id38");
            java.lang.String id_8idTemp = null;
        if(!id_8id.equals("")){
         id_8idTemp  = id_8id;
        }
        java.lang.String remove35mtemp = sampleAssignment3Proxyid.remove(id_8idTemp);
if(remove35mtemp == null){
%>
<%=remove35mtemp %>
<%
}else{
        String tempResultreturnp36 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(remove35mtemp));
        %>
        <%= tempResultreturnp36 %>
        <%
}
break;
case 40:
        gotMethod = true;
        java.lang.String getAll40mtemp = sampleAssignment3Proxyid.getAll();
if(getAll40mtemp == null){
%>
<%=getAll40mtemp %>
<%
}else{
        String tempResultreturnp41 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(getAll40mtemp));
        %>
        <%= tempResultreturnp41 %>
        <%
}
break;
case 43:
        gotMethod = true;
        String id_9id=  request.getParameter("id46");
            java.lang.String id_9idTemp = null;
        if(!id_9id.equals("")){
         id_9idTemp  = id_9id;
        }
        String choice_10id=  request.getParameter("choice48");
            java.lang.String choice_10idTemp = null;
        if(!choice_10id.equals("")){
         choice_10idTemp  = choice_10id;
        }
        String value_11id=  request.getParameter("value50");
            java.lang.String value_11idTemp = null;
        if(!value_11id.equals("")){
         value_11idTemp  = value_11id;
        }
        java.lang.String update43mtemp = sampleAssignment3Proxyid.update(id_9idTemp,choice_10idTemp,value_11idTemp);
if(update43mtemp == null){
%>
<%=update43mtemp %>
<%
}else{
        String tempResultreturnp44 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(update43mtemp));
        %>
        <%= tempResultreturnp44 %>
        <%
}
break;
}
} catch (Exception e) { 
%>
Exception: <%= org.eclipse.jst.ws.util.JspUtils.markup(e.toString()) %>
Message: <%= org.eclipse.jst.ws.util.JspUtils.markup(e.getMessage()) %>
<%
return;
}
if(!gotMethod){
%>
result: N/A
<%
}
%>
</BODY>
</HTML>