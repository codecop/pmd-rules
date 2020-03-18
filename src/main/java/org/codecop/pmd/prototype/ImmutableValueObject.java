package org.codecop.pmd.prototype;

import java.util.List;

import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTExtendsList;
import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTPrimitiveType;
import net.sourceforge.pmd.lang.java.ast.ASTType;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

import org.jaxen.JaxenException;

/**
 * ValueObjects must not define RelationShips and be immutable.
 *
 * @author <a href="https://www.code-cop.org/">Peter Kofler</a>
 */
public class ImmutableValueObject extends AbstractJavaRule {

   private static final String FIND_SETTER = "count(./Block/BlockStatement)=1 and " //
         + "./MethodDeclarator[starts-with(@Image,'set')] and " //
         + "./MethodDeclarator/FormalParameters[count(FormalParameter)=1] and " //
         + "./ResultType[count(Type)=0] and " //
         + "./Block/BlockStatement/Statement/StatementExpression/Expression/PrimaryExpression[count(PrimarySuffix/Arguments)=0]";
   private boolean valueObject;

   @Override
   public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
      if (valueObject) {
         return super.visit(node, data);
      }
      valueObject = isClassOrSubClass(node, "org.codecop.pmd.prototype.domain.base.ValueObject");
      return super.visit(node, data);
   }

   // entity-determination copied from SignatureDeclareThrowsException rule
   private boolean isClassOrSubClass(ASTClassOrInterfaceDeclaration node, String className) {
      // check implemented interfaces
      // ASTImplementsList impl = node.getFirstChildOfType(ASTImplementsList.class);
      // if (impl != null && impl.jjtGetParent().equals(node)) {
      //    for (int ix = 0; ix < impl.jjtGetNumChildren(); ix++) {
      //       ASTClassOrInterfaceType type = (ASTClassOrInterfaceType) impl.jjtGetChild(ix);
      //       if (isClassOrSubClass(type, className)) {
      //          return true;
      //       }
      //    }
      // }

      // check superclass
      if (node.jjtGetNumChildren() != 0 && node.jjtGetChild(0).getClass().equals(ASTExtendsList.class)) {
         ASTClassOrInterfaceType type = (ASTClassOrInterfaceType) (node.jjtGetChild(0)).jjtGetChild(0);
         if (isClassOrSubClass(type, className)) {
            return true;
         }
      }

      return false;
   }

   private boolean isClassOrSubClass(ASTClassOrInterfaceType type, String className) {
      Class<?> clazz = type.getType();
      if (clazz == null) {
         // no type resolution, is it the class itself?
         return className.equals(type.getImage());
      }

      if (clazz.getName().equals(className)) {
         // with type resolution, is it the class?
         return true;
      }

      // List<Class> implementors = Arrays.asList(type.getType().getInterfaces());
      // if (implementors.contains(x.class)) {
      //    return true;
      // }

      while (clazz != null && !Object.class.equals(clazz)) {
         if (clazz.getName().equals(className)) {
            return true;
         }
         // for (Class<?> intf : clazz.getInterfaces()) {
         //    if (intf.getName().equals(className)) {
         //       return true;
         //    }
         // }
         clazz = clazz.getSuperclass();
      }

      return false;
   }

   @Override
   public Object visit(ASTFieldDeclaration node, Object data) {
      if (valueObject) {

         // not private
         if (!node.isPrivate()) {
            addViolation(data, node);
         }

         // contains RelationShip
         ASTType fieldType = node.getFirstChildOfType(ASTType.class);
         if (fieldType != null && !fieldType.isArray() && !fieldType.hasDescendantOfType(ASTPrimitiveType.class)) {
            ASTClassOrInterfaceType refType = fieldType.getFirstDescendantOfType(ASTClassOrInterfaceType.class);
            if (isClassOrSubClass(refType, "org.codecop.pmd.prototype.domain.base.RelationShip")) {
               addViolation(data, node);
            }
         }

      }
      return super.visit(node, data);
   }

   @Override
   public Object visit(ASTMethodDeclaration node, Object data) {
      if (valueObject && node.isPublic()) {

         try {
            List<?> nodes = node.findChildNodesWithXPath(FIND_SETTER);
            if (nodes != null && nodes.size() > 0) {
               addViolation(data, node);
            }
         } catch (JaxenException e) {
            // XPath is valid, this should never happens...
            throw new IllegalArgumentException(FIND_SETTER, e);
         }

      }
      return super.visit(node, data);
   }
}
