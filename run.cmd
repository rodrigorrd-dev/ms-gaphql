@echo off
set JAVA_HOME=C:\Users\Rodrigo\.jdks\corretto-21.0.6
set PATH=%JAVA_HOME%\bin;C:\Users\Rodrigo\.maven\maven-3.9.16\bin;%PATH%
echo Java: %JAVA_HOME%
echo.
mvn spring-boot:run
