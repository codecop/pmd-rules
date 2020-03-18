package org.codecop.pmd.rule;

import java.util.Arrays;
import java.util.List;

import org.codecop.pmd.versions.BooleanPropertyAdapter;

import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTConstructorDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTFormalParameter;
import net.sourceforge.pmd.lang.java.ast.ASTImplementsList;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclarator;
import net.sourceforge.pmd.lang.java.ast.ASTType;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

/**
 * Look for usages of primitives other than in value object/wrappers.
 * 
 * @author <a href="https://www.code-cop.org/">Peter Kofler</a>
 */
public class PrimitiveObsession extends AbstractJavaRule {

    private static final BooleanPropertyAdapter ALLOW_OBJECT = new BooleanPropertyAdapter("allowObject", "Allow plain java.lang.Object", true, 1.0f);
    private static final BooleanPropertyAdapter CHECK_CONSTRUCTORS = new BooleanPropertyAdapter("checkConstructors", "Check public constructors for more than one primitive", false, 1.0f);

    private static final List<String> FORBIDDEN_TYPES = Arrays.asList( //
            "char", "Character", // 
            "byte", "Byte", "short", "Short", "int", "Integer", "AtomicInteger", "long", "Long", "AtomicLong", //  
            "float", "Float", "double", "Double", "Number", //
            "boolean", "Boolean", "AtomicBoolean",  //
            "BigInteger", "java.math.BigInteger", "BigDecimal", "java.math.BigDecimal",

            "String", "StringBuffer", "StringBuilder", //
            
            "Collection", //
            "List", "ArrayList", "Map", "HashMap", "SortedMap", "TreeMap", "Set", "HashSet", "SortedSet", "TreeSet", 
            "Queue", "Deque" // 
    );

    private boolean allowObject;
    private boolean checkConstructors;
    private boolean wrongTypesDetected;
    private boolean isComparator;

    @Override
    public Object visit(ASTCompilationUnit node, Object context) {
        configure();
        isComparator = false;
        return super.visit(node, context);
    }

    private void configure() {
        allowObject = BooleanPropertyAdapter.getProperty(this, ALLOW_OBJECT);
        checkConstructors = BooleanPropertyAdapter.getProperty(this, CHECK_CONSTRUCTORS);
    }

    @Override
    public Object visit(ASTImplementsList implementsList, Object context) {
        for (int i = 0; i < implementsList.jjtGetNumChildren(); i++) {
            Node child = implementsList.jjtGetChild(i);
            if (child instanceof ASTClassOrInterfaceType) {
                if ("Comparator".equals(child.getImage())) {
                    isComparator = true;
                    break;
                }
            }
        }
        return super.visit(implementsList, context);
    }
    
    /**
     * The constructor allows primitive types if they are the only parameters. This is to set the internal value.
     */
    @Override
    public Object visit(ASTConstructorDeclaration constructorDeclaration, Object context) {
        resetTypeCheck();

        Object visit = super.visit(constructorDeclaration, context);

        boolean isPublic = constructorDeclaration.isPublic();
        boolean tooManyArguments = constructorDeclaration.getParameterCount() > 1;
        boolean badConstructor = isPublic && tooManyArguments;
        addViolationOnPrimitiveParameter(checkConstructors && badConstructor, context, constructorDeclaration);

        return visit;
    }

    /**
     * The methods do not allow any primitive arguments. Primitive return values are allowed if there are no arguments.
     * This is true for hashCode, toString and plain getters. There are extra checks for equals, compareTo and compare methods.
     */
    @Override
    public Object visit(ASTMethodDeclaration methodDeclaration, Object context) {
        resetTypeCheck();

        Object visit = super.visit(methodDeclaration, context);

        if (isEqualsMethod(methodDeclaration)) {
            return visit;
        }

        if (isCompareToMethod(methodDeclaration)) {
            return visit;
        }

        if (isComparator && isComparatorMethod(methodDeclaration)) {
            return visit;
        }
        
        checkForPrimitiveReturnType(methodDeclaration);

        boolean isPublic = methodDeclaration.isPublic();
        addViolationOnPrimitiveParameter(isPublic, context, methodDeclaration);

        return visit;
    }

    private boolean isEqualsMethod(ASTMethodDeclaration methodDeclaration) {
        boolean isPublic = methodDeclaration.isPublic();
        boolean returnsBoolean = !methodDeclaration.isVoid() && returnTypeOf(methodDeclaration).getTypeImage().equals("boolean");
        ASTMethodDeclarator declarator = declaratorOf(methodDeclaration);
        boolean namedEquals = declarator.getImage().equals("equals");
        boolean oneArgument = declarator.getParameterCount() == 1;
        return isPublic && returnsBoolean && namedEquals && oneArgument;
    }

    private boolean isCompareToMethod(ASTMethodDeclaration methodDeclaration) {
        boolean isPublic = methodDeclaration.isPublic();
        boolean returnsInt = !methodDeclaration.isVoid() && returnTypeOf(methodDeclaration).getTypeImage().equals("int");
        ASTMethodDeclarator declarator = declaratorOf(methodDeclaration);
        boolean namedCompareTo = declarator.getImage().equals("compareTo");
        boolean oneArgument = declarator.getParameterCount() == 1;
        return isPublic && returnsInt && namedCompareTo && oneArgument;
    }

    private boolean isComparatorMethod(ASTMethodDeclaration methodDeclaration) {
        boolean isPublic = methodDeclaration.isPublic();
        boolean returnsInt = !methodDeclaration.isVoid() && returnTypeOf(methodDeclaration).getTypeImage().equals("int");
        ASTMethodDeclarator declarator = declaratorOf(methodDeclaration);
        boolean namedCompare = declarator.getImage().equals("compare");
        boolean twoSameArguments = declarator.getParameterCount() == 2;
        return isPublic && returnsInt && namedCompare && twoSameArguments;
    }
    
    private void resetTypeCheck() {
        wrongTypesDetected = false;
    }

    private void checkForPrimitiveReturnType(ASTMethodDeclaration methodDeclaration) {
        boolean hasReturnValue = !methodDeclaration.isVoid();
        boolean hasParameters = declaratorOf(methodDeclaration).getParameterCount() > 0;
        if (hasReturnValue && hasParameters) {
            checkForPrimitive(returnTypeOf(methodDeclaration));
        }
    }

    private ASTMethodDeclarator declaratorOf(ASTMethodDeclaration methodDeclaration) {
        return methodDeclaration.getFirstChildOfType(ASTMethodDeclarator.class);
    }

    private ASTType returnTypeOf(ASTMethodDeclaration methodDeclaration) {
        return methodDeclaration.getResultType().getFirstChildOfType(ASTType.class);
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
        if (parameterType.startsWith("java.util.concurrent.atomic.")) {
            checkForPrimitive(parameterType.substring(28));
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
