# PMD Rules - Custom PMD Rules

Custom rules for PMD code analysis.

[PMD](https://pmd.github.io/) scans Java source code and looks for potential problems. (Read about how it works [here](https://pmd.github.io/pmd-5.5.4/customizing/howitworks.html).) It comes with more than 200 [predefined rules](https://pmd.github.io/pmd-5.5.4/pmd-java/index.html). It's possible to define your own rules; I have done so since 2004. This project contains a mixed set of custom rules for PMD 5.x regarding basic bugs, design principles, JUnit test, naming and other rule groups as well as some useful templates to copy from. The `pmd-4.x` branch contains the same rules compatible with PMD 4.1 up to PMD 4.3.

## Rule Sets

* Read my [motivation for first custom rules](https://blog.code-cop.org/2010/05/custom-pmd-rules.html).
* [PmdRulesCodecop](https://github.com/codecop/pmd-rules/wiki/PmdRulesCodecop) - rules for various bugs and conventions.
* [PmdRulesConstraints](https://github.com/codecop/pmd-rules/wiki/PmdRulesConstraints) - rules for constraints in code katas, Coding Dojos and Code Retreats.
* [PmdRulesPrototype](https://github.com/codecop/pmd-rules/wiki/PmdRulesPrototype)

Being lazy, of course I did not write these rule documentations by hand. I had it generated using a Ruby script `rule_summary_*.rb` that converts PMD's `ruleset.xml` format into wiki syntax.

## Usage

Add the jar containing the rule sets to the PMD classpath, just copy the `codecop-pmd-rules.jar` into PMD's `lib` folder. Download the latest rules from [here](https://www.code-cop.org/mvn2repo/releases/org/codecop/pmd-rules/). The current 1.2 version is compatible with PMD 5. For the latest [Maven PMD plugin](http://maven.apache.org/plugins/maven-pmd-plugin/) this looks like:

    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-pmd-plugin</artifactId>
        <version>3.3</version>
        <dependencies>
            <dependency>
                <groupId>org.codecop</groupId>
                <artifactId>pmd-rules</artifactId>
                <version>1.2.2</version>
            </dependency>
            <dependency>
                <groupId>net.sourceforge.pmd</groupId>
                <artifactId>pmd-java</artifactId>
                <version>5.8.1</version>
            </dependency>
        </dependencies>
    </plugin>

The rules also work with any earlier PMD down to PMD 5.1,

    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-pmd-plugin</artifactId>
        <version>3.2</version>
        <dependencies>
            <dependency>
                <groupId>org.codecop</groupId>
                <artifactId>pmd-rules</artifactId>
                <version>1.2.2</version>
            </dependency>
            <dependency>
                <groupId>net.sourceforge.pmd</groupId>
                <artifactId>pmd</artifactId>
                <version>5.1.3</version>
            </dependency>
        </dependencies>
    </plugin>

and there is an PMD 4.3 compatible 1.1-pmd4 release, which does not contain the newer rules.

    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-pmd-plugin</artifactId>
        <version>2.7.1</version>
        <dependencies>
            <dependency>
                <groupId>org.codecop</groupId>
                <artifactId>pmd-rules</artifactId>
                <version>1.1-pmd4</version>
            </dependency>
            <dependency>
                <groupId>pmd</groupId>
                <artifactId>pmd</artifactId>
                <version>4.3</version>
            </dependency>
        </dependencies>
    </plugin>

You have to add my [Mvn2Repo](https://blog.code-cop.org/p/mvn2repo.html) to your Maven repositories to get the releases.

### License

[New BSD License](http://opensource.org/licenses/bsd-license.php), see `license.txt` in repository.
