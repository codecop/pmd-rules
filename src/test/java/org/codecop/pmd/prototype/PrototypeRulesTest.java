package org.codecop.pmd.prototype;

import net.sourceforge.pmd.testframework.SimpleAggregatorTst;

import org.junit.Before;

/**
 * Test the rules in the <code>prototype</code> ruleset.
 * @author <a href="http://www.code-cop.org/">Peter Kofler</a>
 */
public class PrototypeRulesTest extends SimpleAggregatorTst {

   private static final String RULESET = "java-prototype";

   @Before
   @Override
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
