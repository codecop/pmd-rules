package org.codecop.pmd.constraints;

import net.sourceforge.pmd.testframework.SimpleAggregatorTst;

import org.junit.Before;

/**
 * Test the rules in the <code>constraints</code> ruleset.
 * 
 * @author <a href="http://www.code-cop.org/">Peter Kofler</a>
 */
public class ConstraintsRulesTest extends SimpleAggregatorTst {

    private static final String RULESET = "java-constraints";

    @Before
    @Override
    public void setUp() {
        addRule(RULESET, "OnlyVoidMethods");
        addRule(RULESET, "NoVoidMethods");
        addRule(RULESET, "NoLoops");
        addRule(RULESET, "NoElseKeyword");
        addRule(RULESET, "NoConditionals");
        addRule(RULESET, "NoGetterAndSetter");
        addRule(RULESET, "OnlyStaticMethods");
        addRule(RULESET, "OnlyStaticFields");
        addRule(RULESET, "LooongIdentifiers");
        addRule(RULESET, "ShortStatements");
        addRule(RULESET, "NoStaticMethods");
        addRule(RULESET, "NoStaticFields");
        addRule(RULESET, "OnlyMapsAsParameters");
    }

    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(ConstraintsRulesTest.class);
    }

}
