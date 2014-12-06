@call profile160
@pause
@echo running with PMD 5.0
call mvn clean verify site
@start target\site\index.html
@pause
@echo running with PMD 5.1
call mvn clean verify -Dpmd=5.1
@pause
@echo running with PMD 5.2
call mvn clean verify -Dpmd=5.x
@pause
