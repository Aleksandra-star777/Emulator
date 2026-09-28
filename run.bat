@echo off
chcp 65001 >nul
cd /d "%~dp0"

set JAVA_HOME=C:\Users\Aleksandra\.jdks\openjdk-26.0.2.1
set PATH=%JAVA_HOME%\bin;%PATH%

echo Сборка проекта...
javac src/Main.java

if errorlevel 1 (
    echo Ошибка сборки!
    pause
    exit /b 1
)

echo Запуск эмулятора...
java -cp src Main

pause