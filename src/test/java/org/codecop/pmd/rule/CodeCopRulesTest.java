package org.codecop.pmd.rule;

import org.junit.Before;

import test.net.sourceforge.pmd.testframework.SimpleAggregatorTst;

public class CodeCopRulesTest extends SimpleAggregatorTst {

   @Before
   public void setUp() {
      addRule("codecop", "ClassNamingConventions");
      addRule("codecop", "SignatureDeclareThrowsException");
   }

   public static junit.framework.Test suite() {
      return new junit.framework.JUnit4TestAdapter(CodeCopRulesTest.class);
   }
}
