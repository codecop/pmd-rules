# PMD Rules - Custom PMD Rules #

Custom rules for PMD code analysis.

[PMD](http://pmd.sourceforge.net/) scans Java source code and looks for potential problems. (Read about how it works [here](http://pmd.sourceforge.net/howitworks.html).) It comes with more than 200 [predefined rules](http://pmd.sourceforge.net/rules/index.html). It's possible to define your own rules; I have done so since 2004. This project contains a mixed set of custom rules for PMD 5.0.0 regarding basic bugs, design principles, JUnit test, naming and other rule groups as well as some useful templates to copy from. The `pmd-4.x` branch contains the same rules compatible with PMD 4.1 up to PMD 4.3.


## Rule Sets ##

 * Read my [motivation for first custom rules](http://blog.code-cop.org/2010/05/custom-pmd-rules.html).
 * [[PmdRulesCodecop]] - rules for various bugs and conventions. 
 * [[PmdRulesConstraints]] - rules for constraints in code katas, Coding Dojos and Code Retreats. 
 * [[PmdRulesPrototype]]

Being lazy, of course I did not write these rule documentations by hand. I had it generated using a Ruby script `rule_summary_google_code.rb` that converts PMD's ruleset.xml format into wiki syntax.


## Usage ##
Add the jar containing the rulesets to the PMD classpath, just copy the codecop-pmd-rules.jar into PMD's lib folder. Download the latest rules from [http://mvn2repo.code-cop-code.googlecode.com/hg/releases/org/codecop/pmd-rules/ here]. The current 1.2 branch is compatible with PMD 5. For the latest [http://maven.apache.org/plugins/maven-pmd-plugin/ Maven PMD plugin] this looks like:
{{{
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-pmd-plugin</artifactId>
    <version>3.3</version>
    <dependencies>
        <dependency>
            <groupId>org.codecop</groupId>
            <artifactId>pmd-rules</artifactId>
            <version>1.2.1</version>
        </dependency>
        <dependency>
            <groupId>net.sourceforge.pmd</groupId>
            <artifactId>pmd-java</artifactId>
            <version>5.2.2</version>
        </dependency>
    </dependencies>
</plugin>
}}}
The rules also work with PMD 5.1,
{{{
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-pmd-plugin</artifactId>
    <version>3.2</version>
    <dependencies>
        <dependency>
            <groupId>org.codecop</groupId>
            <artifactId>pmd-rules</artifactId>
            <version>1.2.1</version>
        </dependency>
        <dependency>
            <groupId>net.sourceforge.pmd</groupId>
            <artifactId>pmd</artifactId>
            <version>5.1.3</version>
        </dependency>
    </dependencies>
</plugin>
}}}
and there is an PMD 4.3 compatible 1.1-pmd4 release, which does not contain newer rules.
{{{
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
}}}

You have to add Mvn2Repo to your Maven repositories to get the releases. Check out the [GlobalRuleset global rule-set module] for a more detailed example of the proper settings.