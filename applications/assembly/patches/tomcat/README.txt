    EXO-PortletContainer 2.1
=================================

The eXo Platform Portlet Container 2.1 which includes the JSR 168/286 implementation and WSRP v1 and v2.

(1) This release includes the WSRP 2.0 specification implementation
(2) This release implements PortletAPI 2.0 and it was successfully tested by Test Compatibility Kit (TCK).


It depends on tool trunk, kernel 2.1, core 2.2, ws 2.0 and jcr 1.11.

Changes:

1. Starting from version 2.1 WSRP service based ws.soap annotated engine (CXF based).
2. Implemented WSRP persistence based on the JCR implementation.
3. Single project for both WSRP v1 and v2 services.
4. Created administration interface for WSRP service.
5. Administration portlet improvement on consumer.


Changes log:

--------------------------------

Bug

   * [PC-326] - The class org.exoplatform.services.wsrp.filter.AxisFilter doesn't release properly the resources stored into the ThreadLocal
   * [PC-327] - The class org.exoplatform.services.wsrp2.filter.AxisFilter doesn't release properly the resources stored into the ThreadLocal
   * [PC-346] - Appropriate WSDL url for multiport service.
   * [PC-366] - UnknownHostException: my.service
   * [PC-368] - Cannot create one more consumer for the same producer address.

Improvement

   * [PC-338] - Implement WSRP persistence based on the JCR implementation

Task

   * [PC-319] - WSRP service on ws.soap annotated engine (CXF based).
   * [PC-328] - Use the Text object to provide lazy serialization or avoid unnecessary serialization
   * [PC-329] - Remove contention in portlet container metadata retrieval
   * [PC-330] - Cache exo logger retrieval (to increase perf)
   * [PC-344] - Failed to download apache-tomcat-5.5.25
   * [PC-347] - Administration interface for WSRP service
   * [PC-351] - Join both WSRP 1 and 2 code to single project
   * [PC-352] - Single url for WSRP1 and WSRP2.
   * [PC-353] - Producer tests: more tests for each interface.
   * [PC-355] - Administration portlet on consumer improvement
   * [PC-372] - URLs modification: to use of a certain class for specific version of the service.
   * [PC-376] - To fix WSRP1 tests for new source generation.
   * [PC-387] - Commit to the branches-2.1 all missing PC projects.
   * [PC-391] - Update cargo dependencies to use 6x tomcat

Sub-task

   * [PC-343] - ComparisonFailure TestCachingMechanism.testExistenceOfGlobal
   * [PC-363] - WSRPV2ServiceDescriptionPortTypeImpl - null <java.lang.NullPointerException>
   * [PC-364] - DuplicateComponentKeyRegistrationException: Key producer21417245355 duplicated
   * [PC-373] - Create documentation for URL templates and rewriting.
   * [PC-374] - AbstractMethodError: org.apache.xerces.dom.DeferredElementNSImpl.lookupPrefix

--------------------------------


Find more on wiki: http://wiki.exoplatform.org/xwiki/bin/view/PC/2_1

See also:
 Company site           http://www.exoplatform.com
 Community JIRA       http://jira.exoplatform.org/browse/PC
 Community forum     http://www.exoplatform.com/portal/public/en/forum
 JavaDoc site            http://docs.exoplatform.org/rest/jcr/repository/javadoc/pc/2.1/index.html

===========

EXO-PC Tomcat 6.0 bundle includes Apache Tomcat 6.0.18 distributive
bundled with eXo PC sample applications and neccessary libraries.

EXO-PC includes:
  Test portal - portal application which provide portlets functionalities (portal.war)
  Test portlets - portlets, which showing ability of the jsr168 portlet specification (portlets.war)
  Test portlets 2 - portlets, which showing ability of the jsr286 portlet specification (portlets2.war)
  Test demos - portlets, which showing ability of the jsr286 portlet specification (demos.war)
  Test wsrp - service, which processing deploy and handling of the remote portlets (wsrp.war)


1. Start Up
   Tomcat 6.0 with EXO-PC 2.1 bundled can be started by executing the following commands:

      $CATALINA_HOME\bin\exo-run.bat          (Windows)

      $CATALINA_HOME/bin/exo-run.sh           (Unix)

2. After startup, EXO-PC 2.1 sample applications will be available by visiting:

    http://localhost:8080/portal

3. More information in wiki at http://wiki.exoplatform.org/ or send an email to "exo-portlet-container@objectweb.org".

=================================


