package net.sourceforge.pmd;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Wrapper around changed API used in testing framework. <br>
 * <br>
 * import net.sourceforge.pmd.PropertyDescriptor; <br>
 * moved to <br>
 * import net.sourceforge.pmd.properties.PropertyDescriptor; <br>
 * 
 * @author <a href="https://www.code-cop.org/">Peter Kofler</a>
 */
public class PropertyDescriptorAdapter {

    public static Object valueFrom(Object propertyDescriptor, String strValue) {
        try {
            return valueFromReflection(propertyDescriptor, strValue);
        } catch (Exception reflectionFailed) {
            String combinedMessage = "PMD API:\n" + reflectionFailed.getMessage();
            UnsupportedOperationException failed = new UnsupportedOperationException(combinedMessage, reflectionFailed);
            throw failed;
        }
    }

    private static Object valueFromReflection(Object propertyDescriptor, String strValue)
            throws SecurityException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        // return ((net.sourceforge.pmd.PropertyDescriptor) propertyDescriptor).valueFrom(strValue);
        // return ((net.sourceforge.pmd.properties.PropertyDescriptor) propertyDescriptor).valueFrom(strValue);
        Method valueFromMethod = propertyDescriptor.getClass().getMethod("valueFrom", new Class[] { String.class });
        valueFromMethod.setAccessible(true); // PMD 6
        return valueFromMethod.invoke(propertyDescriptor, strValue);
    }

    public static void setProperty(Rule rule, Object propertyDescriptor, Object value) {
        try {
            setPropertyReflection(rule, propertyDescriptor, value);
        } catch (Exception reflectionFailed) {
            String combinedMessage = "PMD API:\n" + reflectionFailed.getMessage();
            UnsupportedOperationException failed = new UnsupportedOperationException(combinedMessage, reflectionFailed);
            throw failed;
        }
    }

    private static void setPropertyReflection(Rule rule, Object propertyDescriptor, Object value)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        // rule.setProperty((net.sourceforge.pmd.PropertyDescriptor) propertyDescriptor, value);
        // rule.setProperty((net.sourceforge.pmd.properties.PropertyDescriptor) propertyDescriptor, value);
        for (Method method : rule.getClass().getMethods()) {
            if (method.getName().equals("setProperty") && method.getParameterTypes().length == 2) {
                method.invoke(rule, new Object[] { propertyDescriptor, value });
                return;
            }
        }
        throw new NoSuchMethodException("setProperty");

    }

}
