@ECHO OFF
SET menu=ECHO. && ECHO. && ECHO 1. Drop/Create Database && ECHO.2. Truncate Tables && ECHO.3. Generate/Load Data && ECHO.4. Exit
:begin
ECHO.
%menu%
SET answer=4
SET /p answer=" >> "
ECHO.
IF %answer%==1 (
ECHO Connecting to Database...
mysql -h localhost -u root -p < university_ddl.cmd
ECHO University Database Created!
)
IF %answer%==2 ECHO Truncated!
IF %answer%==3 (
ECHO Generating Random Data...
python DataGen/Main.py > dataGen-output.txt
ECHO Data Generated!
ECHO Connecting to Database...
mysql -h localhost -u root -p < university_load.cmd > dataLoad-output.txt
ECHO University Database Loaded!
ECHO.
FC dataGen-output.txt dataLoad-output.txt > results.txt
CAT results.txt
)
IF %answer%==4 GOTO end
GOTO begin
:end
GOTO :eof