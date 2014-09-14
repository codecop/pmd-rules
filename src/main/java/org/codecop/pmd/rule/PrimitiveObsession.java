package org.codecop.pmd.rule;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTConstructorDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTFormalParameter;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;
import net.sourceforge.pmd.lang.rule.properties.BooleanProperty;

/**
 * Look for usages of primitives other than in value object/wrappers.
 * 
 * @author PMD 5.0
 * @author <a href="http://www.code-cop.org/">Peter Kofler</a>
 */
public class PrimitiveObsession extends AbstractJavaRule {

    private static final BooleanProperty ALLOW_OBJECT = new BooleanProperty("allowObject", "Allow plain java.lang.Object", true, 1.0f);

    private static final List<String> FORBIDDEN_TYPES = Arrays.asList( //
            "char", "Character", // 
            "byte", "Byte", "short", "Short", "int", "Integer", "long", "Long", //
            "float", "Float", "double", "Double", "Number", //
            "boolean", "Boolean", //
            "String" // 
    );

    private boolean allowObject;
    private boolean wrongParameterDetected;

    @Override
    public Object visit(ASTCompilationUnit node, Object context) {
        configure();
        return super.visit(node, context);
    }

    private void configure() {
        allowObject = getProperty(ALLOW_OBJECT);
    }

    /**
     * The constructor allows primitive types if they are the only parameter. This is to set the internal value.
     */
    @Override
    public Object visit(ASTConstructorDeclaration constructorDeclaration, Object context) {
        resetParameterTypeCheck();

        Object visit = super.visit(constructorDeclaration, context);

        boolean isPublic = constructorDeclaration.isPublic();
        boolean moreThanOneArguments = constructorDeclaration.getParameterCount() >= 2;
        addViolation(isPublic && moreThanOneArguments, context, constructorDeclaration);

        return visit;
    }

    /**
     * The methods do not allow any primitive arguments.
     */
    @Override
    public Object visit(ASTMethodDeclaration methodDeclaration, Object context) {
        resetParameterTypeCheck();

        Object visit = super.visit(methodDeclaration, context);

        boolean isPublic = methodDeclaration.isPublic();
        addViolation(isPublic, context, methodDeclaration);

        return visit;
    }

    private void resetParameterTypeCheck() {
        wrongParameterDetected = false;
    }

    private void addViolation(boolean condition, Object context, Node declaration) {
        if (condition && wrongParameterDetected) {
            addViolation(context, declaration);
        }
    }

    @Override
    public Object visit(ASTFormalParameter formalParameter, Object context) {
        checkForPrimitive(formalParameter);
        return super.visit(formalParameter, context);
    }

    private void checkForPrimitive(ASTFormalParameter formalParameter) {
        String parameterType = formalParameter.getTypeNode().getTypeImage();
        checkForPrimitive(parameterType);
        if (parameterType.startsWith("java.lang.")) {
            checkForPrimitive(parameterType.substring(10));
        }
    }

    private void checkForPrimitive(String parameterType) {
        if (isForbidden(parameterType)) {
            wrongParameterDetected = true;
        }
    }

    private boolean isForbidden(String type) {
        return FORBIDDEN_TYPES.contains(type) || isForbiddenObject(type);
    }

    private boolean isForbiddenObject(String type) {
        return !allowObject && "Object".equals(type);
    }

}
