package org.codecop.pmd.prototype;

import org.junit.Before;

import test.net.sourceforge.pmd.testframework.SimpleAggregatorTst;

public class PrototypeRulesTest extends SimpleAggregatorTst {

   private static final String RULESET = "prototype";

   @Before
   public void setUp() {
      addRule(RULESET, "EntityWithReferences");
      addRule(RULESET, "ImmutableValueObject");
   }

   public static junit.framework.Test suite() {
      return new junit.framework.JUnit4TestAdapter(PrototypeRulesTest.class);
   }
}
