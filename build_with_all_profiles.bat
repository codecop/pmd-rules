@setlocal
@call profile160
@pause

@echo running with PMD 5.0
call mvn2 clean verify
rem mvn site
rem @start target\site\index.html
@pause

@echo running with PMD 5.1
call mvn2 clean verify -Dpmd=5.1
@pause

@echo running with PMD 5.2
call mvn2 clean verify -Dpmd=5.2
@pause

@echo running with PMD 5.3
call mvn2 clean verify -Dpmd=5.3
@pause
@endlocal

@setlocal
@call profile170
@pause

@echo running with PMD 5.4
call mvn clean verify -Dpmd=5.4
@pause

@echo running with PMD 5.5
call mvn clean verify -Dpmd=5.x
@pause
@endlocal
