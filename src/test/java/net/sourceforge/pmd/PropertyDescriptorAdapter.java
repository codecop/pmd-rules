package net.sourceforge.pmd;

/**
 * Wrapper around changed PMD5 API used in testing framework. <br>
 * <br>
 * import net.sourceforge.pmd.PropertyDescriptor; <br>
 * moved to <br>
 * import net.sourceforge.pmd.properties.PropertyDescriptor; <br>
 * 
 * @author <a href="https://www.code-cop.org/">Peter Kofler</a>
 */
public class PropertyDescriptorAdapter {

    public static Object valueFrom(Object propertyDescriptor, String strValue) {
        return ((net.sourceforge.pmd.properties.PropertyDescriptor) propertyDescriptor).valueFrom(strValue);
    }

    public static void setProperty(Rule rule, Object propertyDescriptor, Object value) {
        rule.setProperty((net.sourceforge.pmd.properties.PropertyDescriptor) propertyDescriptor, value);
    }

}
