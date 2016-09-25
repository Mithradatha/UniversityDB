@ECHO OFF
>nul 2>&1 "%SYSTEMROOT%\system32\cacls.exe" "%SYSTEMROOT%\system32\config\system"

IF '%errorlevel%' NEQ '0' (
    ECHO Requesting administrative privileges...
    GOTO UACPrompt
) ELSE ( GOTO gotAdmin )

:UACPrompt
    ECHO Set UAC = CreateObject^("Shell.Application"^) > "%temp%\getadmin.vbs"
    SET params = %*:"=""
    ECHO UAC.ShellExecute "cmd.exe", "/c %~s0 %params%", "", "runas", 1 >> "%temp%\getadmin.vbs"

    "%temp%\getadmin.vbs"
    DEL "%temp%\getadmin.vbs"
    EXIT /B

:gotAdmin
    pushd "%CD%"
    CD /D "%~dp0"
	
ECHO.
ECHO Download Python-2.7.12
ECHO.
start https://www.python.org/downloads/
:ask
SET answer=N
SET /p answer="Finished Downloading (Y/N)? "
IF %answer%==Y (
ECHO.
ECHO Installing Python-2.7.12...
msiexec /i %USERPROFILE%\Downloads\python-2.7.12.msi ALLUSERS=1 ADDLOCAL=ALL
ECHO Python Installed!
ECHO.
SET /p final="Press Enter"
GOTO end
)
GOTO ask
:end
ECHO.
ECHO Example Python Path: C:\Program Files (x86)\Python
SET /p path="Enter Python Path: "
ECHO.
ECHO Updating Pip...
python -m pip install --upgrade pip
ECHO Pip Updated!
ECHO.
SET /p final="Press Enter"
SET path=%path%\Scripts
ECHO.
ECHO Installing Faker...
pip install Faker
ECHO Faker Installed!
ECHO.
SET /p final="Press Enter"
GOTO :eof