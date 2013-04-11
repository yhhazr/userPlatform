@echo off 
cd /d %~dp0
cmd /c mvn clean site site:deploy -N
pause
