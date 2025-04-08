@echo off
echo Starting application with custom environment variables...
set DB_USERNAME=postgres
set DB_PASSWORD=pandacare
echo DB_USERNAME=%DB_USERNAME%
echo DB_PASSWORD=%DB_PASSWORD%
.\gradlew bootRun
