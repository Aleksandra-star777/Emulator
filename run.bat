@echo off
chcp 65001 >nul

if not exist out mkdir out

"C:\Users\Aleksandra\.jdks\openjdk-26.0.2.1\bin\javac.exe" -encoding UTF-8 -d out src\ru\miem\shell\Main.java

if errorlevel 1 (
    echo Ошибка компиляции
    pause
    exit /b 1
)

"C:\Users\Aleksandra\.jdks\openjdk-26.0.2.1\bin\java.exe" -cp out ru.miem.shell.Main

pause