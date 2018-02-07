package org.codecop.pmd.versions;

import net.sourceforge.pmd.Rule;

/**
 * Wrapper around changed API of BooleanProperty.
 * 
 * @author <a href="http://www.code-cop.org/">Peter Kofler</a>
 * @see net.sourceforge.pmd.lang.rule.properties.BooleanProperty
 */
public class BooleanPropertyAdapter extends PropertyAdapter {

    private final String theName;
    private final String theDescription;
    private final Boolean defaultValue;
    private final float theUIOrder;

    public BooleanPropertyAdapter(String theName, String theDescription, Boolean defaultValue, float theUIOrder) {
        this.theName = theName;
        this.theDescription = theDescription;
        this.defaultValue = defaultValue;
        this.theUIOrder = theUIOrder;

        this.property5 = createPropertyOfType("net.sourceforge.pmd.lang.rule.properties.BooleanProperty"); // PMD 5.x
        this.property6 = createPropertyOfType("net.sourceforge.pmd.properties.BooleanProperty"); // PMD 6.x
    }

    private Object createPropertyOfType(String propertyClassName) {
        @SuppressWarnings("rawtypes")
        Class[] argumentTypes = new Class[] { String.class, String.class, Boolean.class, float.class };
        Object[] arguments = new Object[] { theName, theDescription, defaultValue, theUIOrder };
        return newInstance(propertyClassName, argumentTypes, arguments);
    }

    public static boolean getProperty(Rule rule, BooleanPropertyAdapter propertyAdapter) {
        return (Boolean) PropertyAdapter.getProperty(rule, propertyAdapter);
    }

}
