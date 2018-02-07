package org.codecop.pmd;

import net.sourceforge.pmd.Rule;
import net.sourceforge.pmd.lang.rule.properties.BooleanProperty;

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

    public BooleanPropertyAdapter(String theName, String theDescription, Boolean defaultValue, float theUIOrder) {
        this.theName = theName;
        this.theDescription = theDescription;
        this.defaultValue = defaultValue;
        this.theUIOrder = theUIOrder;
    }

    private BooleanProperty asProperty() {
        return new BooleanProperty(theName, theDescription, defaultValue, theUIOrder);
    }

    public static boolean getProperty(Rule rule, BooleanPropertyAdapter propertyAdapter) {
        return rule.getProperty(propertyAdapter.asProperty());
    }

}
