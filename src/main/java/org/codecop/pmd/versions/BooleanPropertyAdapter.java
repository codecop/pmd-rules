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

        this.property5 = createPropertyPMD5();
        this.property6 = createPropertyPMD6();
    }

    private Object createPropertyPMD5() {
        @SuppressWarnings("rawtypes")
        Class[] argumentTypes = new Class[] { String.class, String.class, Boolean.class, float.class };
        return createProperty("net.sourceforge.pmd.lang.rule.properties.BooleanProperty", argumentTypes);
    }

    private Object createPropertyPMD6() {
        @SuppressWarnings("rawtypes")
        Class[] argumentTypes = new Class[] { String.class, String.class, boolean.class, float.class };
        return createProperty("net.sourceforge.pmd.properties.BooleanProperty", argumentTypes);
    }

    @SuppressWarnings("rawtypes")
    private Object createProperty(String propertyClassName, Class[] argumentTypes) {
        Object[] arguments = new Object[] { theName, theDescription, defaultValue, theUIOrder };
        return newInstance(propertyClassName, argumentTypes, arguments);
    }

    public static boolean getProperty(Rule rule, BooleanPropertyAdapter propertyAdapter) {
        return (Boolean) PropertyAdapter.getProperty(rule, propertyAdapter);
    }

}
