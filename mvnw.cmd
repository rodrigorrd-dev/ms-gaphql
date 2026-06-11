@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF)
@REM Maven Wrapper startup batch script for Windows
@REM ----------------------------------------------------------------------------
@IF "%__MVNW_ARG0_NAME__%"=="" (SET "BASE_DIR=%~dp0")

@SET MAVEN_PROJECTBASEDIR=%BASE_DIR%
@IF NOT "%MAVEN_PROJECTBASEDIR%"=="" GOTO endDetectBaseDir

@SET EXEC_DIR=%CD%
@SET WDIR=%EXEC_DIR%
:findBaseDir
@IF EXIST "%WDIR%"\.mvn SET MAVEN_PROJECTBASEDIR=%WDIR%
@cd ..
@IF "%WDIR%"=="%CD%" GOTO baseDirEnd
@SET WDIR=%CD%
@GOTO findBaseDir
:baseDirEnd
@cd "%EXEC_DIR%"
:endDetectBaseDir

@IF NOT EXIST "%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar" (
  @SET MVNW_VERBOSE=true
)

@IF "%MVNW_VERBOSE%"=="true" (
  @echo Downloading https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar to "%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar" 1>&2
)

@SET DOWNLOAD_URL=https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar

@IF NOT "%MVNW_USERNAME%"=="" (
  @SET DOWNLOAD_ARGS=%DOWNLOAD_ARGS% "--user=%MVNW_USERNAME%:%MVNW_PASSWORD%"
)

@IF EXIST "%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar" GOTO skipDownload
@IF NOT EXIST "%JAVA_HOME%" (
  @echo Downloading maven-wrapper.jar using curl 1>&2
  @curl -sS %DOWNLOAD_ARGS% -o "%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar" "%DOWNLOAD_URL%"
) ELSE (
  @echo Downloading maven-wrapper.jar using Java 1>&2
  @"%JAVA_HOME%\bin\java" -classpath "%JAVA_HOME%\lib\bootstrap.jar;%JAVA_HOME%\lib\ext\*" \
    org.apache.maven.wrapper.MavenWrapperDownloader "%DOWNLOAD_URL%" \
    "%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar"
)
:skipDownload

@SET JAVA_HOME_ORIGINAL=%JAVA_HOME%
@CALL :findJavaFromToolchains
@IF ERRORLEVEL 1 @GOTO skipToolchains
:skipToolchains

@IF NOT "%JAVA_HOME%"=="" GOTO skip2
@GOTO endDetectJava
:skip2
@SET JAVA_EXES="%JAVA_HOME%\bin\javaw.exe" "%JAVA_HOME%\bin\java.exe"
@FOR %%f IN (%JAVA_EXES%) DO (
  @IF EXIST %%f SET MVNW_JAVA_ARGS=%%f
)
@GOTO endDetectJava
:endDetectJava

@SET MAVEN_JAVA_EXE="java"
@IF NOT "%MVNW_JAVA_ARGS%"=="" SET MAVEN_JAVA_EXE=%MVNW_JAVA_ARGS%

@SET MVN_CMD=%MAVEN_JAVA_EXE% %JVM_CONFIG_MAVEN_PROPS% %MAVEN_OPTS% %MAVEN_DEBUG_OPTS% \
  -classpath "%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar" \
  "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" \
  org.apache.maven.wrapper.MavenWrapperMain %*

@%MVN_CMD%

@GOTO findJavaFromToolchainsEND
:findJavaFromToolchains
@EXIT /B 1
:findJavaFromToolchainsEND

@EXIT /B %ERRORLEVEL%
