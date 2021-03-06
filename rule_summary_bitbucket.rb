#! ruby
require 'rexml/document'

# Create Bitbucket Wiki pages for the rule sets.
# @author Peter Kofler

@lines = []

# Redefine puts not to print _msg_ to buffer.
def puts(msg)
  # print msg
  # print "\n"
  @lines << "#{msg}\n"
end

# Get the contents from _text_ DOM and normalise it.
def norm_text(text)
  text.to_s.strip.gsub(/\s+/,' ').
       gsub(/(RuntimeException|RelationShip|ValueObject|OperationNotSupportedException)/, '`\1`')
end

# Format the _rule_ DOM.
def format_rule(rule)
  return unless rule.attributes['name']

  puts ''
  puts '----'
  puts ''
  puts "== #{rule.attributes['name']} =="
  puts ''

  is_deprecated = rule.attributes['deprecated'] == 'true'
  if is_deprecated
    puts '**Deprecated**'
    puts ''
    
    ref = rule.attributes['ref'].sub(/rulesets\/java\//, '') 
    name = ref[/[^\/]+$/]
    ruleset = ref[/^[^\.]+/]
    puts "This rule has been renamed or moved. Use instead: [#{name}]{PmdRules#{ruleset.capitalize}.wiki##{name.downcase}}"
    
    return
  end
  
  puts "  #{norm_text(rule.get_text('description'))}"
  puts ''

  isxpath = rule.attributes['class'] == 'net.sourceforge.pmd.lang.rule.XPathRule'
  if isxpath
    puts 'This rule is defined by the following XPath expression:'
    puts '{{{'
    puts rule.elements['properties/property/value'].texts.join.strip
    puts '}}}'
  else
    classname = rule.attributes['class']
    href = "https://bitbucket.org/pkofler/pmd-rules/src/tip/src/main/java/#{classname.gsub(/\./,'/')}.java"
    puts "This rule is defined by the following Java class: [[#{href}|#{classname}]]"
  end

  rule.elements.each('example') do |example|
    puts ''
    puts '=== Example: ==='
    puts '{{{'
    puts '#!java'
    puts ''
    puts example.texts.join.strip
    puts '}}}'
  end

  properties = rule.elements['count(properties/property)']
  properties -= 1 if isxpath
  if properties > 0
    puts ''
    puts 'This rule has the following properties:'
    puts ''
    puts '|=Heading Name |=Heading Default value |=Heading Description |'
    rule.elements.each('properties/property') do |prop|
      next if prop.attributes['name']=='xpath'
      puts "| #{prop.attributes['name']} | '#{prop.attributes['value']}' | #{prop.attributes['description']} |"
    end
  end
end

def format_ruleset(ruleset)
  desc = norm_text(ruleset.get_text('description'))
  puts "= #{ruleset.attributes['name']} ="
  puts ''
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

rules_dir = 'src/main/resources/rulesets/java'
sets = read_rules(rules_dir)
sets.each_key do |name|
  lines = sets[name]
  next if lines.size <= 3
  filename = '../PMDRules Wiki/PmdRules' + name.capitalize.sub(/\.xml/, '') + '.wiki'
  File.open(filename, 'w') { |file|
    lines.each { |line| file.print line }
  }
  print "saved #{filename}\n"
end
