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

@rem 6.31.0 ... 2021
@echo running with PMD 6.31
call mvn clean test -Dpmd.version=6.31.0
@pause

@rem 6.42.0 ... 2022
@echo running with PMD 6.42
call mvn clean test -Dpmd.version=6.42.0
@pause

@rem 6.55.0 ... last 2023
@echo running with PMD 6.55
call mvn clean test -Dpmd.version=6.55.0
@pause

@endlocal
