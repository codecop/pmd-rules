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
    public void setUp() {
        addRule(RULESET, "OnlyVoidMethods");
        addRule(RULESET, "NoVoidMethods");
    }

    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(ConstraintsRulesTest.class);
    }

}
