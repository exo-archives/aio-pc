package com.sun.ts.tests.portlet.Signature;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.PortletSecurityException;
import javax.portlet.PortletSessionUtil;
import javax.portlet.PortletURL;
import javax.portlet.ReadOnlyException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceURL;
import javax.portlet.UnavailableException;
import javax.portlet.ValidatorException;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.ts.tests.portlet.common.util.ResultWriter;
import com.sun.ts.tests.portlet.common.util.requestcount.RequestCount;
import com.sun.ts.tests.portlet.common.util.tags.PortletURLTag;

public class SignatureTestPortlet extends GenericPortlet {

	public static String TEST_NAME="SignatureTest";
	
	public static String failString ="";
	
	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	protected void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
		RequestCount reqCount = new RequestCount(request, response,
                RequestCount.MANAGED_VIA_SESSION);
		
		if (reqCount.isFirstRequest()) {
			String result = renderAPICheck(request,response);
			if (result == null){
				PortletURLTag customTag = new PortletURLTag();
				PortletURL portletURL = response.createActionURL();
		        customTag.setTagContent(portletURL.toString());
		        out.println(customTag.toString());
		        resultWriter.setStatus(ResultWriter.PASS);
		        out.println(resultWriter.toString());
			}
			else{
				resultWriter.setStatus(ResultWriter.FAIL);
				resultWriter.addDetail("Signature for Class: "+ result+ " fail.");
				resultWriter.addDetail(failString);
		        out.println(resultWriter.toString());
			}
		}
		else if (reqCount.getRequestNumber() == 1){
			//after Action Request
			String result = (String)request.getPortletSession().getAttribute(TEST_NAME);
			if (result != null){
				if (result.equals("PASS")){
					PortletURLTag customTag = new PortletURLTag();
					ResourceURL portletURL = response.createResourceURL();
			        customTag.setTagContent(portletURL.toString());
			        out.println(customTag.toString());
			        resultWriter.setStatus(ResultWriter.PASS);
			        out.println(resultWriter.toString());
				}
				else{
					resultWriter.setStatus(ResultWriter.FAIL);
					resultWriter.addDetail("Signature for Class: "+ result+ " fail.");
			        out.println(resultWriter.toString());
				}
			}
			else{
				resultWriter.setStatus(ResultWriter.FAIL);
				resultWriter.addDetail("PortletSessionAttribute not found.");
				out.println(resultWriter.toString());
			}
		}
	}

	@Override
	public void init() throws PortletException {
	}

	@Override
	public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {
		String result = actionAPICheck(request,response); 
		if (result== null){
			request.getPortletSession().setAttribute(TEST_NAME, "PASS");
		}
		else{
			request.getPortletSession().setAttribute(TEST_NAME, result);
		}
	}

	@Override
	public void processEvent(EventRequest request, EventResponse response) throws PortletException, IOException {
	}

	@Override
	public void serveResource(ResourceRequest request, ResourceResponse response) throws PortletException, IOException {
		ResultWriter resultWriter = new ResultWriter(TEST_NAME);
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		String result = serveResourceAPICheck(request,response);
		if (result == null){
			resultWriter.setStatus(ResultWriter.PASS);
	        out.println(resultWriter.toString());
		}
		else{
			resultWriter.setStatus(ResultWriter.FAIL);
			resultWriter.addDetail("Signature for Class: "+ result+ "fail.");
	        out.println(resultWriter.toString());
		}
	}
	//---------------------------------------------------------------------------------------------
	private ResultClass getClassConfiguration(String name, Class clazz){
		ResultClass resultClass = new ResultClass();
		//////////////////////////
		//get the Class Interfaces
		//////////////////////////
		int length = 0;
		Class tmpclass = clazz;
		while(tmpclass != null){
			length = tmpclass.getInterfaces().length;
			if (length != 0){
				ArrayList<String> result = resultClass.getInterfaceNames();
				for (int i = 0; i < length; i++){
					result.add(tmpclass.getInterfaces()[i].getName());
				}
				resultClass.setInterfaceNames(result);
			}
			tmpclass = tmpclass.getSuperclass();
		}
		
		//////////////////////////
		//get the Method Signature
		//////////////////////////
		ArrayList<Method> resultM = new ArrayList<Method>();
		tmpclass = clazz;
		while(tmpclass != null){
			if (tmpclass.getDeclaredConstructors()!= null){
				length = tmpclass.getDeclaredConstructors().length;
				for (int i = 0; i < length; i++){
						String tmp = tmpclass.getDeclaredConstructors()[i].toString();
						String rem = tmpclass.getPackage().getName();
						tmp = tmp.replace('$', '.').replaceFirst(rem.replace('$', '.') + ".", "");
						tmp = tmp.replace(" final", "");
						if (!tmp.contains("throws")){
							resultM.add(new Method(tmp));
						}
						else
						{
							ArrayList<Class> exceptionList = new ArrayList<Class>();
							int exceptionSize = clazz.getDeclaredConstructors()[i].getExceptionTypes().length;
							for (int e = 0; e < exceptionSize; e++){
								exceptionList.add(clazz.getDeclaredConstructors()[i].getExceptionTypes()[e]);
							}
							resultM.add(new Method(tmp.substring(0, tmp.indexOf(" throws")),exceptionList));
						}
				}
			}
			tmpclass = tmpclass.getSuperclass();
		}

		length = clazz.getMethods().length;
		if (length != 0){
			for (int i = 0; i < length; i++){	
				String tmp = clazz.getMethods()[i].toString();
				String rem = clazz.getMethods()[i].getDeclaringClass().getName();
				tmp = tmp.replace('$', '.').replaceFirst(rem.replace('$', '.') + ".", "");
				tmp = tmp.replace(" final", "");
				if (!tmp.contains("throws")){
					resultM.add(new Method(tmp));
				}
				else
				{
					ArrayList<Class> exceptionList = new ArrayList<Class>();
					int exceptionSize = clazz.getMethods()[i].getExceptionTypes().length;
					for (int e = 0; e < exceptionSize; e++){
						exceptionList.add(clazz.getMethods()[i].getExceptionTypes()[e]);
					}
					resultM.add(new Method(tmp.substring(0, tmp.indexOf(" throws")),exceptionList));
				}
			}
		}
		length = clazz.getDeclaredMethods().length;
		if (length != 0){
			for (int i = 0; i < length; i++){	
					String tmp = clazz.getDeclaredMethods()[i].toString();
					String rem = clazz.getDeclaredMethods()[i].getDeclaringClass().getName();
					tmp = tmp.replace('$', '.').replaceFirst(rem.replace('$', '.') + ".", "");
					tmp = tmp.replace(" final", "");
					if (!tmp.contains("throws")){
						resultM.add(new Method(tmp));
					}
					else
					{
						ArrayList<Class> exceptionList = new ArrayList<Class>();
						int exceptionSize = clazz.getDeclaredMethods()[i].getExceptionTypes().length;
						for (int e = 0; e < exceptionSize; e++){
							exceptionList.add(clazz.getDeclaredMethods()[i].getExceptionTypes()[e]);
						}
						resultM.add(new Method(tmp.substring(0, tmp.indexOf(" throws")),exceptionList));
					}
			}
		}
		resultClass.setMethods(resultM);
		//////////////////////////
		//get the fields
		//////////////////////////
		ArrayList<String> resultF = new ArrayList<String>();
		if (!clazz.getName().equals(name)){
			for (int i = 0; i < clazz.getInterfaces().length;i++){
				length =clazz.getInterfaces()[i].getDeclaredFields().length;
				if (length != 0){
					for (int j = 0; j < length; j++){
						if (clazz.getInterfaces()[i].getDeclaredFields()[j].toString().contains(clazz.getInterfaces()[i].getName())){
							resultF.add(clazz.getInterfaces()[i].getDeclaredFields()[j].toString().replaceFirst(name+".", ""));
						}
					}
				}
			}
		}
		length = clazz.getDeclaredFields().length;
		if (length != 0){
			
			for (int i = 0; i < length; i++){
				if (clazz.getDeclaredFields()[i].toString().contains(clazz.getName())){
					resultF.add(clazz.getDeclaredFields()[i].toString().replaceFirst(name+".", ""));
				}
			}
			
		}
		
		length = clazz.getFields().length;
		if (length != 0){
			for (int i = 0; i < length; i++){
				resultF.add(clazz.getFields()[i].toString().replaceFirst(clazz.getFields()[i].getDeclaringClass().getName()+".", ""));
			}
		}
		resultClass.setFields(resultF);
		
		return resultClass;
	}
	
	private class Method{
		String head;
		ArrayList<Class> exception = new ArrayList<Class>();
		public Method(String head){
			this.head = head;
		}
		public Method(String head, ArrayList<Class> exception){
			this.head = head;
			this.exception = exception;
		}
		public ArrayList<Class> getException() {
			return exception;
		}
		public void setException(ArrayList<Class> exception) {
			this.exception = exception;
		}
		public String getHead() {
			return head;
		}
		public void setHead(String head) {
			this.head = head;
		}
		
	}
	
	private class ResultClass{
		
		ArrayList<String> interfaceNames = new ArrayList<String>();
		ArrayList<Method> methods = new ArrayList<Method>();
		ArrayList<String> fields = new ArrayList<String>();
		
		public void setFields(ArrayList<String> fields) {
			this.fields = fields;
		}
		public void setInterfaceNames(ArrayList<String> interfaceNames) {
			this.interfaceNames = interfaceNames;
		}
		public void setMethods(ArrayList<Method> methods) {
			this.methods = methods;
		}
		public ArrayList<String> getFields() {
			return fields;
		}
		public ArrayList<String> getInterfaceNames() {
			return interfaceNames;
		}
		public ArrayList<Method> getMethods() {
			return methods;
		}
	}
	private boolean checkAPI(String name,ResultClass resultClass){
		try {
			
			Document doc = DocumentBuilderFactory
				.newInstance()
				.newDocumentBuilder()
				.parse((new File(this.getPortletContext().getRealPath("/WEB-INF/signature-repository/signature_2_0.xml"))));
			Element e = doc.getDocumentElement();
			NodeList nodelist = e.getChildNodes();
			int length =nodelist.getLength();
			for (int i = 0; i < length; i++){
				Node tmp = nodelist.item(i);
				//here you have the class Nodes
				if (tmp.getNodeName().equals("class")){
					String type;
					for (int j = 0; j < tmp.getChildNodes().getLength(); j++){
						if (tmp.getChildNodes().item(j).getNodeName().equals("Name")){
							if (!tmp.getChildNodes().item(j).getTextContent().equals(name))
								j = tmp.getChildNodes().getLength() + 1;
						}
						else if (tmp.getChildNodes().item(j).getNodeName().equals("type")){
							type =  tmp.getChildNodes().item(j).getTextContent();
						}
						else if (tmp.getChildNodes().item(j).getNodeName().equals("implements")){
							if (!resultClass.getInterfaceNames().contains(tmp.getChildNodes().item(j).getTextContent())){
								failString = "expected: " + tmp.getChildNodes().item(j).getTextContent() + " result: " + resultClass.getInterfaceNames().toString()+ " didn't contains expected result.";
								return false;
							}
						}
						else if (tmp.getChildNodes().item(j).getNodeName().equals("Field")){
							if (!resultClass.getFields().contains(tmp.getChildNodes().item(j).getTextContent())){
								failString = "expected: " + tmp.getChildNodes().item(j).getTextContent() + " result: " + resultClass.getFields().toString()+ " didn't contains expected result.";
								return false;
							}
						}
						else if (tmp.getChildNodes().item(j).getNodeName().equals("Method")){
							
							String head = "";
							ArrayList<String> exceptions = new ArrayList<String>();
							
							for (int m = 0;m <tmp.getChildNodes().item(j).getChildNodes().getLength(); m++){
								if (tmp.getChildNodes().item(j).getChildNodes().item(m).getNodeName().equals("Head")){
									head=tmp.getChildNodes().item(j).getChildNodes().item(m).getTextContent();
								}
								else if (tmp.getChildNodes().item(j).getChildNodes().item(m).getNodeName().equals("Throws")){
									exceptions.add(tmp.getChildNodes().item(j).getChildNodes().item(m).getTextContent());
								}
							}
							if (!methodTest(head,exceptions,resultClass))
								return false;
						}
					}
				}
			}
		} catch (SAXException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	private boolean methodTest(String head,ArrayList<String> exceptions,ResultClass resultClass){
		//compare expected methods with result
		ArrayList<Method> resultMethodList = resultClass.getMethods();
		int resultMethodSize = resultMethodList.size();
		boolean foundMethod = false;
		for (int k = 0; k < resultMethodSize; k++){
			if (resultMethodList.get(k).getHead().equals(head)){
				if (resultMethodList.get(k).getException().size()>0){
					if (exceptionTest(resultMethodList.get(k).getException(),exceptions)){
						foundMethod = true;
						k = resultMethodSize;
					}
				}
				else
					foundMethod = true;
			}
		}
		if (!foundMethod){
			String methodResultString = "";
			ArrayList<Method> method = resultClass.getMethods();
			for (Method m : method) {
				methodResultString += "\n" + m.getHead();
			}
			failString = "expected: " + head + exceptions.toString() +" result: " + resultClass.getMethods().toString()+ " didn't contains expected result.";
			return false;
		}
		return true;
	}
	private boolean exceptionTest(ArrayList<Class> resultExceptions,ArrayList<String> expectedExceptions){
		//tests the exceptionList
		boolean foundExc = true;
		for (int i = 0; i < resultExceptions.size(); i++){
			boolean foundTmpException = false;
			if (expectedExceptions.contains(resultExceptions.get(i).getName())){
				foundTmpException = true;
			}
			else{
				//result must be subclass
				Class tmpclazz = resultExceptions.get(i).getSuperclass();
				while(tmpclazz != null){
					if (expectedExceptions.contains(tmpclazz.getName())){
						foundTmpException=true;
						tmpclazz = null;
					}
					else{
						tmpclazz = tmpclazz.getSuperclass();
					}
				}
				// or java.lang.Runtime
				if (!foundTmpException){
					tmpclazz = resultExceptions.get(i);
					while(tmpclazz != null){
						if (tmpclazz.getName().equals("java.lang.RuntimeException")){
							foundTmpException=true;
							tmpclazz = null;
						}
						else{
							tmpclazz = tmpclazz.getSuperclass();
						}
					}
				}
			}
			if (!foundTmpException)
				return false;
		}
		return foundExc;
	}
	private String renderAPICheck(RenderRequest request, RenderResponse response){
		//BaseURL
		if (!checkAPI("BaseURL",getClassConfiguration("javax.portlet.BaseURL",response.createActionURL().getClass()))){
			return "BaseURL";
		}
		//CacheControl
		if (!checkAPI("CacheControl", getClassConfiguration("javax.portlet.CacheControl",response.getCacheControl().getClass()))){
			return "CacheControl";
		}
		//EventPortlet
		Class clazz = null;
		for (int i = 0; i < this.getClass().getSuperclass().getInterfaces().length;i++){
			if (this.getClass().getSuperclass().getInterfaces()[i].getName().equals("javax.portlet.EventPortlet")){
				clazz = this.getClass().getSuperclass().getInterfaces()[i];
			}
		}
		if (!checkAPI("EventPortlet", getClassConfiguration("javax.portlet.EventPortlet", clazz))){
			return "EventPortlet";
		}
		//GenericPortlet
		if (!checkAPI("GenericPortlet", getClassConfiguration(this.getClass().getSuperclass().getName(), this.getClass().getSuperclass()))){
			return "GenericPortlet";
		}
		//MimeResponse
		if (!checkAPI("MimeResponse",getClassConfiguration("javax.portlet.MimeResponse", response.getClass()))){
			return "MimeResponse";
		}
		//PortalContext
		if (!checkAPI("PortalContext",getClassConfiguration("javax.portlet.PortalContext", request.getPortalContext().getClass()))){
			return "PortalContext";
		}
		//Portlet
		clazz = null;
		for (int i = 0; i < this.getClass().getSuperclass().getInterfaces().length;i++){
			if (this.getClass().getSuperclass().getInterfaces()[i].getName().equals("javax.portlet.Portlet")){
				clazz = this.getClass().getSuperclass().getInterfaces()[i];
			}
		}
		if (!checkAPI("Portlet",getClassConfiguration("javax.portlet.Portlet", clazz))){
			return "Portlet";
		}
		//PortletConfig
		if (!checkAPI("PortletConfig",getClassConfiguration("javax.portlet.PortletConfig", getPortletConfig().getClass()))){
			return "PortletConfig";
		}
		//PortletContext
		if (!checkAPI("PortletContext",getClassConfiguration("javax.portlet.PortletContext", getPortletContext().getClass()))){
			return "PortletContext";
		}
		//PortletException
		if (!checkAPI("PortletException",getClassConfiguration("javax.portlet.PortletException", (new PortletException()).getClass()))){
			return "PortletException";
		}
		//PortletMode
		if (!checkAPI("PortletMode",getClassConfiguration("javax.portlet.PortletMode", (new PortletMode(PortletMode.VIEW.toString())).getClass()))){
			return "PortletMode";
		}
		//PortletModeException
		if (!checkAPI("PortletModeException",getClassConfiguration("javax.portlet.PortletModeException", (new PortletModeException("",PortletMode.VIEW)).getClass()))){
			return "PortletModeException";
		}
		//PortletPreferences
		if (!checkAPI("PortletPreferences",getClassConfiguration("javax.portlet.PortletPreferences", request.getPreferences().getClass()))){
			return "PortletPreferences";
		}
		//PortletRequest
		if (!checkAPI("PortletRequest",getClassConfiguration("javax.portlet.PortletRequest", request.getClass()))){
			return "PortletRequest";
		}
		//PortletRequestDispatcher
		if (!checkAPI("PortletRequestDispatcher",getClassConfiguration("javax.portlet.PortletRequestDispatcher", getPortletContext().getRequestDispatcher("/test").getClass()))){
			return "PortletRequestDispatcher";
		}
		//PortletResponse
		if (!checkAPI("PortletResponse",getClassConfiguration("javax.portlet.PortletResponse", response.getClass()))){
			return "PortletResponse";
		}
		//PortletSecurityException
		if (!checkAPI("PortletSecurityException",getClassConfiguration("javax.portlet.PortletSecurityException", (new PortletSecurityException("")).getClass()))){
			return "PortletSecurityException";
		}
		//PortletSession
		if (!checkAPI("PortletSession",getClassConfiguration("javax.portlet.PortletSession", request.getPortletSession().getClass()))){
			return "PortletSession";
		}
		//PortletSessionUtil
		if (!checkAPI("PortletSessionUtil",getClassConfiguration("javax.portlet.PortletSessionUtil", (new PortletSessionUtil()).getClass()))){
			return "PortletSessionUtil";
		}
		//PortletURL
		if (!checkAPI("PortletURL",getClassConfiguration("javax.portlet.PortletURL", response.createRenderURL().getClass()))){
			return "PortletURL";
		}
		//ReadOnlyException
		if (!checkAPI("ReadOnlyException",getClassConfiguration("javax.portlet.ReadOnlyException", (new ReadOnlyException("").getClass())))){
			return "ReadOnlyException";
		}
		//RenderRequest
		if (!checkAPI("RenderRequest",getClassConfiguration("javax.portlet.RenderRequest", request.getClass()))){
			return "RenderRequest";
		}
		//RenderResponse
		if (!checkAPI("RenderResponse",getClassConfiguration("javax.portlet.RenderResponse", response.getClass()))){
			return "RenderResponse";
		}
		//ResourceServingPortlet
		clazz = null;
		for (int i = 0; i < this.getClass().getSuperclass().getInterfaces().length;i++){
			if (this.getClass().getSuperclass().getInterfaces()[i].getName().equals("javax.portlet.ResourceServingPortlet")){
				clazz = this.getClass().getSuperclass().getInterfaces()[i];
			}
		}
		if (!checkAPI("ResourceServingPortlet",getClassConfiguration("javax.portlet.ResourceServingPortlet", clazz))){
			return "ResourceServingPortlet";
		}
		//ResourceURL
		if (!checkAPI("ResourceURL",getClassConfiguration("javax.portlet.ResourceURL", response.createResourceURL().getClass()))){
			return "ResourceURL";
		}
		//UnavailableException
		if (!checkAPI("UnavailableException",getClassConfiguration("javax.portlet.UnavailableException", (new UnavailableException("")).getClass()))){
			return "UnavailableException";
		}
		//ValidatorException
		if (!checkAPI("ValidatorException",getClassConfiguration("javax.portlet.ValidatorException", (new ValidatorException("", new LinkedList<String>())).getClass()))){
			return "ValidatorException";
		}
		//WindowState
		if (!checkAPI("WindowState",getClassConfiguration("javax.portlet.WindowState", (new WindowState(WindowState.NORMAL.toString())).getClass()))){
			return "WindowState";
		}
		//WindowStateException
		if (!checkAPI("WindowStateException",getClassConfiguration("javax.portlet.WindowStateException", (new WindowStateException("",WindowState.NORMAL)).getClass()))){
			return "WindowStateException";
		}
		//RenderFilter
		if (!checkAPI("RenderFilter",getClassConfiguration("javax.portlet.filter.RenderFilter", (Class)request.getPortletSession().getAttribute("RenderFilter")))){
			return "RenderFilter";
		}
		//ActionFilter
		if (!checkAPI("ActionFilter",getClassConfiguration("javax.portlet.filter.ActionFilter", (Class)request.getPortletSession().getAttribute("ActionFilter")))){
			return "ActionFilter";
		}
		//ResourceFilter
		if (!checkAPI("ResourceFilter",getClassConfiguration("javax.portlet.filter.ResourceFilter", (Class)request.getPortletSession().getAttribute("ResourceFilter")))){
			return "ResourceFilter";
		}
		//EventFilter
		if (!checkAPI("EventFilter",getClassConfiguration("javax.portlet.filter.EventFilter", (Class)request.getPortletSession().getAttribute("EventFilter")))){
			return "EventFilter";
		}
		//PortletFilter
		if (!checkAPI("PortletFilter",getClassConfiguration("javax.portlet.filter.PortletFilter", (Class)request.getPortletSession().getAttribute("PortletFilter")))){
			return "PortletFilter";
		}
		//FilterChain
		if (!checkAPI("FilterChain",getClassConfiguration("javax.portlet.filter.FilterChain", (Class)request.getPortletSession().getAttribute("FilterChain")))){
			return "FilterChain";
		}
		
		return null;
	}
	private String actionAPICheck(ActionRequest request, ActionResponse response){
		//ActionRequest
		if (!checkAPI("ActionRequest",getClassConfiguration("javax.portlet.ActionRequest", request.getClass()))){
			return "ActionRequest";
		}
		//ActionResponse
		if (!checkAPI("ActionResponse",getClassConfiguration("javax.portlet.ActionResponse", response.getClass()))){
			return "ActionResponse";
		}
		//ClientDataRequest
		if (!checkAPI("ClientDataRequest",getClassConfiguration("javax.portlet.ClientDataRequest", request.getClass()))){
			return "ClientDataRequest";
		}
		//StateAwareResponse
		if (!checkAPI("StateAwareResponse",getClassConfiguration("javax.portlet.StateAwareResponse", response.getClass()))){
			return "StateAwareResponse";
		}
		return null;
	}
	private String serveResourceAPICheck(ResourceRequest request,ResourceResponse response){
		//ResourceRequest
		if (!checkAPI("ResourceRequest",getClassConfiguration("javax.portlet.ResourceRequest", request.getClass()))){
			return "ResourceRequest";
		}
		//ResourceResponse
		if (!checkAPI("ResourceResponse",getClassConfiguration("javax.portlet.ResourceResponse", response.getClass()))){
			return "ResourceResponse";
		}
		return null;
	}
}
