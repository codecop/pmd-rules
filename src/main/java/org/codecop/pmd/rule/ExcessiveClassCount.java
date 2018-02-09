package org.codecop.pmd.rule;

import org.codecop.pmd.versions.IntegerPropertyAdapter;

import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTPackageDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTTypeDeclaration;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import net.sourceforge.pmd.lang.rule.AbstractRule;

/**
 * Look for number of classes per package.
 * 
 * @author <a href="http://www.code-cop.org/">Peter Kofler</a>
 */
public class ExcessiveClassCount extends AbstractJavaRule {

    private static final IntegerPropertyAdapter CLASSES_BY_PACKAGE_DESCRIPTOR = new IntegerPropertyAdapter("maxClasses",
            "Maximum allowed classes in package.", 1, 999, 10, 1.0F);

    private String packageName;
    private int classesInUnit;

    @Override
    public Object visit(ASTCompilationUnit node, Object data) {
        packageName = "";
        classesInUnit = 0;
        Object visit = super.visit(node, data);

        check(this, packageName, classesInUnit, node, data);

        return visit;
    }

    private static void check(AbstractRule x, String packageName, int classesInUnit, ASTCompilationUnit node, Object data) {
        // check globally
        int maxClasses = IntegerPropertyAdapter.getProperty(x, CLASSES_BY_PACKAGE_DESCRIPTOR);
        if (classesInUnit > maxClasses) {
            x.addViolation(data, node, packageName);
        }
    }

    @Override
    public Object visit(ASTPackageDeclaration node, Object data) {
        packageName = node.getPackageNameImage();
        return super.visit(node, data);
    }

    @Override
    public Object visit(ASTTypeDeclaration node, Object data) {
        classesInUnit += 1;
        return super.visit(node, data);
    }

}
