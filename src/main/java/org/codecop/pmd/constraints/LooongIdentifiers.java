package org.codecop.pmd.constraints;

import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTEnumConstant;
import net.sourceforge.pmd.lang.java.ast.ASTEnumDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclarator;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTPackageDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclaratorId;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import net.sourceforge.pmd.properties.IntegerProperty;

/**
 * Force all identifiers to be at least that looong.
 * 
 * @author PMD 5.0
 * @author <a href="http://www.code-cop.org/">Peter Kofler</a>
 */
public class LooongIdentifiers extends AbstractJavaRule {

    private static final IntegerProperty IDENTIFIER_MIN_LEN_DESCRIPTOR = new IntegerProperty("identifierMinLen",
            "Minimal length of Identifier names", 1, 99, 20, 1.0f);

    private int minLen;

    @Override
    public Object visit(ASTPackageDeclaration node, Object data) {
        ASTName name = node.getFirstChildOfType(ASTName.class);
        for (String part : name.getImage().split("\\.")) {
            check(part, node, data);
        }
        return super.visit(node, data);
    }

    @Override
    public Object visit(ASTClassOrInterfaceDeclaration node, Object data) {
        check(node.getImage(), node, data);
        return super.visit(node, data);
    }

    @Override
    public Object visit(ASTVariableDeclaratorId node, Object data) {
        // field, parameter and local, probably exception, 
        check(node.getImage(), node, data);
        return super.visit(node, data);
    }

    @Override
    public Object visit(ASTMethodDeclarator node, Object data) {
        check(node.getImage(), node, data);
        return super.visit(node, data);
    }

    @Override
    public Object visit(ASTEnumDeclaration node, Object data) {
        check(node.getImage(), node, data);
        return super.visit(node, data);
    }

    @Override
    public Object visit(ASTEnumConstant node, Object data) {
        check(node.getImage(), node, data);
        return super.visit(node, data);
    }

    private void check(final String name, Node node, Object data) {
        if (minLen == 0) {
            minLen = getProperty(IDENTIFIER_MIN_LEN_DESCRIPTOR);
        }
        if (name.length() < minLen) {
            addViolation(data, node);
        }
    }

}
