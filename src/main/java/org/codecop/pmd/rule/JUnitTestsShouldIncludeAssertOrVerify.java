package org.codecop.pmd.rule;

import net.sourceforge.pmd.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.ast.ASTName;
import net.sourceforge.pmd.ast.ASTPrimaryExpression;
import net.sourceforge.pmd.ast.ASTPrimaryPrefix;
import net.sourceforge.pmd.ast.ASTStatementExpression;
import net.sourceforge.pmd.ast.Node;
import net.sourceforge.pmd.rules.junit.AbstractJUnitRule;

/**
 * JUnit tests should include at least one assertion (assert, fail or verify). 
 * This makes the tests more robust, and using assert with messages provide 
 * the developer a clearer idea of what the test does.
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
            if (!containsAssert(method.getBlock(), false)) {
                addViolation(data, method);
            }
        }
        return data;
    }

    private boolean containsAssert(Node n, boolean assertFound) {
        if (!assertFound) {
            if (n instanceof ASTStatementExpression) {
                if (isAssertOrFailStatement((ASTStatementExpression) n)) {
                    return true;
                }
            }
            if (!assertFound) {
                for (int i = 0; i < n.jjtGetNumChildren() && !assertFound; i++) {
                    Node c = n.jjtGetChild(i);
                    if (containsAssert(c, assertFound)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Tells if the expression is an assert/verify statement or not.
     */
    private boolean isAssertOrFailStatement(ASTStatementExpression expression) {
        if (expression != null && 
                expression.jjtGetNumChildren() > 0 &&
                expression.jjtGetChild(0) instanceof ASTPrimaryExpression
            ) {
            ASTPrimaryExpression pe = (ASTPrimaryExpression) expression.jjtGetChild(0);
            if (pe.jjtGetNumChildren()> 0 && pe.jjtGetChild(0) instanceof ASTPrimaryPrefix) {
                ASTPrimaryPrefix pp = (ASTPrimaryPrefix) pe.jjtGetChild(0);
                if (pp.jjtGetNumChildren()>0 && pp.jjtGetChild(0) instanceof ASTName) {
                    String img = ((ASTName) pp.jjtGetChild(0)).getImage();                                              
                    if (img != null && (img.startsWith("assert") || img.startsWith("fail") ||
                                        img.startsWith("Assert.assert") || img.startsWith("Assert.fail") ||
                                        img.startsWith("Mockito.verify") || img.startsWith("verify")|| 
                                        img.contains(".expect"))) {                  
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
