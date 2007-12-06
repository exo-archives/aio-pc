<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>
<portlet:defineObjects/>

<script LANGUAGE=JavaScript>
var portletReq;

function getHttpRequest() {
  var req = null;
  try {                           
    // Firefox, Opera 8.0+, Safari
    req = new XMLHttpRequest();   
  } catch (e) {                   
    // Internet Explorer          
    try {
      req = new ActiveXObject("Msxml2.XMLHTTP");
    } catch (e) {
      try {
        req = new ActiveXObject("Microsoft.XMLHTTP");
      } catch (e) {
        alert("Your browser does not support AJAX!");
        return false;
      }
    }
  }
  return req;
}

function asynchGetXMLHttpRequest() {
  var requestURL = document.getElementById("resourceURL").value;
  portletReq = getHttpRequest();
  if (portletReq === false) {
    alert("Exception: I could not find the XMLHttpRequest");
  }
  portletReq.onreadystatechange = processReqChange;
  portletReq.open('GET', requestURL, true);
  portletReq.send(null);
}

function processReqChange() {
  if (portletReq.readyState == 4) {
    if (portletReq.status == 200) {
      displayInvoice();
    }
  }
}

function displayInvoice() {
  var div = document.getElementById("portletcontent");
  div.innerHTML = "";
  div.innerHTML = portletReq.responseText;
  var but = document.getElementById("mainbut");
  but.value = "clear";
  but.onclick = clearContent;
}

function clearContent() {
  var div = document.getElementById("portletcontent");
  div.innerHTML = "";
  var but = document.getElementById("mainbut");
  but.value = "Serve resource";
  but.onclick = asynchGetXMLHttpRequest;
}
</script>

<b>Send event "MyEventPub2" to "portlets2/TestPortlet"</b>
<br><br>
<input type="button" id="mainbut" value="Serve resource" onclick="asynchGetXMLHttpRequest()">
<input type="hidden" id="resourceURL" value="<portlet:resourceURL />">
<br>
<div id="portletcontent"></div>
