package net.sourceforge.pmd;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class SimpleRuleSetNameMapper {

   private final StringBuffer rulesets = new StringBuffer();
   protected Map<String, String> nameMap = new HashMap<String, String>();

   public SimpleRuleSetNameMapper(String ruleString) {
      populateNameMap();
      if (ruleString.indexOf(',') == -1) {
         check(ruleString);
         return;
      }
      for (StringTokenizer st = new StringTokenizer(ruleString, ","); st.hasMoreTokens();) {
         String tok = st.nextToken();
         check(tok);
      }
   }

   public String getRuleSets() {
      return rulesets.toString();
   }

   protected void check(String name) {
      if (name.indexOf("rulesets") == -1 && nameMap.containsKey(name)) {
         append(nameMap.get(name));
      } else {
         append(name);
      }
   }

   protected void append(String name) {
      if (rulesets.length() > 0) {
         rulesets.append(',');
      }
      rulesets.append(name);
   }

   private void populateNameMap() {
      nameMap.put("codecop", "src/main/resources/rulesets/codecop.xml");
      nameMap.put("prototype", "src/main/resources/rulesets/prototype.xml");
   }
}
