@setlocal
@call profile180
@pause

@rem 6.0.1 ... 2018
@echo running with PMD 6.0
call mvn clean verify
rem mvn site
rem @start target\site\index.html
@pause

@rem 6.11.0 ... 2019
@echo running with PMD 6.11
call mvn clean test -Dpmd.version=6.11.0
@pause

@rem 6.21.0 ... 2020
@echo running with PMD 6.21
call mvn clean test -Dpmd.version=6.21.0
@pause

@endlocal
