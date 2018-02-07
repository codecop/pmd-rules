package org.codecop.pmd.versions;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import net.sourceforge.pmd.Rule;

public class PropertyAdapter {

    protected Object property5;
    protected Object property6;
    private UnsupportedOperationException failed;

    @SuppressWarnings("rawtypes")
    protected Object newInstance(String propertyClassName, Class[] argumentTypes, Object[] arguments) {
        try {

            Class<?> factory = Class.forName(propertyClassName);
            Constructor<?> constructor = factory.getConstructor(argumentTypes);
            return constructor.newInstance(arguments);

        } catch (Exception reflectionFailed) {
            setFailed(reflectionFailed);
            return null;
        }
    }

    private void setFailed(Exception reflectionFailed) {
        String combinedMessage = "PMD API:\n" + reflectionFailed.getMessage();
        this.failed = new UnsupportedOperationException(combinedMessage, reflectionFailed);
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

    protected static Object getProperty(Rule rule, PropertyAdapter propertyAdapter) {
        Object property = propertyAdapter.getProperty();

        try {

            // return rule.getProperty(property);
            return findGetter(rule).invoke(rule, new Object[] { property });

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
