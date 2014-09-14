package org.codecop.pmd.rule;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTConstructorDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTFormalParameter;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclarator;
import net.sourceforge.pmd.lang.java.ast.ASTType;
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
            "String", //
            "List", "Map", "Set" //
    );

    private boolean allowObject;
    private boolean wrongTypesDetected;

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
        resetTypeCheck();

        Object visit = super.visit(constructorDeclaration, context);

        boolean isPublic = constructorDeclaration.isPublic();
        boolean moreThanOneArguments = constructorDeclaration.getParameterCount() >= 2;
        addViolationOnPrimitiveParameter(isPublic && moreThanOneArguments, context, constructorDeclaration);

        return visit;
    }

    /**
     * The methods do not allow any primitive arguments. Primitive return values are allowed if there are no arguments.
     * This is true for hashCode, toString and plain getters.
     */
    @Override
    public Object visit(ASTMethodDeclaration methodDeclaration, Object context) {
        resetTypeCheck();

        Object visit = super.visit(methodDeclaration, context);

        checkForPrimitiveReturnType(methodDeclaration);

        boolean isPublic = methodDeclaration.isPublic();
        addViolationOnPrimitiveParameter(isPublic, context, methodDeclaration);

        return visit;
    }

    private void resetTypeCheck() {
        wrongTypesDetected = false;
    }

    private void checkForPrimitiveReturnType(ASTMethodDeclaration methodDeclaration) {
        boolean hasReturnValue = !methodDeclaration.isVoid();
        boolean hasParameters = methodDeclaration.getFirstChildOfType(ASTMethodDeclarator.class).getParameterCount() > 0;
        if (hasReturnValue && hasParameters) {
            ASTType returnType = methodDeclaration.getResultType().getFirstChildOfType(ASTType.class);
            checkForPrimitive(returnType);
        }
    }

    private void addViolationOnPrimitiveParameter(boolean condition, Object context, Node declaration) {
        if (condition && wrongTypesDetected) {
            addViolation(context, declaration);
        }
    }

    @Override
    public Object visit(ASTFormalParameter formalParameter, Object context) {
        checkForPrimitiveParameter(formalParameter);
        return super.visit(formalParameter, context);
    }

    private void checkForPrimitiveParameter(ASTFormalParameter formalParameter) {
        ASTType parameterType = formalParameter.getTypeNode();
        checkForPrimitive(parameterType);
    }

    private void checkForPrimitive(ASTType type) {
        String parameterType = type.getTypeImage();
        checkForPrimitive(parameterType);
        if (parameterType.startsWith("java.lang.")) {
            checkForPrimitive(parameterType.substring(10));
        }
        if (parameterType.startsWith("java.util.")) {
            checkForPrimitive(parameterType.substring(10));
        }
    }

    private void checkForPrimitive(String parameterType) {
        if (isForbidden(parameterType)) {
            wrongTypesDetected = true;
        }
    }

    private boolean isForbidden(String type) {
        return FORBIDDEN_TYPES.contains(type) || isForbiddenObject(type);
    }

    private boolean isForbiddenObject(String type) {
        return !allowObject && "Object".equals(type);
    }

}
