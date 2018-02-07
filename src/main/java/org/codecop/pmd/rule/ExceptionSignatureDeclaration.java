package org.codecop.pmd.rule;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTConstructorDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTImportDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import net.sourceforge.pmd.properties.BooleanProperty;

/**
 * Changed Exception Signature rule to allow also Spring mock.
 * @author PMD 3.1 - Copied from PMD 3.1 Source Edition.
 * @author PMD 3.7 - updated
 * @author <a href="http://www.code-cop.org/">Peter Kofler</a>
 * @see net.sourceforge.pmd.lang.java.rule.strictexception.SignatureDeclareThrowsExceptionRule
 */
public class ExceptionSignatureDeclaration extends AbstractJavaRule {

   private static final List<String> ALLOWED_METHODS = Arrays.asList(new String[] { "setUp", "tearDown", "onSetUp", "onTearDown", });

   private boolean ignoreTests;
   private boolean junitImported;

   private static final BooleanProperty IGNORE_TESTS_DESCRIPTOR = new BooleanProperty("ignoreTests", "Ignore test methods", false, 1.0f);

   public ExceptionSignatureDeclaration() {
      // definePropertyDescriptor(IGNORE_TESTS_DESCRIPTOR);
      // defined in XML for better readability
   }

   @Override
   public Object visit(ASTCompilationUnit node, Object o) {
      junitImported = false;
      ignoreTests = getProperty(IGNORE_TESTS_DESCRIPTOR);
      return super.visit(node, o);
   }

   //   @Override
   //   public Object visit(ASTPackageDeclaration node, Object o) {
   //      // allow any classes which reside in  a .junit. or .httpunit package
   //      ASTName packageName = node.getFirstChildOfType(ASTName.class);
   //      if (packageName != null && (packageName.getImage().endsWith(".junit") || packageName.getImage().endsWith(".httpunit"))) {
   //         junitImported = true;
   //      }
   //      return super.visit(node, o);
   //   }

   @Override
   public Object visit(ASTImportDeclaration node, Object o) {
      final String importName = node.getImportedName();
      if (importName.startsWith("junit.") || // JUnit 3.x
            importName.startsWith("org.junit.") || // JUnit 4.x
            importName.indexOf(".httpunit.") != -1 || //
            importName.startsWith("org.springframework.test.")) { // Spring Mock
         junitImported = true;
      }
      return super.visit(node, o);
   }

   @Override
   public Object visit(ASTMethodDeclaration methodDeclaration, Object o) {
      final String methodName = methodDeclaration.getMethodName();
      if (ALLOWED_METHODS.contains(methodName) && junitImported) {
         return super.visit(methodDeclaration, o);
      }
      if (ignoreTests && methodName.startsWith("test")) {
         return super.visit(methodDeclaration, o);
      }

      List<ASTName> exceptionList = methodDeclaration.findDescendantsOfType(ASTName.class);
      if (!exceptionList.isEmpty()) {
         evaluateExceptions(exceptionList, o);
      }
      return super.visit(methodDeclaration, o);
   }

   @Override
   public Object visit(ASTConstructorDeclaration constructorDeclaration, Object o) {
       List<ASTName> exceptionList = constructorDeclaration.findDescendantsOfType(ASTName.class);
       if (!exceptionList.isEmpty()) {
           evaluateExceptions(exceptionList, o);
       }
       return super.visit(constructorDeclaration, o);
   }

   /**
    * Checks all exceptions for possible violation on the exception declaration.
    * @param exceptionList containing all exception for declaration
    */
   private void evaluateExceptions(List<ASTName> exceptionList, Object context) {
       for (ASTName exception: exceptionList) {
           if (hasDeclaredExceptionInSignature(exception)) {
            // --- PMD 3.1
            // context.getReport().addRuleViolation(createRuleViolation(context, exception));
            // --- PMD 3.7
               addViolation(context, exception);
            }
        }
    }

   /**
    * Checks if the given value is defined as <code>Exception</code> and the parent is either a method or constructor
    * declaration.
    * @param exception to evaluate
    * @return true if <code>Exception</code> is declared and has proper parents
    */
   private boolean hasDeclaredExceptionInSignature(ASTName exception) {
       return exception.hasImageEqualTo("Exception") && isParentSignatureDeclaration(exception);
   }

   /**
    * @param exception to evaluate
    * @return true if parent node is either a method or constructor declaration
    */
   private boolean isParentSignatureDeclaration(ASTName exception) {
       Node parent = exception.jjtGetParent().jjtGetParent();
       return parent instanceof ASTMethodDeclaration || parent instanceof ASTConstructorDeclaration;
   }

}
