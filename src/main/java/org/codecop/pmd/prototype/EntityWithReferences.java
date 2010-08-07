package org.codecop.pmd.prototype;

import net.sourceforge.pmd.AbstractRule;
import net.sourceforge.pmd.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.ast.ASTExtendsList;
import net.sourceforge.pmd.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.ast.ASTPrimitiveType;
import net.sourceforge.pmd.ast.ASTType;
import net.sourceforge.pmd.ast.SimpleNode;

/**
 * Entities must define all attributes with RelationShips.
 * @author <a href="http://www.code-cop.org/">Peter Kofler</a>
 */
public class EntityWithReferences extends AbstractRule {

   private boolean entity;

   @Override
   public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
      if (entity) {
         return super.visit(node, data);
      }
      entity = isClassOrSubClass(node, "org.codecop.pmd.prototype.domain.base.Entity");
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
         ASTClassOrInterfaceType type = (ASTClassOrInterfaceType) ((SimpleNode) node.jjtGetChild(0)).jjtGetChild(0);
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
      if (entity) {

         if (!node.isPrivate()) {
            addViolation(data, node);
         }

         ASTType fieldType = node.getFirstChildOfType(ASTType.class);
         if (fieldType != null) {
            if (fieldType.isArray() || fieldType.containsChildOfType(ASTPrimitiveType.class)) {
               addViolation(data, node);

            } else {

               ASTClassOrInterfaceType refType = fieldType.getFirstChildOfType(ASTClassOrInterfaceType.class);
               if (!isClassOrSubClass(refType, "org.codecop.pmd.prototype.domain.base.RelationShip")) {
                  addViolation(data, node);
               }
            }
         }

      }
      return super.visit(node, data);
   }
}
