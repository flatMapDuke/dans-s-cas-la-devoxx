@REM SBT launcher script
@REM 
@REM Envioronment:
@REM JAVA_HOME - location of a JDK home dir (mandatory)
@REM SBT_OPTS  - JVM options (optional)


@setlocal

@echo off

chcp 65001

set SBT_HOME=%~dp0
set ERROR_CODE=0

rem We use the value of the JAVACMD environment variable if defined
set _JAVACMD=%JAVACMD%

if "%_JAVACMD%"=="" (
  if not "%JAVA_HOME%"=="" (
    if exist "%JAVA_HOME%\bin\java.exe" set "_JAVACMD=%JAVA_HOME%\bin\java.exe"
  )
)

if "%_JAVACMD%"=="" set _JAVACMD=java

rem We use the value of the JAVA_OPTS environment variable if defined
set _JAVA_OPTS=%JAVA_OPTS%
if "%_JAVA_OPTS%"=="" set _JAVA_OPTS=-Xmx512M -XX:MaxPermSize=256m -XX:ReservedCodeCacheSize=128m -Dfile.encoding=UTF8 -Dsbt.log.format=true

set SBT_OPTS=-Dsbt.ivy.home=.\sbt\repository -Dsbt.boot.directory=.\sbt\boot -Dsbt.boot.properties=sbt/sbt.boot.properties  -Xss1M -XX:+CMSClassUnloadingEnabled

:run

"%_JAVACMD%" %_JAVA_OPTS% %SBT_OPTS% -cp .\sbt\jansi.jar;.\sbt\sbt-launch.jar;.\sbt SbtJansiLaunch %*
if ERRORLEVEL 1 goto error
goto end

:error
set ERROR_CODE=1

:end

@endlocal

exit /B %ERROR_CODE%





rem set SCRIPT_DIR=%~dp0

rem set SBT=sbt-launch-0.12.2.jar

rem java -Dsbt.ivy.home=.\ivyrepo -Dsbt.boot.directory=.\sbtboot -Xmx512M -jar -Dfile.encoding=UTF8 -Xss1M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=256m "%SCRIPT_DIR%\%SBT%" %*
