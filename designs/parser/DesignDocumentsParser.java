record Line(int number, String text){}

final Pattern newLineRegex= Pattern.compile("[\\x0B\\f\\r\\x85\\u2028\\u2029]");
final Pattern stringOrCommentRegex= Pattern.compile("\"(?:\\\\.|[^\"\\\\])*\"|//");
final Pattern grammarLineRegex= Pattern.compile("([A-Z][A-Za-z0-9]*) *::=.*");
final Pattern wellFormednessLineRegex= Pattern.compile("in +([A-Z][A-Za-z0-9]*):.*");
final Map<String, String> defined= new HashMap<>();
boolean failed= false;

void main(String[] args) throws IOException{
  if (args.length != 1 || !Files.isDirectory(Path.of(args[0]))){ System.err.println("usage: java DesignDocumentsParser.java folder; the files directly in folder are the documents of one DesignScope."); System.exit(1); }
  try (var files= Files.list(Path.of(args[0]))){
    for (var f: files.filter(Files::isRegularFile).sorted().toList()){ check(f); }
  }
  if (failed){ System.exit(1); }
}
void error(Path p, int line, String message){
  failed= true;
  System.err.println(p + ":" + line + ": " + message);
}
void check(Path p) throws IOException{
  String text;
  try{ text= Files.readString(p); }
  catch (CharacterCodingException e){ failed= true; System.err.println(p + ": the file is not UTF-8."); return; }
  var m= newLineRegex.matcher(text);
  if (m.find()){ error(p, lineOf(text, m.start()), "the character U+%04X represents a new line; lines end only with \"\\n\".".formatted((int)text.charAt(m.start()))); return; }
  if (!text.isEmpty() && !text.endsWith("\n")){ error(p, lineOf(text, text.length()), "the last line does not end with \"\\n\"."); return; }
  var sections= sections(p, lines(p, text.lines().toList()));
  for (int i= 0; i < sections.size(); i++){ section(p, sections, i); }
}
int lineOf(String text, int index){
  return (int)text.substring(0, index).chars().filter(c -> c == '\n').count() + 1;
}
int commentStart(String s){
  return stringOrCommentRegex.matcher(s).results().filter(r -> r.group().equals("//")).mapToInt(MatchResult::start).findFirst().orElse(-1);
}
List<Line> lines(Path p, List<String> raw){
  var res= new ArrayList<Line>();
  Line open= null;
  for (int i= 0; i < raw.size(); i++){
    var s= raw.get(i);
    int c= commentStart(s);
    if (c >= 0 && s.substring(0, c).matches(" *")){ continue; }
    if (c >= 0){ s= s.substring(0, c); }
    var l= open == null ? new Line(i + 1, s) : new Line(open.number(), open.text() + s);
    open= null;
    if (l.text().endsWith("\\")){ open= new Line(l.number(), l.text().substring(0, l.text().length() - 1)); continue; }
    res.add(l);
  }
  if (open != null){ error(p, open.number(), "this line ends with a backslash, but no line follows to continue it."); }
  return res;
}
List<List<Line>> sections(Path p, List<Line> lines){
  var res= new ArrayList<List<Line>>();
  var current= new ArrayList<Line>();
  for (int i= 0; i < lines.size(); i++){
    var l= lines.get(i);
    if (!l.text().isEmpty()){ current.add(l); continue; }
    if (current.isEmpty() || i + 1 == lines.size()){ error(p, l.number(), "this empty line separates no two sections: two sections are separated by exactly one empty line, and a document neither starts nor ends with an empty line."); continue; }
    res.add(current);
    current= new ArrayList<>();
  }
  if (!current.isEmpty()){ res.add(current); }
  return res;
}
void section(Path p, List<List<Line>> sections, int i){
  var head= sections.get(i).getFirst();
  var rest= sections.get(i).subList(1, sections.get(i).size());
  switch (head.text()){
    case "Text:", "Invariant:" -> {}
    case "Implementation notes:" -> { if (i + 1 < sections.size()){ error(p, head.number(), "the \"Implementation notes:\" section is not the last section of its document."); } }
    case "Grammar:" -> rest.forEach(l -> grammarLine(p, l));
    case "Well formedness:" -> wellFormedness(p, head, rest, i == 0 ? List.of() : sections.get(i - 1));
    case "Story:" -> storyLines(p, rest);
    case String s when s.matches("Define .*:") -> storyLines(p, rest);
    default -> error(p, head.number(), "\"%s\" is not a category line: the first line of a section is \"Text:\", \"Grammar:\", \"Well formedness:\", \"Story:\", \"Define \" followed by any text and \":\", \"Invariant:\" or \"Implementation notes:\".".formatted(head.text()));
  }
}
void grammarLine(Path p, Line l){
  var m= grammarLineRegex.matcher(l.text());
  if (!m.matches()){ error(p, l.number(), "\"%s\" is not a grammar line: a grammar line is a Nonterminal (an uppercase letter, then letters and digits), any number of spaces, \"::=\", then any text.".formatted(l.text())); return; }
  var first= defined.putIfAbsent(m.group(1), p + ":" + l.number());
  if (first != null){ error(p, l.number(), "the Nonterminal %s is defined again: %s defines it first.".formatted(m.group(1), first)); }
}
void wellFormedness(Path p, Line head, List<Line> rest, List<Line> before){
  boolean afterGrammar= !before.isEmpty() && before.getFirst().text().equals("Grammar:");
  if (!afterGrammar){ error(p, head.number(), "this Well formedness section does not come directly after a Grammar section."); }
  var heads= before.stream().map(l -> grammarLineRegex.matcher(l.text())).filter(Matcher::matches).map(m -> m.group(1)).toList();
  for (var l: rest){
    var m= wellFormednessLineRegex.matcher(l.text());
    if (!m.matches()){ error(p, l.number(), "\"%s\" is not a well formedness line: a well formedness line is \"in\", one or more spaces, a Nonterminal (an uppercase letter, then letters and digits), \":\", then any text.".formatted(l.text())); continue; }
    if (afterGrammar && !heads.contains(m.group(1))){ error(p, l.number(), "\"%s\" constrains %s, but the Grammar section directly before does not define %s.".formatted(l.text(), m.group(1), m.group(1))); }
  }
}
void storyLines(Path p, List<Line> lines){
  for (int i= 0; i < lines.size(); i++){
    var l= lines.get(i);
    if (i == 26){ error(p, l.number(), "this is story line 27 of its section: the labels go only from A to Z."); return; }
    var label= (char)('A' + i) + " ";
    if (!l.text().startsWith(label)){ error(p, l.number(), "\"%s\" is story line %d of its section, so it starts with \"%s\": the labels of a section are A, B, C, ... in order, each followed by a space.".formatted(l.text(), i + 1, label)); return; }
  }
}
