@echo off
chcp 65001 >nul

set JAVA_HOME=C:\Users\Aleksandra\.jdks\openjdk-26.0.2.1
set PATH=%JAVA_HOME%\bin;%PATH%

if not exist out mkdir out

javac -encoding UTF-8 -d out src\ru\miem\shell\Main.java

if errorlevel 1 (
    echo Ошибка компиляции
    pause
    exit /b 1
)

java -cp out ru.miem.shell.Main

pause