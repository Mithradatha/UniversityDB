@ECHO OFF

setlocal enabledelayedexpansion

SET origin=%~dp0
SET root=.\..\..\..

:begin
ECHO. && ECHO.
ECHO 1. Drop/Create Database
ECHO.2. Truncate Tables
ECHO.3. Generate Data
ECHO.4. Load Data
ECHO.5. Exit
ECHO. && ECHO.

SET lang=2
SET scale=1

SET answer=5
SET /p answer=" >> "
ECHO.

IF %answer%==1 (
	ECHO Connecting to Database...
	mysql -h localhost -u root -p < university_ddl.cmd
	ECHO University Database Created!
)
IF %answer%==2 ECHO !origin!
IF %answer%==3 (
	PUSHD %origin%
	CD /d %root%/Generation
	
	ECHO Generate with:
	ECHO.
	ECHO 1. Java
	ECHO 2. Python
	ECHO.
	SET /p lang=" >> "
	ECHO.
	
	SET /p scale="Enter Scale Factor: "
	ECHO.
	
	ECHO Generating Random Data...
	IF !lang!==1 (java -cp Generation; JGen !scale! > .\Data\dataGen-output.txt)
	IF !lang!==2 (python PGen.py !scale! > .\Data\dataGen-output.txt)
	ECHO Data Generated.

	POPD
	ECHO.
)
IF %answer%==4 (
	PUSHD %origin%
	CD /d %root%/Generation/Data

	ECHO Connecting to Database...
	mysql -h localhost -u root -p < %origin%\university_load.cmd
	ECHO University Database Loaded.
	
	POPD
	ECHO.
)
IF %answer%==5 GOTO end
IF %answer%==exit GOTO end
GOTO begin
:end
GOTO :eof