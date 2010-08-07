#! ruby
require 'rexml/document'

# Create Google Code Wiki pages for the rule sets.
# @author Peter Kofler

@lines = []

# Redefine puts not to print _msg_ to buffer. 
def puts(msg)
  # print msg
  # print "\n"
  @lines << "#{msg}\n"
end

# Get the contents from _text_ DOM and normalize it.
def norm_text(text)
  text.to_s.strip.gsub(/\s+/,' ')
end

def wiki_name?(name)
  name !~ /[A-Z]{2}/ && name !~ /[A-Z]$/
end

# Format the _rule_ DOM.
def format_rule(rule)
  puts ''
  puts '----'
  puts ''
  is_wiki_name = wiki_name?(rule.attributes['name'])
  if is_wiki_name
    puts "==!#{rule.attributes['name']}=="
  else
    puts "==#{rule.attributes['name']}=="
  end
  puts " #{norm_text(rule.get_text('description'))}"
  #rule_msg = rule.attributes['message'].gsub(/\s+/,' ')
  #prio = rule.elements['priority'].text.to_i
  
  isxpath = rule.attributes['class']=='net.sourceforge.pmd.rules.XPathRule' 
  if isxpath
    puts '<p>This rule is defined by the following XPath expression:</p>'
    puts '{{{'
    puts rule.elements['properties/property/value'].texts.join.strip
    puts '}}}'
  else
    classname = rule.attributes['class']
    href = "https://code.google.com/p/code-cop-code/source/browse/src/main/java/#{classname.gsub(/\./,'/')}.java?repo=pmd-rules"
    puts "<p>This rule is defined by the following Java class: [#{href} `#{classname}`]</p>"
  end
  
  rule.elements.each('example') do |example|
    puts '<p>*Example:*</p>'
    puts '{{{'
    puts example.texts.join.strip
    puts '}}}'
  end
  
  properties = rule.elements['count(properties/property)']
  properties -= 1 if isxpath
  if properties > 0
    puts '<p>This rule has the following properties:</p>'
    # puts '<table><th>Name</th><th>Default value</th><th>Description</th>'
    puts '|| Name || Default value || Description ||'
    rule.elements.each('properties/property') do |prop|
      next if prop.attributes['name']=='xpath'
      # puts "  <tr><td>#{prop.attributes['name']}</td><td>`#{prop.attributes['value']}`</td><td>#{prop.attributes['description']}</td></tr>"
      puts "|| #{prop.attributes['name']} || `#{prop.attributes['value']}` || #{prop.attributes['description']} ||"
    end
    # puts '</table>'
  end
end

def format_ruleset(ruleset)
  desc = norm_text(ruleset.get_text('description'))
  puts "#summary #{desc.sub(/\..*$/,'.')}"
  puts '#sidebar PmdRulesLinks'
  puts ''
  
  puts ''
  puts "=#{ruleset.attributes['name']}="
  puts "#{desc}"
  ruleset.elements.each('rule') do |rule|
    format_rule(rule)
  end
end

def read_rules(rules_dir)
  current_dir = Dir.getwd
  Dir.chdir rules_dir
  begin
    
    sets = {}
    
    Dir['*.xml'].each do |file_name|
      @lines = [] 
      doc = REXML::Document.new(File.new(file_name))
      doc.elements.each('ruleset') do |ruleset|
        format_ruleset(ruleset)
      end
      sets[File.basename(file_name)] = @lines.dup
    end
    
  ensure
    Dir.chdir current_dir
    @lines = [] 
  end
  sets
end

rules_dir = 'src/main/resources/rulesets'
sets = read_rules(rules_dir)
sets.each_key do |name|
  lines = sets[name]
  filename = 'target/' + name + '.txt'
  File.open(filename, 'w') { |file|
    lines.each { |line| file.print line }
  }
  print "saved #{filename}\n"
end
