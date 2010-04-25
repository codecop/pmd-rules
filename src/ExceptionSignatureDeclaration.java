import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.pmd.AbstractRule;
import net.sourceforge.pmd.ast.ASTCompilationUnit;
import net.sourceforge.pmd.ast.ASTConstructorDeclaration;
import net.sourceforge.pmd.ast.ASTImportDeclaration;
import net.sourceforge.pmd.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.ast.ASTName;
import net.sourceforge.pmd.ast.ASTPackageDeclaration;
import net.sourceforge.pmd.ast.Node;
/**
 * Changed Exception Signature rule to allow everything in .junit and .httpunit packages 
 * for setup, teardown and test methods.
 * Copied from PMD 3.1 Source Edition in package <code>net.sourceforge.pmd.rules.strictexception</code>.
 * @author PMD 3.7
 */
public class ExceptionSignatureDeclaration extends AbstractRule
{
   private final List allowedMethods = Arrays.asList(new String[] { "setUp", "oneTimeSetUp", "tearDown" });

   private boolean junitImported;

   public Object visit(ASTCompilationUnit node, Object o)
   {
      junitImported = false;
      return super.visit(node, o);
   }

   public Object visit(ASTPackageDeclaration node, Object o)
   {
      // allow any classes which reside in  a .junit. or .httpunit package too
      ASTName packageName = (ASTName) node.getFirstChildOfType(ASTName.class);
      if (packageName != null && (packageName.getImage().endsWith(".junit") || packageName.getImage().endsWith(".httpunit")))
      {
         junitImported = true;
      }
      return super.visit(node, o);
   }

   public Object visit(ASTImportDeclaration node, Object o)
   {
      final String importName = node.getImportedName();
      if (importName.indexOf(".junit.") != -1 || importName.indexOf(".httpunit.") != -1)
      {
         junitImported = true;
      }
      return super.visit(node, o);
   }

   public Object visit(ASTMethodDeclaration methodDeclaration, Object o)
   {
      final String methodName = methodDeclaration.getMethodName();
      if ((allowedMethods.contains(methodName) || methodName.startsWith("test")) && junitImported)
      {
         return super.visit(methodDeclaration, o);
      }

      List exceptionList = methodDeclaration.findChildrenOfType(ASTName.class);
      if (!hasContent(exceptionList))
      {
         return super.visit(methodDeclaration, o);
      }

      evaluateExceptions(exceptionList, o);
      return super.visit(methodDeclaration, o);
   }

   public Object visit(ASTConstructorDeclaration constructorDeclaration, Object o)
   {
      List exceptionList = constructorDeclaration.findChildrenOfType(ASTName.class);
      if (!hasContent(exceptionList))
      {
         return super.visit(constructorDeclaration, o);
      }

      evaluateExceptions(exceptionList, o);
      return super.visit(constructorDeclaration, o);
   }

   /**
    * Checks all exceptions for possible violation on the exception declaration.
    *
    * @param exceptionList containing all exception for declaration
    * @param context
    */
   private void evaluateExceptions(List exceptionList, Object context)
   {
      ASTName exception;
      for (Iterator iter = exceptionList.iterator(); iter.hasNext();)
      {
         exception = (ASTName) iter.next();
         if (hasDeclaredExceptionInSignature(exception))
         {
            // PMD 3.1
            // context.getReport().addRuleViolation(createRuleViolation(context, exception));
            // PMD 3.7
            addViolation(context, exception);
         }
      }
   }

   /**
    * Checks if the given value is defined as <code>Exception</code> and the parent is either
    * a method or constructor declaration.
    *
    * @param exception to evaluate
    * @return true if <code>Exception</code> is declared and has proper parents
    */
   private boolean hasDeclaredExceptionInSignature(ASTName exception)
   {
      return exception.getImage().equals("Exception") && isParentSignatureDeclaration(exception);
   }

   /**
    * @param exception to evaluate
    * @return true if parent node is either a method or constructor declaration
    */
   private boolean isParentSignatureDeclaration(ASTName exception)
   {
      Node parent = exception.jjtGetParent().jjtGetParent();
      return parent instanceof ASTMethodDeclaration || parent instanceof ASTConstructorDeclaration;
   }

   private boolean hasContent(List nameList)
   {
      return nameList != null && nameList.size() > 0;
   }

}
