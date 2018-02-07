package net.sourceforge.pmd;

/**
 * Wrapper around changed API used in testing framework. <br>
 * <br>
 * import net.sourceforge.pmd.PropertyDescriptor; <br>
 * moved to <br>
 * import net.sourceforge.pmd.properties.PropertyDescriptor; <br>
 * 
 * @author PMD 6.0
 * @author <a href="http://www.code-cop.org/">Peter Kofler</a>
 */
public class PropertyDescriptorAdapter {

    @SuppressWarnings("rawtypes")
    public static Object valueFrom(Object propertyDescriptor, String strValue) {
        return ((PropertyDescriptor) propertyDescriptor).valueFrom(strValue);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void setProperty(Rule rule, Object propertyDescriptor, Object value) {
        rule.setProperty((PropertyDescriptor) propertyDescriptor, value);
    }

}
