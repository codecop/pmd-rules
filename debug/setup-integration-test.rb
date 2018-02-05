#! ruby
require 'rexml/document'

include REXML

RULESET = 'ruleset.xml'
IT_FOLDER = 'it'

def skip_last_part(s, part)
  esc_part = Regexp.escape(part)
  s.sub(/#{esc_part}[^#{esc_part}]+$/, '')
end

def parse_xml(file)
  File.open(file, 'rb') { |file|  REXML::Document.new(file) }
end

def trim(s)
  s.gsub(/^\s+|\s+$/, '')
end

def text(xml_node)
  trim(xml_node.text)
end

def comment(line)
  "// #{line} \n"
end

def list_test_cases(xml_tests)
  puts 'found following test names'
  puts '--------------------------'
  REXML::XPath.each(xml_tests, "//test-code/description" ) do |description_node|
    puts text(description_node)
  end
end

def save_file(file, body)
  puts "saving #{file}"
  File.open(file, "w") { |f| f.print body }
end

# Create a rule set file _rulefile_ containing a single _rule_ reference.
def save_single_ruleset_rule(rulefile, rule)
  body = "<?xml version=\"1.0\" ?>
<ruleset name=\"onlyone\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"http://pmd.sf.net/ruleset_xml_schema.xsd\">
   <rule ref=\"#{rule}\" />
</ruleset>"

  save_file(rulefile, body)
end

if __FILE__ == $0

  if ARGV.empty?
    print "#{File.basename $0} rule-name test-name\n\n"
    print "rule-name ... e.g. codecop.xml/JUnitTestsShouldIncludeAssertOrVerify\n"
    print "prepares local test setup."
    exit(1)
  end

  it_folder = File.dirname($0) + '/' + IT_FOLDER

  header_lines = []

  given_rule = ARGV[0]
  if ARGV.size > 1
    given_test = ARGV[1]
  end

  rule_ref = 'rulesets/java/' + given_rule
  header_lines << "testing #{rule_ref}"

  rule_file = './src/main/resources/' + skip_last_part(rule_ref, '/')
  raise rule_file unless File.exists? rule_file
  header_lines << "in file  #{rule_file}"

  xml_tests_file = './src/test/resources/org/codecop/pmd/' + given_rule.sub(/codecop/, 'rule').sub(/\.xml/, '/xml') + '.xml'
  raise xml_tests_file unless File.exists? xml_tests_file
  header_lines << "test cases #{xml_tests_file}"

  xml_tests = parse_xml(xml_tests_file)
  if !given_test
    list_test_cases(xml_tests)
    exit(1)
  end

  xml_test = REXML::XPath.first(xml_tests, "//test-code[contains(description, '#{given_test}')]")
  xml_test_name = text(xml_test.elements['description'])
  header_lines << "*** #{xml_test_name} ***"
  puts "using test case #{xml_test_name}"

  sample_code = text(xml_test.elements['code'])
  code = header_lines.map { |l| comment(l) }.join + sample_code + "\n"

  sample_code =~ /class\s+(\w+)/
  class_name = $1

  package = ''
  java_dir = ''
  if sample_code =~ /package\s+([\w.]+;)/
    package = $1
    java_dir = package.gsub(/\./, '/') + '/'
  end
  java_file = java_dir + class_name + '.java'

  Dir.mkdir it_folder unless File.exists? it_folder
  Dir.chdir it_folder
  save_single_ruleset_rule(RULESET, rule_ref)
  Dir.mkdir java_dir unless java_dir.empty?
  save_file(java_file, code)
  Dir.chdir '..'

  puts "run net.sourceforge.pmd.PMD -d #{it_folder} -f text -R #{it_folder}/#{RULESET} -l java -debug"
end
