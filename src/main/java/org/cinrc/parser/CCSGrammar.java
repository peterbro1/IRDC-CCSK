package org.cinrc.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.cinrc.process.nodes.ComplementLabelNode;
import org.cinrc.process.nodes.Label;
import org.cinrc.process.nodes.LabelKey;
import org.cinrc.process.nodes.LabelNode;
import org.cinrc.process.nodes.ProgramNode;
import org.cinrc.process.nodes.TauLabelNode;
import org.cinrc.process.process.ActionPrefixProcess;
import org.cinrc.process.process.ConcurrentProcess;
import org.cinrc.process.process.NullProcess;
import org.cinrc.process.process.ProcessImpl;
import org.cinrc.process.process.SummationProcess;

public enum CCSGrammar {

  LABEL("[a-z]", LabelNode.class, null, false),
  WHITESPACE(" ", null, " ", false),
  OPEN_PARENTHESIS("\\(", null, "(", true),
  CLOSE_PARENTHESIS("\\)", null, ")", true),
  OUT_LABEL(String.format("'%s", LABEL.pString), ComplementLabelNode.class, null, false),
  LABELS_BASIC(String.format("((%s)|(%s))",LABEL.pString,OUT_LABEL.pString), null, null, false),

  TAU_START("Tau", TauLabelNode.class, null, true), // k0, k1
  TAU_LABEL(String.format("%s\\{%s\\}",TAU_START.pString, LABEL.pString),
      TauLabelNode.class, null, true),
  LABEL_COMBINED(String.format("((%s)|(%s)|(%s))",
      LABEL.pString, OUT_LABEL.pString, TAU_LABEL.pString), Label.class, null, true),

  PROCESS("[A-Z]", ProcessImpl.class, null, true),
  NULL_PROCESS("[0]", NullProcess.class, "0", true),
  OP_SEQUENTIAL("\\.", null, ".", true),
  COMPLEMENT_SIG("'", null, "'", false),
  OP_CONCURRENT("\\|", ConcurrentProcess.class, "|", true),
  OP_SUMMATION("\\+", SummationProcess.class, "+", true),
  OPEN_RESTRICTION("\\\\\\{", null, "{", true), //6 backslashes, LOL. \\{
  CLOSE_RESTRICTION("\\}", null, "}", true),
  OPEN_KEY_NOTATION("\\[", null, "[", false),
  CLOSE_KEY_NOTATION("\\]", null, "]", false),
  LABEL_KEY(String.format("k[0-9]*"), LabelKey.class, null, false), // k0, k1
  LABEL_KEY_COMBINED(String.format("%s%s%s"
      ,OPEN_KEY_NOTATION.pString, LABEL_KEY.pString, CLOSE_KEY_NOTATION.pString)
      , LabelKey.class, null, false), //[k4]
  LABEL_KEY_FULL(String.format("(%s|%s)%s",
      TAU_LABEL.pString, LABEL_COMBINED.pString, LABEL_KEY_COMBINED.pString),
      LabelKey.class, null, true);

  public static final Pattern parenthesisRegex;

  static {
    parenthesisRegex =
        Pattern.compile(String.format("([\\%s\\%s])", OPEN_PARENTHESIS, CLOSE_PARENTHESIS));
  }

  public final String pString, rep;
  private final Class<? extends ProgramNode> classObject;
  private final boolean canParse;

  /**
   * @param s        Regex to match against
   * @param c        Instantiatable class representation
   * @param rep      Human readable constant
   * @param canParse Should this be parseable
   */
  CCSGrammar(String s, Class<? extends ProgramNode> c, String rep, boolean canParse) {
    this.pString = s;
    this.classObject = c;
    this.rep = rep;
    this.canParse = canParse;
  }

  public Class<? extends ProgramNode> getClassObject() {
    return classObject;
  }

  public boolean canBeParsed() {
    return this.canParse;
  }

  /***
   * Returns pattern object from stored string, caching it if first access.
   * @return Pattern object
   */
  Pattern getPattern() {
    return Pattern.compile(this.pString);
  }

  public Matcher match(CharSequence c) {
    return getPattern().matcher(c);
  }

  public String toString() {
    return rep == null ? "" : rep;
  }


}
