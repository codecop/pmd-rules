package org.codecop.pmd;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import net.sourceforge.pmd.Rule;

/**
 * Wrapper around changed API used in rules. <br>
 * 
 * @author PMD 6.0
 * @author <a href="http://www.code-cop.org/">Peter Kofler</a>
 */
public class IntegerPropertyAdapter {

    private final String theName;
    private final String theDescription;
    private final Integer min;
    private final Integer max;
    private final Integer theDefault;
    private final float theUIOrder;

    private Object property5;
    private Object property6;
    private UnsupportedOperationException failed;

    public IntegerPropertyAdapter(String theName, String theDescription, Integer min, Integer max, Integer theDefault, float theUIOrder) {
        this.theName = theName;
        this.theDescription = theDescription;
        this.min = min;
        this.max = max;
        this.theDefault = theDefault;
        this.theUIOrder = theUIOrder;

        this.property5 = createPropertyOfType("net.sourceforge.pmd.lang.rule.properties.IntegerProperty"); // PMD 5.x
        this.property6 = createPropertyOfType("net.sourceforge.pmd.properties.IntegerProperty"); // PMD 6.x
    }

    private Object createPropertyOfType(String propertyClassName) {
        try {

            // return new IntegerProperty(theName, theDescription, min, max, theDefault, theUIOrder);
            Class<?> factory = Class.forName(propertyClassName);
            @SuppressWarnings("rawtypes")
            Class[] argumentTypes = new Class[] { String.class, String.class, Integer.class, Integer.class, Integer.class, float.class };
            Constructor<?> constructor = factory.getConstructor(argumentTypes);
            Object[] arguments = new Object[] { theName, theDescription, min, max, theDefault, theUIOrder };
            return constructor.newInstance(arguments);

        } catch (Exception reflectionFailed) {
            String combinedMessage = "PMD API:\n" + reflectionFailed.getMessage();
            this.failed = new UnsupportedOperationException(combinedMessage, reflectionFailed);
            return null;
        }
    }

    private Object getProperty() {
        if (property5 != null) {
            return property5;
        }
        if (property6 != null) {
            return property6;
        }
        throw failed;
    }

    public static int getProperty(Rule rule, IntegerPropertyAdapter propertyAdapter) {
        Object property = propertyAdapter.getProperty();

        try {

            // return rule.getProperty(property);
            return (Integer) findGetter(rule).invoke(rule, new Object[] { property });

        } catch (Exception reflectionFailed) {
            String combinedMessage = "PMD API:\n" + reflectionFailed.getMessage();
            UnsupportedOperationException failed = new UnsupportedOperationException(combinedMessage, reflectionFailed);
            throw failed;
        }
    }

    private static Method findGetter(Rule rule) throws NoSuchMethodException {
        // return rule.getProperty(property);
        for (Method method : rule.getClass().getMethods()) {
            if (method.getName().equals("getProperty") && method.getParameterTypes().length == 1) {
                return method;
            }
        }
        throw new NoSuchMethodException("getProperty");
    }

}
