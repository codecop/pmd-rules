@setlocal
@call profile160
@pause

@rem 5.0.5 ... 2013
@echo running with PMD 5.0
call mvn2 clean verify -Dpmd=5.0
rem mvn site
rem @start target\site\index.html
@pause

@rem 5.1.3 ... 2014
@echo running with PMD 5.1
call mvn2 clean test -Dpmd=5.0 -Dpmd.version=5.1.3
@pause

@rem 5.2.3 ... 2014
@echo running with PMD 5.2
call mvn2 clean verify -Dpmd=5.2 -Dpmd.version=5.2.3
@pause

@rem 5.3.8 ... 2016
@echo running with PMD 5.3
call mvn2 clean test -Dpmd=5.2 -Dpmd.version=5.3.8
@pause
@endlocal

@setlocal
@call profile170
@pause

@rem 5.4.3 ... 2016
@echo running with PMD 5.4
call mvn clean verify -Dpmd=5.2 -Dpmd.version=5.4.6
@pause

@rem 5.5.7 ... 2017
@echo running with PMD 5.5
call mvn clean test -Dpmd=5.2 -Dpmd.version=5.5.7
@pause

rem BREAKING BREAKING BREAKING BREAKING
@rem 5.6.1 ... 2017
@echo running with PMD 5.6
call mvn clean verify -Dpmd=5.2 -Dpmd.version=5.6.1
@pause

@rem 5.7.0 ... 2017
@echo running with PMD 5.7
call mvn clean test -Dpmd=5.2 -Dpmd.version=5.7.0
@pause

@rem 5.8.1 ... 2017
@echo running with PMD 5.8
call mvn clean test -Dpmd=5.2 -Dpmd.version=5.8.1
@pause

@endlocal
