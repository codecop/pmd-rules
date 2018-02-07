package org.codecop.pmd.rule;

import java.util.List;

import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMemberValuePair;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTNormalAnnotation;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryExpression;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryPrefix;
import net.sourceforge.pmd.lang.java.ast.ASTStatementExpression;
import net.sourceforge.pmd.lang.java.rule.AbstractJUnitRule;

/**
 * JUnit tests should include at least one assertion (assert, fail or verify). 
 * This makes the tests more robust, and using assert with messages provide 
 * the developer a clearer idea of what the test does.
 * @author PMD 4.1 - Copied from PMD 4.1 Source Edition.
 * @author PMD 4.3 - updated
 * @author PMD 5.1 - updated
 * @see net.sourceforge.pmd.lang.java.rule.junit.JUnitTestsShouldIncludeAssertRule
 */
public class JUnitTestsShouldIncludeAssertOrVerify extends AbstractJUnitRule {

    @Override
    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
        if (node.isInterface()) {
            return data;
        }
        return super.visit(node, data);
    }

    @Override
    public Object visit(ASTMethodDeclaration method, Object data) {
        if (isJUnitMethod(method, data)) {
            if (!containsAssertOrFailOrVerify(method.getBlock()) &&
                    !containsTestAnnotationWithExpectedException(method.jjtGetParent())) {
                addViolation(data, method);
            }
        }
        return data;
    }

    private boolean containsAssertOrFailOrVerify(Node n) {
        if (n instanceof ASTStatementExpression) {
            if (isAssertOrFailOrVerifyStatement((ASTStatementExpression) n)) {
                return true;
            }
        }
        for (int i = 0; i < n.jjtGetNumChildren(); i++) {
            Node c = n.jjtGetChild(i);
            if (containsAssertOrFailOrVerify(c)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsTestAnnotationWithExpectedException(Node methodParent) {
        List<ASTNormalAnnotation> annotations = methodParent.findDescendantsOfType(ASTNormalAnnotation.class);
        for (ASTNormalAnnotation annotation : annotations) {
            ASTName name = annotation.getFirstChildOfType(ASTName.class);
            if (name != null && ("Test".equals(name.getImage())
                    || (name.getType() != null && name.getType().equals(JUNIT4_CLASS_NAME)))) {
                List<ASTMemberValuePair> memberValues = annotation.findDescendantsOfType(ASTMemberValuePair.class);
                
                for (ASTMemberValuePair pair : memberValues) {
                    if ("expected".equals(pair.getImage())) {
                        return true;
                    }
                }
                
            }
        }
        return false;
    }
    
    private boolean isAssertOrFailOrVerifyStatement(ASTStatementExpression expression) {
        if (expression != null && 
                expression.jjtGetNumChildren() > 0 &&
                expression.jjtGetChild(0) instanceof ASTPrimaryExpression) {
            ASTPrimaryExpression pe = (ASTPrimaryExpression) expression.jjtGetChild(0);
            if (pe.jjtGetNumChildren()> 0 && pe.jjtGetChild(0) instanceof ASTPrimaryPrefix) {
                ASTPrimaryPrefix pp = (ASTPrimaryPrefix) pe.jjtGetChild(0);
                if (pp.jjtGetNumChildren()>0 && pp.jjtGetChild(0) instanceof ASTName) {
                    
                    String img = ((ASTName) pp.jjtGetChild(0)).getImage();                                              
                    if (img != null && isAssertOrFailOrVerifyMethodName(img)) {                  
                        return true;
                    }
                    
                }
            }
        }
        return false;
    }

    private boolean isAssertOrFailOrVerifyMethodName(String img) {
        return img.startsWith("assert") || img.startsWith("fail") || // static import
               img.startsWith("Assert.assert") || img.startsWith("Assert.fail") || // plain JUnit
               img.startsWith("Mockito.verify") || // Mockito
               img.startsWith("verify") || img.contains(".expect") || // static import
               img.startsWith("Assertions.assert"); // Fest and AssertJ
    }

}
