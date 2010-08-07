package org.codecop.pmd.rule;

import java.util.Map;

import net.sourceforge.pmd.AbstractRule;
import net.sourceforge.pmd.PropertyDescriptor;
import net.sourceforge.pmd.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.properties.IntegerProperty;

/**
 * Changed Class name rule to allow underscore before _Stub and _Core (RMI). Added some additional tests.
 * @author PMD 3.1 - Copied from PMD 3.1 Source Edition.
 * @author PMD 3.7 - updated
 * @author <a href="http://www.code-cop.org/">Peter Kofler</a>
 */
public class ClassNamingConventions extends AbstractRule {

   private int upperCaseLen;

   private static final PropertyDescriptor UPPERCASE_LEN_DESCRIPTOR = new IntegerProperty("upperCaseLen", "Allowed length of upper case only names", 3, 1.0f);
   private static final Map<String, PropertyDescriptor> PROPERTY_DESCRIPTORS_BY_NAME = asFixedMap(new PropertyDescriptor[] { UPPERCASE_LEN_DESCRIPTOR });

   @Override
   protected Map<String, PropertyDescriptor> propertiesByName() {
      return PROPERTY_DESCRIPTORS_BY_NAME;
   }

   @Override
   public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
      upperCaseLen = getIntProperty(UPPERCASE_LEN_DESCRIPTOR);

      final String className = node.getImage();

      // first letter must be upper case letter
      if (Character.isLowerCase(className.charAt(0)) || !Character.isLetter(className.charAt(0))) {
         // --- PMD 3.1
         // RuleContext ctx = (RuleContext) data;
         // ctx.getReport().addRuleViolation(createRuleViolation(ctx, node, getMessage()));
         // --- PMD 3.7
         addViolation(data, node);
      }

      // must not be uppercase only
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
