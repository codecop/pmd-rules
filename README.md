# PMD Rules - Custom PMD Rules

Custom rules for PMD code analysis.

[PMD](https://pmd.github.io/) scans Java source code and looks for potential problems. (Read about how it works [here](https://docs.pmd-code.org/latest/pmd_devdocs_how_pmd_works.html).) It comes with more than 200 [predefined rules](https://docs.pmd-code.org/latest/pmd_rules_java.html). It's possible to define your own rules; I have done so since 2004. This project contains a mixed set of custom rules for PMD regarding basic bugs, design principles, JUnit test, naming and other rule groups as well as some useful templates to copy from.

The `pmd-4.x` branch contains some early rules compatible with PMD 4.1 up to PMD 4.3 and its last release is `1.1-pmd4`. The `pmd-5.x` branch contains rules for versions 5.0.5 to 5.8.1 and its last release is `1.2.4-pmd5`. PMD 6 is the current one.   

## Rule Sets

* Read my [motivation for first custom rules](https://blog.code-cop.org/2010/05/custom-pmd-rules.html).
* [PmdRulesCodecop](https://github.com/codecop/pmd-rules/wiki/PmdRulesCodecop) - rules for various bugs and conventions.
* [PmdRulesConstraints](https://github.com/codecop/pmd-rules/wiki/PmdRulesConstraints) - rules for constraints in code katas, Coding Dojos and Code Retreats.
* [PmdRulesPrototype](https://github.com/codecop/pmd-rules/wiki/PmdRulesPrototype)

Being lazy, of course I did not write these rule documentations by hand. I had it generated using a Ruby script `rule_summary_*.rb` that converts PMD's `ruleset.xml` format into wiki syntax.

## Usage

Add the jar containing the rule sets to the PMD classpath, just copy the `pmd-rules-x.y.z.jar` into PMD's `lib` folder. Download the latest rules from [here](https://www.code-cop.org/mvn2repo/releases/org/codecop/pmd-rules/). For example the 1.2.4 version is compatible with PMD 5 and PMD 6. For the latest [Maven PMD plugin](http://maven.apache.org/plugins/maven-pmd-plugin/) this looks like:

    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-pmd-plugin</artifactId>
        <version>3.21.2</version>
        <dependencies>
            <!-- comes with PMD 6.55.0 -->
            <dependency>
                <groupId>org.codecop</groupId>
                <artifactId>pmd-rules</artifactId>
                <version>1.2.4-pmd6</version>
            </dependency>
        </dependencies>
    </plugin>

You have to add my [Mvn2Repo](https://blog.code-cop.org/p/mvn2repo.html) to your Maven repositories to get the releases.

### License

[New BSD License](http://opensource.org/licenses/bsd-license.php), see `license.txt` in repository.
