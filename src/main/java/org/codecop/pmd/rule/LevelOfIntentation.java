package org.codecop.pmd.rule;

import org.codecop.pmd.versions.IntegerPropertyAdapter;

import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTDoStatement;
import net.sourceforge.pmd.lang.java.ast.ASTForStatement;
import net.sourceforge.pmd.lang.java.ast.ASTIfStatement;
import net.sourceforge.pmd.lang.java.ast.ASTSwitchStatement;
import net.sourceforge.pmd.lang.java.ast.ASTTryStatement;
import net.sourceforge.pmd.lang.java.ast.ASTWhileStatement;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

/**
 * Avoid deep nesting.
 * 
 * @author PMD 5.0 - Partly copied from PMD 5.0 source.
 * @author <a href="http://www.code-cop.org/">Peter Kofler</a>
 * @see net.sourceforge.pmd.lang.java.rule.design.AvoidDeeplyNestedIfStmtsRule
 */

public class LevelOfIntentation extends AbstractJavaRule {
    private static final IntegerPropertyAdapter PROBLEM_DEPTH_DESCRIPTOR = new IntegerPropertyAdapter("problemDepth", "Maximum allowed nesting depth.", 1, 25, 1, 1.0F);

    private int depth;
    private int depthLimit;

    @Override
    public Object visit(ASTCompilationUnit node, Object data) {
        this.depth = 0;
        this.depthLimit = IntegerPropertyAdapter.getProperty(this, PROBLEM_DEPTH_DESCRIPTOR);
        return super.visit(node, data);
    }

    private void enter(Node node, Object data) {
        this.depth += 1;
        if (this.depth > this.depthLimit) {
            addViolation(data, node);
        }
    }

    private void exit() {
        this.depth -= 1;
    }

    @Override
    public Object visit(ASTIfStatement node, Object data) {
        enter(node, data);
        super.visit(node, data);
        exit();
        return data;
    }

    @Override
    public Object visit(ASTWhileStatement node, Object data) {
        enter(node, data);
        super.visit(node, data);
        exit();
        return data;
    }

    @Override
    public Object visit(ASTSwitchStatement node, Object data) {
        enter(node, data);
        super.visit(node, data);
        exit();
        return data;
    }

    @Override
    public Object visit(ASTTryStatement node, Object data) {
        enter(node, data);
        super.visit(node, data);
        exit();
        return data;
    }

    @Override
    public Object visit(ASTForStatement node, Object data) {
        enter(node, data);
        super.visit(node, data);
        exit();
        return data;
    }

    @Override
    public Object visit(ASTDoStatement node, Object data) {
        enter(node, data);
        super.visit(node, data);
        exit();
        return data;
    }
}