@echo off

REM Copyright 2002 Sun Microsystems, Inc. All rights reserved.
REM SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.

REM @(#)tsant.bat       1.9   02/05/13

REM uncomment below for resolve test failure problems
REM set JAVA_HOME=c:\progra~1\java\jdk15~1.0_0

@setlocal
if "%1" == "ld" goto ld
if "%1" == "lld" goto lld
if "%1" == "lc" goto lc
if "%1" == "llc" goto llc


if "%TS_HOME%" == "" goto end1

if "%ANT_HOME%" == "" set ANT_HOME=%TS_HOME%\tools\ant
if not exist "%ANT_HOME%\bin\ant.bat" goto end3

if "%JAVA_HOME%" == "" goto end4

echo TS_HOME is set to %TS_HOME%
echo JAVA_HOME is set to %JAVA_HOME%
echo Using Ant %ANT_HOME%

REM CLASSPATH defines extra jar files used by ant.

REM unset JAVACMD. Use JAVA_HOME\bin\java instead
set JAVACMD=

REM prevent ant from using javac in the path. Use JAVA_HOME\bin\javac instead
set path=%JAVA_HOME%\bin;%path%

set TS_ANT_JAR=%TS_HOME%\lib\ant_sun.jar
set HARNESS_JARS=%TS_HOME%\lib\tsharness.jar
set CLASSPATH=%TS_ANT_JAR%;%HARNESS_JARS%

if not exist build.xml goto skip1;
call "%ANT_HOME%\bin\ant.bat" -listener com.sun.ant.TSBuildListener -logger com.sun.ant.TSLogger %*
goto end

:skip1
call "%ANT_HOME%\bin\ant.bat" -buildfile %TS_HOME%\bin\build.xml -listener com.sun.ant.TSBuildListener -logger com.sun.ant.TSLogger %*
goto end


REM subs to handle directory-related routines
REM
:ld
for /f "delims=" %%a in ('cd') do set srcdir=%%a
set newdir=%srcdir:src=dist%
echo %newdir%
echo.
dir %newdir% /P/B/O-D
goto end

:lld
for /f "delims=" %%a in ('cd') do set srcdir=%%a
set newdir=%srcdir:src=dist%
dir %newdir% /P/O-D
goto end

:lc
for /f "delims=" %%a in ('cd') do set srcdir=%%a
set newdir=%srcdir:src=classes%
echo %newdir%
echo.
dir %newdir% /P/B/O-D
goto end

:llc
for /f "delims=" %%a in ('cd') do set srcdir=%%a
set newdir=%srcdir:src=classes%
dir %newdir% /P/O-D
goto end

:end1
    echo ERROR: TS_HOME is NOT SET!!
    echo Please set TS_HOME (ie: c:\files\jsftck ) before running tsant.
    echo Setup is INCOMPLETE - Exiting
    goto end
:end3
    echo ERROR: ANT_HOME is NOT a valid directory
    echo Please set ANT_HOME (ie: c:\files\ts ) to a valid directory
    echo before running tsant.
    echo Setup is INCOMPLETE - Exiting
    goto end
:end4
    echo ERROR: JAVA_HOME is NOT SET!!
    echo Please set JAVA_HOME (ie: c:\files\j2se ) before running tsant.
    echo Setup is INCOMPLETE - Exiting
    goto end

:end
@endlocal
