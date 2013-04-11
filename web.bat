@echo off
cd /d %~dp0
cmd /c mvn clean site jetty:run -Pjetty -N
pause

