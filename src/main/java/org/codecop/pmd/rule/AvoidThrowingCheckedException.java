package org.codecop.pmd.rule;

import net.sourceforge.pmd.AbstractRule;
import net.sourceforge.pmd.ast.ASTAllocationExpression;
import net.sourceforge.pmd.ast.ASTClassOrInterfaceType;

/**
 * Avoid throwing checked Exceptions - it's considered noise.
 * @author <a href="http://www.code-cop.org/">Peter Kofler</a>
 */
public class AvoidThrowingCheckedException extends AbstractRule {

   @Override
   public Object visit(ASTAllocationExpression node, Object data) {
      //AllocationExpression/ClassOrInterfaceType[@Image='NullPointerException']
      if (node.jjtGetNumChildren() != 0 && node.jjtGetChild(0).getClass().equals(ASTClassOrInterfaceType.class)) {
         ASTClassOrInterfaceType type = (ASTClassOrInterfaceType) node.jjtGetChild(0);
         if (isClassOrSubClass(type, Throwable.class)) {

            if (!isClassOrSubClass(type, RuntimeException.class) && !isClassOrSubClass(type, Error.class)) {
               addViolation(data, node);
            }
         }
      }

      return super.visit(node, data);
   }

   private boolean isClassOrSubClass(ASTClassOrInterfaceType type, Class<?> ofClass) {
      Class<?> typeClass = type.getType();
      if (typeClass == null) {
         // no type resolution, is it the class itself?
         return ofClass.getName().equals(type.getImage());
      }

      if (ofClass.isAssignableFrom(typeClass)) {
         // with type resolution, is it the class?
         return true;
      }

      return false;
   }

}
