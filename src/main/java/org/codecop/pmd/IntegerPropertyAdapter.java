package org.codecop.pmd;

import net.sourceforge.pmd.Rule;
import net.sourceforge.pmd.lang.rule.properties.IntegerProperty;

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

    public IntegerPropertyAdapter(String theName, String theDescription, Integer min, Integer max, Integer theDefault, float theUIOrder) {
        this.theName = theName;
        this.theDescription = theDescription;
        this.min = min;
        this.max = max;
        this.theDefault = theDefault;
        this.theUIOrder = theUIOrder;
    }

    private IntegerProperty asProperty() {
        return new IntegerProperty(theName, theDescription, min, max, theDefault, theUIOrder);
    }

    public static int getProperty(Rule rule, IntegerPropertyAdapter propertyAdapter) {
        return rule.getProperty(propertyAdapter.asProperty());
    }

}
