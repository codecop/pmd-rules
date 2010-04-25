import net.sourceforge.pmd.AbstractRule;
import net.sourceforge.pmd.ast.ASTClassOrInterfaceDeclaration;
/**
 * Changed Class name rule to allow underscore before _Stub and _Core (RMI). This rule must
 * not be run against the generated SOAP packages. Added some additional tests.
 * Copied from PMD 3.1 Source Edition in package <code>net.sourceforge.pmd.rules</code>.
 * @author PMD 3.7 
 */
public class ClassNamingConventions extends AbstractRule
{

   public Object visit(ASTClassOrInterfaceDeclaration node, Object data)
   {
      final String className = node.getImage();

      // first letter must be upper case letter
      if (Character.isLowerCase(className.charAt(0)) || !Character.isLetter(className.charAt(0)))
      {
         // PMD 3.1
         // RuleContext ctx = (RuleContext) data;
         // ctx.getReport().addRuleViolation(createRuleViolation(ctx, node, getMessage()));
         // PMD 3.7
         addViolation(data, node);
      }

      // must not be uppercase only
      if (className.toUpperCase().equals(className) && className.length() > 6)
      {
         addViolation(data, node);
      }

      // must not contain $ or blank
      else if (className.indexOf('$') >= 0 || className.indexOf(' ') >= 0)
      {
         addViolation(data, node);
      }

      // must not contain underscore but RMI stubs and Core pattern
      else if (className.indexOf("_") >= 0 && !className.endsWith("_Stub") && !className.endsWith("_Core"))
      {
         addViolation(data, node);
      }

      return data;
   }
}
