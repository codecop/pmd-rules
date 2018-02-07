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
public class BooleanPropertyAdapter {

    private final String theName;
    private final String theDescription;
    private final Boolean defaultValue;
    private final float theUIOrder;

    private Object property5;
    private Object property6;
    private UnsupportedOperationException failed;

    public BooleanPropertyAdapter(String theName, String theDescription, Boolean defaultValue, float theUIOrder) {
        this.theName = theName;
        this.theDescription = theDescription;
        this.defaultValue = defaultValue;
        this.theUIOrder = theUIOrder;

        this.property5 = createPropertyOfType("net.sourceforge.pmd.lang.rule.properties.BooleanProperty"); // PMD 5.x
        this.property6 = createPropertyOfType("net.sourceforge.pmd.properties.BooleanProperty"); // PMD 6.x
    }

    private Object createPropertyOfType(String propertyClassName) {
        try {

            // return new IntegerProperty(theName, theDescription, min, max, theDefault, theUIOrder);
            Class<?> factory = Class.forName(propertyClassName);
            @SuppressWarnings("rawtypes")
            Class[] argumentTypes = new Class[] { String.class, String.class, Boolean.class, float.class };
            Constructor<?> constructor = factory.getConstructor(argumentTypes);
            Object[] arguments = new Object[] { theName, theDescription, defaultValue, theUIOrder };
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

    public static boolean getProperty(Rule rule, BooleanPropertyAdapter propertyAdapter) {
        Object property = propertyAdapter.getProperty();

        try {

            // return rule.getProperty(property);
            return (Boolean) findGetter(rule).invoke(rule, new Object[] { property });

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
