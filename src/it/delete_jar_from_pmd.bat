@setlocal
@call env_pmd.bat

del %PMD_HOME%\lib\codecop-*.jar

@endlocal  
pause
