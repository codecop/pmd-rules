package org.codecop.pmd.rule;

import java.util.concurrent.ConcurrentHashMap;

import org.codecop.pmd.versions.IntegerPropertyAdapter;

import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTPackageDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTTypeDeclaration;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

/**
 * Look for number of classes per package.
 * 
 * @author <a href="https://www.code-cop.org/">Peter Kofler</a>
 */
public class ExcessiveClassCount extends AbstractJavaRule {

    private static final IntegerPropertyAdapter CLASSES_BY_PACKAGE_DESCRIPTOR = new IntegerPropertyAdapter("maxClasses",
            "Maximum allowed classes in package.", 1, 999, 10, 1.0F);

    private static final GlobalCounter sharedCounter = new GlobalCounter();
    // maybe not necessary to have a concurrent counter because rules could be single threaded. 
    // need to read more about how it runs in parallel.

    private String packageName;
    private int classesInUnit;

    @Override
    public Object visit(ASTCompilationUnit node, Object data) {
        packageName = "";
        classesInUnit = 0;
        Object visit = super.visit(node, data);

        if (classesInUnit > 0) {
            sharedCounter.add(packageName, classesInUnit);

            int maxClasses = IntegerPropertyAdapter.getProperty(this, CLASSES_BY_PACKAGE_DESCRIPTOR);
            if (sharedCounter.get(packageName) > maxClasses) {
                this.addViolation(data, node, packageName);
            }
        }

        return visit;
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

class GlobalCounter {

    private final ConcurrentHashMap<String, Integer> countByPackage = new ConcurrentHashMap<>();

    public void add(String key, int count) {
        countByPackage.putIfAbsent(key, 0);
        while (true) {
            int prevCount = get(key);
            if (countByPackage.replace(key, prevCount, prevCount + count)) {
                break;
            }
        }
    }

    public int get(String key) {
        return countByPackage.get(key);
    }

}