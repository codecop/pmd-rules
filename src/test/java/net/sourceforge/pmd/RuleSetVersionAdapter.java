package net.sourceforge.pmd;

/**
 * Wrapper around changed PMD5 API used in testing framework.
 * 
 * @author PMD 5.6
 * @author <a href="https://www.code-cop.org/">Peter Kofler</a>
 */
public class RuleSetVersionAdapter {

    public static RuleSet create(Rule rule) {
        RuleSetFactory factory = new RuleSetFactory();
        RuleSet rules = factory.createSingleRuleRuleSet(rule);
        return rules;
    }

}
