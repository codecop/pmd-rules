package net.sourceforge.pmd;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Wrapper around changed API used in testing framework.
 * 
 * @author PMD 5.6
 * @author <a href="https://www.code-cop.org/">Peter Kofler</a>
 */
public class RuleSetVersionAdapter {

    public static RuleSet create(Rule rule) {

        try {
            return tryPmd55(rule);
        } catch (Exception pmd55Failed) {

            try {
                return tryPmd56(rule);
            } catch (Exception pmd56Failed) {

                String combinedMessage = "PMD API:\n" + pmd55Failed.getMessage() + "\n" + pmd56Failed.getMessage();
                UnsupportedOperationException failed = new UnsupportedOperationException(combinedMessage, pmd56Failed);
                // failed.addSuppressed(pmd55Failed);
                throw failed;

            }

        }
    }

    private static RuleSet tryPmd55(Rule rule)
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        // RuleSet rules = new RuleSet();
        Constructor<RuleSet> constructor = RuleSet.class.getConstructor();
        RuleSet rules = constructor.newInstance();

        // rules.addRule(rule);
        Method setter = RuleSet.class.getMethod("addRule", Rule.class);
        setter.invoke(rules, rule);

        return rules;
    }

    private static RuleSet tryPmd56(Rule rule) throws ClassNotFoundException, NoSuchMethodException, InstantiationException,
            IllegalAccessException, InvocationTargetException {
        // RuleSetFactory factory = new RuleSetFactory();
        Class<?> factoryClass = RuleSetVersionAdapter.class.getClassLoader().loadClass("net.sourceforge.pmd.RuleSetFactory");
        Constructor<?> constructor = factoryClass.getConstructor();
        Object factory = constructor.newInstance();

        // RuleSet rules = factory.createSingleRuleRuleSet(rule);
        Method getter = factoryClass.getMethod("createSingleRuleRuleSet", Rule.class);
        RuleSet rules = (RuleSet) getter.invoke(factory, rule);

        return rules;
    }

}
