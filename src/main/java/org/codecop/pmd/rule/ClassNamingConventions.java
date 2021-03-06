package org.codecop.pmd.rule;

import org.codecop.pmd.versions.IntegerPropertyAdapter;

import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

/**
 * Changed Class name rule to allow underscore before _Stub and _Core (RMI). Added some additional tests.
 * @author PMD 3.1 - Copied from PMD 3.1 Source Edition.
 * @author PMD 3.7 - updated
 * @author <a href="https://www.code-cop.org/">Peter Kofler</a>
 * @see net.sourceforge.pmd.lang.java.rule.codestyle.ClassNamingConventionsRule
 */
public class ClassNamingConventions extends AbstractJavaRule {

   private int upperCaseLen;

   private static final IntegerPropertyAdapter UPPERCASE_LEN_DESCRIPTOR = new IntegerPropertyAdapter("upperCaseLen", "Allowed length of upper case only names", 1, 99, 3, 1.0f);
   
   public ClassNamingConventions() {
      // definePropertyDescriptor(UPPERCASE_LEN_DESCRIPTOR);
      // defined in XML for better readability
   }

   @Override
   public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
      upperCaseLen = IntegerPropertyAdapter.getProperty(this, UPPERCASE_LEN_DESCRIPTOR);

      final String className = node.getImage();

      // first letter must be upper case letter
      if (Character.isLowerCase(className.charAt(0)) || !Character.isLetter(className.charAt(0))) {
         // --- PMD 3.1
         // RuleContext ctx = (RuleContext) data;
         // ctx.getReport().addRuleViolation(createRuleViolation(ctx, node, getMessage()));
         // --- PMD 3.7
         addViolation(data, node);
      }

      // must not be upper case only
      if (className.toUpperCase().equals(className) && className.length() > upperCaseLen) {
         addViolation(data, node);
      }

      // must not contain $ or blank
      else if (className.indexOf('$') >= 0 || className.indexOf(' ') >= 0) {
         addViolation(data, node);
      }

      // must not contain underscore but RMI stubs and Core pattern
      else if (className.indexOf('_') >= 0 && !className.endsWith("_Stub") && !className.endsWith("_Core")) {
         addViolation(data, node);
      }

      return data;
   }
}
