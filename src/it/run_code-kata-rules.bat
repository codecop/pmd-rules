@setlocal
@call env_pmd.bat

call %PMD_HOME%\bin\pmd.bat -d resources -R ../main/resources/rulesets/java/code-kata.xml
echo ------------------------------------------------------------
call %PMD_HOME%\bin\pmd.bat -d resources -R ../main/resources/rulesets/java/object-calisthenics.xml
echo ------------------------------------------------------------
call %PMD_HOME%\bin\pmd.bat -d resources -R ../main/resources/rulesets/java/brutal-refactoring.xml

@endlocal  
pause
