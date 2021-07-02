@echo off

rem ----------------
set name="playerfarms-bungee"
set version="1.0.0"
rem ----------------

call build.bat
copy "target\%name%-%version%.jar" "run\plugins"
cd "./run"
java -server -Xms4G -Xmx4G -jar "BungeeCord.jar" nogui
pause