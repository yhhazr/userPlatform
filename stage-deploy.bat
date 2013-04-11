@echo off
cd /d %~dp0
cmd /c mvn clean site site:stage-deploy -N
pause
