@echo off
chcp 65001 >nul

if not exist out mkdir out

javac -encoding UTF-8 -d out src\ru\miem\shell\Main.java

if errorlevel 1 (
    echo Ошибка компиляции
    pause
    exit /b 1
)

java -cp out ru.miem.shell.Main

pause