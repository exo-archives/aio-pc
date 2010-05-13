    EXO-PortletContainer 2.0 
    --------------------------------

The eXo Platform team is pleased to announce eXo PC 2.0 which includes the JSR 286 specification as wellas the WSRP 2 one.

(1) This release includes the WSRP 2.0 public review draft 03 specification implementation
(2) This release implements PortletAPI 2.0 and it was successfully tested by first available Test Compatibility Kit (TCK). About 95% of TCK tests passed.


Portlet Container major improvements and bugfixes:
- major changes in portlet application deployment descriptor
- major refactorings in API interfaces
- container runtime options support
- resource ID support
- more proper QName/namespace processing
- annotated methods support
- fixed event processing (default namespace, etc.)
- fully refactored portal framework: easy and clear to use
- PortletURLGenerationListener implementation
- JAXB type validation for event payload
- new PortletConfig methods
- cache level for resource URLs
- predefined container events
- portlet/servlet request/response relations changes
- full portlet filter support
- validation cache and ETAG support
- new namespace, param, property tags in tag library
- portlet/servlet session relation changes
- include/forward processing changes
- more flexible public render parameters processing
- many other small fixes and improvements


WSRP version 2.0 PR03 implementation includes
- resource serving
- event handling
- navigational parameters
- some improvements and bugfixes in WSRP starter mechanism
- added plugin mechanism
- three step protocol
- JAXB event binding
- state distribution
- new markup operations: getResource and handleEvents
- added portlets2events as separate project
- added setEventDescriptions for getServiceDescription on producer 


--------------------------------
eXo Portlet Container: version 2.0 beta 3 release notes:

* full compliance with PortletAPI 2.0 public draft (rev.19) specification
* bigfixes in the PC plugin mechanism
* WSRP version 1.0 plugin
* some other internal improvements and bugfixes

--------------------------------
eXo Portlet Container: version 2.0 beta 2 release notes:

This product comes with many functionalities:

* plugin architecture
* clustering support
* jsr 286 implementation with:
- resource serving
- fragment serving -- AJAX support
- shared render parameters
- portlet events (inter portlet communication)
- portlet filters
- test portal fully refactored
- portal maker's framework

--------------------------------

EXO-PC Tomcat 5.5 bundle includes Apache Tomcat 5.5.17 distributive
bundled with eXo PC sample applications and neccessary libraries.

EXO-PC includes:
  Test portal - portal application which provide portlets functionalities (portal.war)
  Test portlets - portlets, which showing ability of the jsr168 portlet specification (portlets.war)
  Test portlets 2 - portlets, which showing ability of the jsr286 portlet specification (portlets2.war)

1. Start Up
   Tomcat 5.5 with EXO-PC 2.0 bundled can be started by executing the following commands:

      $CATALINA_HOME\bin\exo-run.bat          (Windows)

      $CATALINA_HOME/bin/exo-run.sh           (Unix)

2. After startup, EXO-PC 2.0 sample applications will be available by visiting:

    http://localhost:8080/portal

3. More information in wiki at http://wiki.exoplatform.org/ or send an email to "exo-portlet-container@objectweb.org".

