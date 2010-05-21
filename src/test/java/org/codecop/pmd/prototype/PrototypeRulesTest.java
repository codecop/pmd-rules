package org.codecop.pmd.prototype;

import org.junit.Before;

import test.net.sourceforge.pmd.testframework.SimpleAggregatorTst;

public class PrototypeRulesTest extends SimpleAggregatorTst {

   private static final String RULESET = "prototype";

   @Before
   public void setUp() {
      addRule(RULESET, "EntityWithReferences");
      addRule(RULESET, "ImmutableValueObject");
      addRule(RULESET, "EmptyFunction");
      addRule(RULESET, "MoreThanOneCustomLogger");
      addRule(RULESET, "CustomLoggerIsNotStaticFinal");
      addRule(RULESET, "NoAttrInClass");
      addRule(RULESET, "TypeNotAllowed");
      addRule(RULESET, "NewEnumOnlyInside");
      addRule(RULESET, "Call4MethodNotInPackage");
      addRule(RULESET, "ActionHasNoShortName");
      addRule(RULESET, "JUnitTestHasWrongName");
      addRule(RULESET, "CommitInDBLayer");
   }

   public static junit.framework.Test suite() {
      return new junit.framework.JUnit4TestAdapter(PrototypeRulesTest.class);
   }
}
