@setlocal
@call env_pmd.bat
@cd ..\..

call mvn package
copy target\codecop-*.jar %PMD_HOME%\lib\

@cd src\it
@endlocal  
pause
