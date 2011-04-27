Summary

    * Status: WSRP, problem in consuming web services
    * CCP Issue: CCP-740, Product Jira Issue: PC-525.
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?

    * Reproduced the problem using AIO-1.6.7:
      1- Go to Groups-> Application Regestry
      2- Auto Import porltets
      3- Add a new page and insert on it WSRP1 or WSRP2 app
      4- In the view click on save in order to deploy services.
      Trying to invoque WSDL(for example http://localhost:8080/wsrp/services/WSRPRegistrationService?wsdl) of those services we get axis fault

      Fault - Bean attribute lang is of type java.lang.String, which is not a simple type

      AxisFault
       faultCode: {http://schemas.xmlsoap.org/soap/envelope/}Server.generalException
       faultSubcode:
       faultString: Bean attribute lang is of type java.lang.String, which is not a simple type
       faultActor:
       faultNode:
       faultDetail:
      	{http://xml.apache.org/axis/}hostname:eXo

      such problem has been considered as axis one refering to some links like this one:http://old.nabble.com/Wsrp4j-not-working-on-Weblogic-9.2.1-due-Axis-Bug-td12896082.html. A problem of serialization is signaled on it
      5- Turn back to the Application Regestry
      6- Auto import again
      7- the webservices' s consummer are added
      8- Add a page on which you insert one of them. An exception related to the first problem is rised:

      [ERROR] consumer - Remote exception  <fault : InvalidSession>AxisFault
       faultCode: {urn:oasis:names:tc:wsrp:v1:types}InvalidSession
       faultSubcode:
       faultString: fault : InvalidSession
       faultActor:
       faultNode:
       faultDetail:
              {urn:oasis:names:tc:wsrp:v1:types}InvalidSession:null

      fault : InvalidSession

[ERROR] wsrp1 - WS Fault occured <org.exoplatform.services.wsrp.exceptions.WSRPException: fault : OperationFailed>org.exoplatform.services.wsrp.exceptions.WSRPExceptio
n: fault : OperationFailed
        at org.exoplatform.services.wsrp.consumer.impl.PortletDriverImpl.getMarkup(PortletDriverImpl.java:320)
        at org.exoplatform.services.wsrp.consumer.impl.WSRPConsumerPlugin.render(WSRPConsumerPlugin.java:767)
        at org.exoplatform.services.portletcontainer.impl.PortletContainerServiceImpl.render(PortletContainerServiceImpl.java:576)
        at org.exoplatform.portal.webui.application.UIPortletLifecycle.processRender(UIPortletLifecycle.java:169)
        at org.exoplatform.webui.core.UIComponent.processRender(UIComponent.java:100)
        at org.exoplatform.webui.core.UIContainer.renderChildren(UIContainer.java:243)
        at org.exoplatform.webui.core.UIContainer.renderChildren(UIContainer.java:237)

Fix description

How is the problem fixed?
- applications/assembly/patches/tomcat/bin/exo-run.sh: add AXIS_WS 
- applications/wsrp/src/main/webapp/WEB-INF/web.xml: add parameters for the WSRPStarter serlvet declaration
- Add new portlet SecurityPortlet
- applications/test-portal/src/main/webapp/WEB-INF/web.xml: add security-role tag
- component/plugins/pc/src/main/java/org/exoplatform/services/portletcontainer/impl/servlet/PortletApplicationListener.java
  Update to read the "security-role" tag from the web.xml
- component/plugins/pc/src/main/java/org/exoplatform/services/portletcontainer/plugins/pc/portletAPIImp/tags/XURLTag.java
  Add encode Chars in URL
- WSRP1: 
  + component/plugins/wsrp1/src/test/java/org/exoplatform/services/wsrp/testConsumer/BaseTest.java
    Add method applySecurityParams
  + component/plugins/wsrp1/src/main/java/org/exoplatform/services/wsrp/producer/impl/WSRPStarter.java
    Add ability to read and use in start() "credentials" params
  + component/plugins/wsrp1/src/main/java/org/exoplatform/services/wsrp/producer/impl/helpers/ProducerRewriterPortletURLImp.java
  Add toString() method
  + component/plugins/wsrp1/src/main/java/org/exoplatform/services/wsrp/producer/impl/TransientStateManagerImpl.java
    In resolveSession() method, check whether sessionID is null.
  + component/plugins/wsrp1/src/main/java/org/exoplatform/services/wsrp/producer/impl/MarkupOperationsInterfaceImpl.java
    In processNavigationalState() method, check whether navigationalState is null.
  + component/plugins/wsrp1/src/main/java/org/exoplatform/services/wsrp/AdminClient.java
    Modify in processOpts() method
  + component/plugins/wsrp1/src/main/java/org/exoplatform/services/wsrp/security/PasswordHandler.java
    Add new class for password handler
  + component/plugins/wsrp1/src/main/java/org/exoplatform/services/wsrp/consumer/impl/ProducerImpl.java
    Add private method applySecurityParams()
  + component/plugins/wsrp1/src/main/java/org/exoplatform/services/wsrp/consumer/impl/WSRPConsumerPlugin.java
    Set userID for the userSession.
  + component/plugins/wsrp1/src/main/java/org/exoplatform/services/wsrp/consumer/impl/PortletDriverImpl.java
    Add for the methods (getMarkup, performBlockingInteraction, ...)
    Add private method applySecurityParams()
  + component/plugins/wsrp1/pom.xml
    Add usage deploy.wsdd from the sources
    Add dependency of org.apache.ws.security:wss4j-1.5.10
- WSRP2: besides same modifications as for WSRP1, 
  + Update component/plugins/wsrp2/src/test/java/org/exoplatform/services/wsrp2/test/BaseTest.java
  + component/plugins/wsrp2/src/main/java/org/exoplatform/services/wsrp2/producer/impl/WSRPStarter.java
    Add ability to read and use in start() "credentials" params
  + component/plugins/wsrp2/src/main/java/org/exoplatform/services/wsrp2/consumer/impl/WSRP2ProducerData.java
    Fix db table name
  + component/core/src/main/java/org/exoplatform/services/portletcontainer/helper/WindowInfosContainer.java
    Fix owner

Patch file: PC-525.patch

Tests to perform

Reproduction test
* SecurityPortlet

Tests performed at DevLevel
* JUnit, SecurityPortlet

Tests performed at QA/Support Level
*
Documentation changes

Documentation changes:
* no
Configuration changes

Configuration changes:
* Add web.xml parameter

    <init-param>
      <param-name>credentials</param-name>
      <param-value>root/exo</param-value>
    </init-param>

for the WSRPStarter</servlet-class>

Add the axis security system property

AXIS_WS="-Daxis.ClientConfigFile=../conf/client_deploy.wsdd"

Add the axis security file
/conf/client_deploy.wsdd

Can be downloaded at client_deploy.wsdd

Will previous configuration continue to work?
* no

Risks and impacts

Can this bug fix have any side effects on current client projects?

    * add several security helps method.

Is there a performance risk/cost?
* no

Validation (PM/Support/QA)

PM Comment
* 

Support Comment
* Validated

QA Feedbacks
*

