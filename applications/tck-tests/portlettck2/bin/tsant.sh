#!/bin/sh 
#
# @(#)tsant	1.16 03/03/26
#
# Copyright 1995-2002 by Sun Microsystems, Inc.,
# All rights reserved.
#
# This software is the confidential and proprietary information
# of Sun Microsystems, Inc. ("Confidential Information").  You
# shall not disclose such Confidential Information and shall use
# it only in accordance with the terms of the license agreement
# you entered into with Sun.
#


#
# tsant script. Must be called only after TS_HOME has been set.
#
# handles directory-related rout

# EXOMAN added and commentted the lines below
# TS_HOME="/home/alexey/java/eXoProjects/portlet-container/trunk/applications/tck-tests/portlettck2"
# export TS_HOME
# PATH=/home/alexey/java/eXoProjects/portlet-container/trunk/applications/tck-tests/portlettck2/bin:${PATH} 
# export PATH

if [ $#	-eq 1 ]
then
	if [ $1	= "ld" ]
	then
		newdir=`pwd | sed 's/\/src\//\/dist\//'`
	cd $newdir
	if [ $?	-ne 0 ]
	then
	    exit -1
	fi
		echo $newdir
		echo `pwd | sed	's/./-/g'`
		ls -tF $newdir | more
		exit 0
	fi

	if [ $1	= "lld" ]
	then
		newdir=`pwd | sed 's/\/src\//\/dist\//'`
	cd $newdir
	if [ $?	-ne 0 ]
	then
	    exit -1
	fi
		echo $newdir
		echo `pwd | sed	's/./-/g'`
		ls -tlF $newdir | more
		exit 0
	fi

	if [ $1	= "lc" ]
	then
		newdir=`pwd | sed 's/\/src\//\/classes\//'`
	cd $newdir
	if [ $?	-ne 0 ]
	then
	    exit -1
	fi
		echo $newdir
		echo `pwd | sed	's/./-/g'`
		ls -tF $newdir | more
		exit 0
	fi

	if [ $1	= "llc" ]
	then
		newdir=`pwd | sed 's/\/src\//\/classes\//'`
	cd $newdir
	if [ $?	-ne 0 ]
	then
	    exit -1
	fi
		echo $newdir
		echo `pwd | sed	's/./-/g'`
		ls -tlF $newdir | more
		exit 0
	fi
fi


if [ -z "${TS_HOME}" ]
then
    echo "ERROR: TS_HOME is NOT SET!! "
    echo "Please set TS_HOME (ie: /files/ts ) before running tsant."
    echo "Setup is INCOMPLETE - Exiting $0"
    exit 1
fi
if [ ! -d "${TS_HOME}" ]
then
    echo "ERROR: TS_HOME is NOT a valid directory"
    echo "Please set TS_HOME (ie: /files/ts ) to a valid directory"
    echo "before running tsant."
    echo "Setup is INCOMPLETE - Exiting $0"
    exit 1
fi
echo "TS_HOME is set to:${TS_HOME}"


if [ -z "${JAVA_HOME}" ]
then
    echo "ERROR: JAVA_HOME is NOT SET!! "
    echo "Please set JAVA_HOME (ie: /files/j2se ) before running tsant."
    echo "Setup is INCOMPLETE - Exiting $0"
    exit 1
fi
if [ ! -d "${JAVA_HOME}" ]
then
    echo "ERROR: JAVA_HOME is NOT a valid directory"
    echo "Please set JAVA_HOME (ie: /files/j2se ) to a valid directory"
    echo "before running tsant."
    echo "Setup is INCOMPLETE - Exiting $0"
    exit 1
fi
echo "JAVA_HOME is set to:${JAVA_HOME}"

if [ -z "${ANT_HOME}" ]
then
    ANT_HOME=${TS_HOME}/tools/ant
fi

if [ ! -d "${ANT_HOME}" ]
then
    echo "ERROR: ANT_HOME is NOT a valid directory"
    echo "Please set ANT_HOME (ie: /files/ant ) to a valid directory"
    echo "before running tsant."
    echo "Setup is INCOMPLETE - Exiting $0"
    exit 1
fi
if [ ! -x "${ANT_HOME}/bin/ant" ]
then
    echo "ERROR: The ant script (${ANT_HOME}/bin/ant)is not executable"
    echo "Please correct this situation before running tsant."
    echo "Setup is INCOMPLETE - Exiting $0"
    exit 1
fi
echo "Using Ant ${ANT_HOME}"

TS_ANT_JAR=${TS_HOME}/lib/ant_sun.jar
HARNESS_JARS=${TS_HOME}/lib/tsharness.jar

CLASSPATH=${TS_ANT_JAR}:${HARNESS_JARS}
export CLASSPATH

#unset JAVACMD. Use JAVA_HOME/bin/java instead
JAVACMD=
export JAVACMD

PATH=${JAVA_HOME}/bin:${PATH}
export PATH

if [ -f "./build.xml" ]
then
	${ANT_HOME}/bin/ant -listener com.sun.ant.TSBuildListener -logger com.sun.ant.TSLogger "$@"
else
	${ANT_HOME}/bin/ant -buildfile ${TS_HOME}/bin/build.xml -listener com.sun.ant.TSBuildListener -logger com.sun.ant.TSLogger "$@"
fi
