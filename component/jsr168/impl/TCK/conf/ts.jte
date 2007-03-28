#
# @(#)ts.jte	1.11 02/06/07
# Copyright 1995-2001 by Sun Microsystems, Inc.,
# All rights reserved.
#
# This software is the confidential and proprietary information
# of Sun Microsystems, Inc. ("Confidential Information").  You
# shall not disclose such Confidential Information and shall use
# it only in accordance with the terms of the license agreement
# you entered into with Sun.
# 
#########################################################################
#                                                                   	#
#   JavaTest Environment file for PORTLET Compatibility Test Suite       #
#                                                                       #
#   Environment specific properties in this file will likely	 	#
#   have to be modified prior to running the PORTLET TCK. Instructions	#
#   for modifying these properties are contained in this file.	 	#
#                                                                       #
#########################################################################
#EXO_UPDATE
TCK_HOME=d:/java/exo/ExoServices/PortletContainerService/TCK/
TS_HOME=${TCK_HOME}/portlettck
 
########################################################################
# Environment ts_unix                                              #
#Leave as default
########################################################################
env.ts_unix.description=\
        Run the runtime tests in a separate JVM (process) on Solaris
env.ts_unix.menu=true
env.ts_unix.testsuite=com.sun.ts.lib.harness.TS
env.ts_unix.command.testExecute=com.sun.ts.lib.harness.ExecTSTestCmd \
        CLASSPATH=${JAVA_HOME}/lib/rt.jar:${LOCAL_CLASSES} \
        DISPLAY=:0.0 \
        HOME=${user.home} \
        ${JAVA_HOME}/bin/java \
        -Dbytecheck=true \
        ${JAVA_OPTIONS} $testExecuteClass $testExecuteArgs

########################################################################
# Environment ts_win32						       #
#Leave as default
########################################################################
env.ts_win32.description=\
        Run the runtime tests in a separate JVM (process) on Win32
env.ts_win32.menu=true
env.ts_win32.testsuite=com.sun.ts.lib.harness.TS
env.ts_win32.command.testExecute=com.sun.ts.lib.harness.ExecTSTestCmd \
        CLASSPATH=${JAVA_HOME}\\lib\\rt.jar;${LOCAL_CLASSES} \
	   windir=${windir} \
	   SYSTEMROOT=${SYSTEMROOT} \
        ${JAVA_HOME}\\bin\\java \
        -Dbytecheck=true \
        ${JAVA_OPTIONS} $testExecuteClass $testExecuteArgs

########################################################################
#These properties are used by the Javatest harness. "harness.log.port"
#specifies the port that server components use to send logging
#output back to JavaTest.  If the default port # is not available
#on the machine running JavaTest, then you can set it here. 
#
#"harness.log.traceflag" is used to turn on/off verbose debugging output
#for the tests.
#
#Leave as default
##########################/##############################################
#EXO-UPDATE
harness.temp.directory=${TCK_HOME}/tmp
harness.log.port=2000
harness.log.mode=silent
harness.log.delayseconds=1 
harness.log.traceflag=false
ts_home=${TS_HOME}
cts_home=${TS_HOME}


#################################################################
#
#portalURLFetcherMode - valid values are 0, 1
# 0 - XML_BASED_URL_FETCHER 
# 1 - PROGRAMMATIC_URL_FETCHER
#
# Set this value to 0, if vendor is using Declarative Configuration to 
# let TCK obtain URL for each Test. If 0, you would need to set the 
# vendorTestsToURLMappingFile.  Content of this file are explained in detail
# in Chapter 22 of the JSR168 Spec. 
#
# Set this value to 1, if vendor is using Programmatic Configuration to
# let TCK obtain URL for each test. For this you would need to set
# the property vendorPortalURL
#
# Default value is set for Pluto.
#
#######################################################################
#EXO-UPDATE
portalURLFetcherMode=1

#########################################################
#
# Set this property only if portalURLFetcherMode=0 ( Declarative Configuration.)
# Set this to the name of file containing URLs for each test.
#
#
############################################################
#EXO-UPDATE
vendorTestsToURLMappingFile=${TCK_HOME}/conf/exoTestsToURLMapping.xml
#########################################################
#
# Set this property only if portalURLFetcherMode=1 ( programmatic Configuration)
#
# Syntax for this URL is explained in Chapter 22 of
# the spec.
#
############################################################
#vendorPortalURL=http://HOSTNAME:PORTNAME/pluto/tck
#EXO-UPDATE
vendorPortalURL=http://localhost:8080/exo/faces/public/page.jsp?_ctx=anonymous


#########################################################
#
# Set this property to adjust the level of output generated from
# the Http response when a test fails
# 0 = Only print the content inserted by the Portlet TCK for a test case
# 1= Print the whole response
#
############################################################
outputResponseContentLevel=1

#########################################################################
#The following properties must be set before running any tests
# that require authentication support.  
#
# Set authConfigType to one of the following.
#
# 0 -- if no authentication needed for any test at all.
#
# 1 -- if using the Basic Authentication for authUser/authpassword for tests
# specified by the vendor in ${TS_HOME}/bin/authTestList.txt
#
# 2 -- if using a vendor specific implementation of 
# TSPortletAuthCookieInterface.java that returns a cookie to be used for
# all tests specified by vendor in ${TS_HOME}/bin/authTestList.txt
# You would need to append the classpath to this file in 
# build.properties - local.classes
#
#########################################################################
authConfigType = 0

#
# Need to be set only if authConfigType is other than 0.
#
#authuser     = javajoe
#authpassword = javajoe
#
# Need to be set only if authConfigType=2
#
#porting.ts.portletAuthCookie.class=com.vendor.MyTSPortletAuthCookieImpl


############################################################################
# This won't be exposed. Only here for development purposes.
# value = 0 -- It would create xml file that would be used to create masterxml file.
# value = 1 -- It would be used to read the master xml file in the bin directory to make sure
# all tests are there with correct portlet info.
#
#
############################################################################
#masterXMLFileOper=1
