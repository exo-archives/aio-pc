<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>
<portlet:defineObjects/>


<style type="text/css">
	#resource_inactive {
		color:#999999;
		font-style:italic;
		text-indent:0.3em;
		font-size:12px;
	}  
</style>


<script LANGUAGE=JavaScript>

var resource_inactive;
resource_inactive = "<p id='resource_inactive'>Resource inactive<p>";

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
  but.value = "Clear";
  but.onclick = clearContent;
}

function clearContent() {
  var div = document.getElementById("portletcontent");
  //div.innerHTML = "none";
  div.innerHTML = resource_inactive;
  var but = document.getElementById("mainbut");
  but.value = "Get markup";
  but.onclick = asynchGetXMLHttpRequest;
}
</script>

<b>Press the button below to get a piece of markup via AJAX</b>
<br><br>
<input type="button" id="mainbut" value="Get markup" onclick="asynchGetXMLHttpRequest()">
<input type="hidden" id="resourceURL" value="<portlet:resourceURL />">
<br>
<div id="portletcontent"><p id='resource_inactive'>Resource inactive<p></div>
