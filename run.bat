@echo off
chcp 65001 >nul
cd /d "%~dp0"

rem === Поиск JDK ===
set "JAVA_HOME="

rem 1. Проверяем переменную окружения JAVA_HOME
if defined JAVA_HOME (
    if exist "%JAVA_HOME%\bin\javac.exe" goto :jdk_found
)

rem 2. Ищем в папке пользователя .jdks (JDK от IntelliJ IDEA)
for /d %%i in ("%USERPROFILE%\.jdks\*") do (
    if exist "%%i\bin\javac.exe" set "JAVA_HOME=%%i"
)
if defined JAVA_HOME goto :jdk_found

rem 3. Ищем в стандартных местах установки JDK
for /d %%i in ("C:\Program Files\Java\jdk*") do (
    if exist "%%i\bin\javac.exe" set "JAVA_HOME=%%i"
)
if defined JAVA_HOME goto :jdk_found

for /d %%i in ("C:\Program Files\Eclipse Adoptium\jdk*") do (
    if exist "%%i\bin\javac.exe" set "JAVA_HOME=%%i"
)
if defined JAVA_HOME goto :jdk_found

echo JDK не найден. Установите JDK или задайте JAVA_HOME вручную.
pause
exit /b 1

:jdk_found
set "PATH=%JAVA_HOME%\bin;%PATH%"

echo Используется JDK: %JAVA_HOME%
echo Сборка проекта...

if not exist out mkdir out
javac -encoding UTF-8 -d out src\ru\miem\shell\Main.java
if errorlevel 1 (
    echo Ошибка сборки!
    pause
    exit /b 1
)

echo Запуск эмулятора с параметрами --vfs и --script...
java -cp out ru.miem.shell.Main --vfs "./vfs_data" --script "./startup.txt"

pause