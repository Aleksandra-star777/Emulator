@echo off
chcp 65001 >nul
cd /d "%~dp0"

rem Автопоиск JDK в папке пользователя .jdks
for /d %%i in ("%USERPROFILE%\.jdks\*") do (
    if exist "%%i\bin\javac.exe" set "JAVA_HOME=%%i"
)

if not defined JAVA_HOME (
    echo JDK не найден в %USERPROFILE%\.jdks
    echo Установите JDK или укажите JAVA_HOME вручную.
    pause
    exit /b 1
)

set "PATH=%JAVA_HOME%\bin;%PATH%"

echo Используется JDK: %JAVA_HOME%
echo Сборка проекта...

rem Создаем папку out, если её нет
if not exist out mkdir out

rem Компилируем с учетом пакета и кодировки UTF-8
javac -encoding UTF-8 -d out src\ru\miem\shell\Main.java

if errorlevel 1 (
    echo Ошибка сборки!
    pause
    exit /b 1
)

echo Запуск эмулятора...
rem Запускаем, указывая classpath (папку out) и полное имя класса с пакетом
java -cp out ru.miem.shell.Main

pause