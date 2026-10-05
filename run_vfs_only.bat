@echo off
chcp 65001 >nul
cd /d "%~dp0"

set "JAVA_HOME="
if defined JAVA_HOME if exist "%JAVA_HOME%\bin\javac.exe" goto :jdk_found
for /d %%i in ("%USERPROFILE%\.jdks\*") do (
    if exist "%%i\bin\javac.exe" set "JAVA_HOME=%%i"
)
if defined JAVA_HOME goto :jdk_found
for /d %%i in ("C:\Program Files\Java\jdk*") do (
    if exist "%%i\bin\javac.exe" set "JAVA_HOME=%%i"
)

:jdk_found
if not defined JAVA_HOME (
    echo JDK не найден.
    pause
    exit /b 1
)
set "PATH=%JAVA_HOME%\bin;%PATH%"

if not exist out mkdir out
javac -encoding UTF-8 -d out src\ru\miem\shell\Main.java
if errorlevel 1 (
    echo Ошибка сборки!
    pause
    exit /b 1
)

echo Запуск эмулятора только с --vfs...
java -cp out ru.miem.shell.Main --vfs "./vfs_data"

pause