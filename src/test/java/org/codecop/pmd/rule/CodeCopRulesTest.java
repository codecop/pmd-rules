package org.codecop.pmd.rule;

import org.junit.Before;

import test.net.sourceforge.pmd.testframework.SimpleAggregatorTst;

public class CodeCopRulesTest extends SimpleAggregatorTst {

   private static final String RULESET = "codecop";

   @Before
   public void setUp() {
      addRule(RULESET, "ClassNamingConventions");
      addRule(RULESET, "SignatureDeclareThrowsException");
      addRule(RULESET, "JumbledIterator");
      addRule(RULESET, "UnintendedEnvUsage");
      addRule(RULESET, "JunitSetupDoesNotCallSuper");
      addRule(RULESET, "ParameterNameWithP");
      addRule(RULESET, "InterfaceNamesEndWithIF");
      addRule(RULESET, "NonFinalFieldInException");
      addRule(RULESET, "AvoidPrivateGetterAndSetter");
      addRule(RULESET, "MembersMustBePrivate");
   }

   public static junit.framework.Test suite() {
      return new junit.framework.JUnit4TestAdapter(CodeCopRulesTest.class);
   }

}
