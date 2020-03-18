package org.codecop.pmd.versions;

import net.sourceforge.pmd.Rule;

/**
 * Wrapper around changed API of IntegerProperty.
 * 
 * @author <a href="https://www.code-cop.org/">Peter Kofler</a>
 * @see net.sourceforge.pmd.properties.IntegerProperty
 */
public class IntegerPropertyAdapter extends PropertyAdapter {

    private final String theName;
    private final String theDescription;
    private final Integer min;
    private final Integer max;
    private final Integer theDefault;
    private final float theUIOrder;

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
        @SuppressWarnings("rawtypes")
        Class[] argumentTypes = new Class[] { String.class, String.class, Integer.class, Integer.class, Integer.class, float.class };
        Object[] arguments = new Object[] { theName, theDescription, min, max, theDefault, theUIOrder };

        // return new IntegerProperty(theName, theDescription, min, max, theDefault, theUIOrder);
        return newInstance(propertyClassName, argumentTypes, arguments);
    }

    public static int getProperty(Rule rule, IntegerPropertyAdapter propertyAdapter) {
        return (Integer) PropertyAdapter.getProperty(rule, propertyAdapter);
    }

}
