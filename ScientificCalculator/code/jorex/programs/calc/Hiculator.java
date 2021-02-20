package jorex.programs.calc;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jorex.programs.ntw.NumberToWord;

import static java.lang.Math.*;
import static jorex.programs.ntw.NumberToWord.*;

/**
 * Program to solve/evaluate mathematical expressions using BODMAS.
 *
 * @author Jore
 *
 * @version 1.0
 */
public class Hiculator {

  public static final int ROUND_MODE_OFF = 0;

  public static final String DELIMETER_DEFAULT = " ";

  public static final String USAGE_COMMAND_LINE = new StringBuffer ()
  .append (" Usage: [command] [options] [expressions...]                         \n\n")
  .append (" Evaluate/solve (a) mathematical expression(s) using BODMAS.         \n\n")
  .append (" The command represents the command used to excecute this program e.g\n\n")
  .append ("$ java -jar ./ScientificCalculator/jorexcalc.jar                     \n\n")
  .append ("or                                                                   \n\n")
  .append ("$ java -cp ./ScientificCalculator jorex.programs.calc.Hiculator      \n\n")
  .append ("Options include:                                                     \n\n")
  .append ("  -w, --words                                                          \n")
  .append ("                    Output the answer in words.                        \n")
  .append ("  -r, --round-off=<value>                                              \n")
  .append ("                    Round off the answer to the nearest place value of \n")
  .append ("                    the digit at position 'value' in the answer from   \n")
  .append ("                    the left.                                          \n")
  .append ("  -d, --delimeter=<string>                                             \n")
  .append ("                    Used with -w to specify 'string' as  a delimeter/  \n")
  .append ("                    separator for the words.                           \n")
  .append ("  -1, --format-all-uppercase                                           \n")
  .append ("                    Used with -w to convert all the words to uppercase.\n")
  .append ("  -2, --format-first-letter-uppercase                                  \n")
  .append ("                    Used with -w to convert only the first letter of   \n")
  .append ("                    the first word to uppercase.                       \n")
  .append ("  -3, ---format-first-letter-uppercase-all                             \n")
  .append ("                    Used with -w to convert the first letter of each   \n")
  .append ("                    word to uppercase.                                 \n")
  .append ("  -f, --format-first-letter-uppercase-range=<range>                    \n")
  .append ("                    Used with -w to convert the first letter of each   \n")
  .append ("                    word after 'range' digits in the answer from the   \n")
  .append ("                    right to uppercase (Experimental)                  \n")
  .append ("  -h, --help                                                           \n")
  .append ("                    Print this message end exit.                       \n")
  .append ("  -l, --list                                                           \n")
  .append ("                    Print a list of supported operations and symbols   \n")
  .append ("                    and exit.                                        \n\n")
  .append (" The answers to the supplied expressions are all output to System.out  \n")
  .append ("in the order in which they are supplied, using the specified format if \n")
  .append ("if any. If none are supplied as arguments they are read from           \n")
  .append ("System.in.                                                           \n\n")
  .append (" Note: Unfortunately, concatenation of short commands e.g -wrd1f is    \n")
  .append ("       currently not supported and will be interpreted as an expression\n")
  .append ("       resolving to a Syntax error!                                    \n")
  .append (" All mandatory arguments for short options are mandatory for long      \n")
  .append ("options too.                                                           \n")
  .append (" All supplied arguments excluding the above are considered to be       \n")
  .append ("expressions.                                                           \n")
  .append (" By default, if -w specified without any formats, the output is all    \n")
  .append ("converted to lower case.                                               \n")
  .append (" Incase multiple options resolving to the same function are supplied,  \n")
  .append ("the last option ovewrites any preceding ones.                          \n")
  .toString ();

  public static final String OPERATIONS = new StringBuffer ()
  .append (" Supported operations include:                                       \n\n")
  .append ("  [÷ | /] - For division.                                              \n")
  .append ("  [× | *] - For multiplication.                                        \n")
  .append ("      [+] - For addition.                                              \n")
  .append ("      [-] - For subtraction.                                           \n")
  .append ("      [²] - For raising a number to the power 2.                       \n")
  .append ("      [³] - For raising a number to the power 3.                       \n")
  .append ("      [√] - For finding the square root of a number.                   \n")
  .append ("      [!] - For finding the factorial of a number.                   \n\n")
  .append (" Note: The maximum value for finding a factorial is 50, and any value  \n")
  .append ("       higher than that will resolve to a Math error!                  \n")
  .toString ();

  public static final String SYMBOLS = new StringBuffer ()
  .append (" Supported symbols include:                                          \n\n")
  .append ("      [π | Π] - Replaced with the mathematical constant PI.            \n")
  .append ("  [ANS | ans] - Replaced with the answer to the previous successful    \n")
  .append ("                evaluation, or 0 if none.                              \n")
  .toString ();

  private static String ANS = "0";

  private static final String ANSWER_PATTERN = "(([-+]?)(\\d+)([\\.]?)(\\d*)(([E]?)([-+]?)(\\d+)([\\.]?)(\\d*))*)*";

  public static void main (String[] args) {
    int range = -1;
    boolean toWords = false;
    int format = FORMAT_DEFAULT;
    int roundTo = ROUND_MODE_OFF;
    String delimeter = DELIMETER_DEFAULT;
    List<String> expressions = new LinkedList<> ();
    String prompt = " E: Option %s requires a(n) %s argument.\n Use '-h' or '--help' for more information.\n";

    for (int i = 0; i < args.length; i++) {
      String arg = args [i];

      if (arg.equals ("-w") || arg.equals ("--words"))
        toWords = true;
      else if (arg.equals ("-r"))
        try {
          roundTo = Integer.parseInt (args [++i]);
        }
        catch (Throwable t) {
          System.out.printf (prompt, arg, "int");
          return;
        }
      else if (arg.startsWith ("--round-off")) {
        try {
          roundTo = Integer.parseInt (arg.split ("=") [1]);
        }
        catch (Throwable t) {
          System.out.printf (prompt, arg.split ("=") [0].replace ("=", ""), "int");
          return;
        }
      }
      else if (arg.equals ("-d"))
        try {
          delimeter = args [++i];
        }
        catch (Throwable t) {
          System.out.printf (prompt, arg, "String");
          return;
        }
      else if (arg.startsWith ("--delimeter"))
        try {
          delimeter = arg.split ("=") [1];
        }
        catch (Throwable t) {
          System.out.printf (prompt, arg.split ("=") [0].replace ("=", ""), "String");
          return;
        }
      else if (arg.equals ("-1") || arg.equals ("--format-all-uppercase"))
        format = FORMAT_ALL_UPPERCASE;
      else if (arg.equals ("-2") || arg.equals ("--format-first-letter-uppercase"))
        format = FORMAT_FIRST_LETTER_UPPER_CASE;
      else if (arg.equals ("-3") || arg.equals ("--format-first-letter-uppercase-all"))
        format = FORMAT_FIRST_LETTER_UPPER_CASE_ALL;
      else if (arg.equals ("-f"))
        try {
          format = FORMAT_FIRST_LETTER_UPPER_CASE_RANGE;
          range = Integer.parseInt (args [++i]);
        }
        catch (Throwable t) {
          System.out.printf (prompt, arg.split ("=") [0].replace ("=", ""), "int");
          return;
        }
      else if (arg.startsWith ("--format-first-letter-uppercase-range"))
        try {
          format = FORMAT_FIRST_LETTER_UPPER_CASE_RANGE;
          range = Integer.parseInt (arg.split ("=") [1]);
        }
        catch (Throwable t) {
          System.out.printf (prompt, arg.split ("=") [0].replace ("=", ""), "int");
          return;
        }
      else if (arg.equals ("-h") || arg.equals ("--help")) {
        System.out.println (USAGE_COMMAND_LINE);
        return;
      }
      else if (arg.equals ("-l") || arg.equals ("--list")) {
        System.out.println (OPERATIONS);
        System.out.println (SYMBOLS);
        return;
      }
      else
        expressions.add (arg);
    }

    if (expressions.isEmpty ()) {
      System.out.printf (" Use 'q' or 'Q' to quit.\n\n%s\n%s\n%s\n%s\n%s\n\n",
                         " ┌────────────────────────────┐",
                         " │╻░╻╻░░░┏━╸╻░╻╻░░┏━┓╺┳╸┏━┓┏━┓│",
                         " │┣━┫┃╺━╸┃░░┃░┃┃░░┣━┫░┃░┃░┃┣┳┛│",
                         " │╹░╹╹░░░┗━╸┗━┛┗━╸╹░╹░╹░┗━┛╹┗╸│",
                         " └────────────────────────────┘");

      try (Scanner sc = new Scanner (System.in)) {
        while (true) {
          System.out.print (" Expression: ");
          String expr;

          if (sc.hasNext ()) {
            expr = sc.next ();

            if (expr.equalsIgnoreCase ("q"))
              break;
            else
              System.out.printf ("\t= %s\n\n", toWords ? evaluateToWords (expr, roundTo, delimeter, format, range) : evaluate (expr, roundTo));
          }
          else {
            System.out.println ();
            return;
          }
        }
      }
    }
    else
      for (String expr : expressions)
        System.out.printf (" %s = %s\n", expr, toWords ? evaluateToWords (expr, roundTo, delimeter, format, range) : evaluate (expr, roundTo));
  }

  public static final BigDecimal getAns () {
    try (Scanner sc = new Scanner (ANS)) {
      return sc.nextBigDecimal ();
    }
  }

  public static final String evaluate (String expression, int roundTo) {
    expression = evaluate (expression);

    try {
      if (roundTo > ROUND_MODE_OFF && Pattern.matches (ANSWER_PATTERN, expression))
        try (Scanner sc = new Scanner (evaluate (expression))) {
          return sc.nextBigDecimal ()
            .round (new MathContext (roundTo))
            .toPlainString ();
        }
    }
    catch (Throwable t) {
      System.err.println (" WTF: SERIOUS ERROR! REPORT IMMEDIATELY!");
    }
    return expression;
  }
  public static final String evaluateToWords (String expression, int roundTo) {
    return evaluateToWords (expression, roundTo, DELIMETER_DEFAULT, FORMAT_DEFAULT, -1);
  }

  public static final String evaluateToWords (String expression, int roundTo, int format) {
    return evaluateToWords (expression, roundTo, DELIMETER_DEFAULT, format, -1);
  }

  public static final String evaluateToWords (String expression, int roundTo, String delimeter) {
    return evaluateToWords (expression, roundTo, delimeter, FORMAT_DEFAULT, -1);
  }

  public static final String evaluateToWords (String expression, int roundTo, int format, int range) {
    return evaluateToWords (expression, roundTo, DELIMETER_DEFAULT, format, range);
  }

  public static final String evaluateToWords (String expression, int roundTo, String delimeter, int format) {
    return evaluateToWords (expression, roundTo, delimeter, format, -1);
  }

  public static final String evaluateToWords (String expression, int roundTo, String delimeter, int format, int range) {
    expression = evaluate (expression, roundTo);

    if (Pattern.matches (ANSWER_PATTERN, expression))
      try (Scanner sc = new Scanner (expression)) {
        return NumberToWord.numberToWordFormatted (sc.nextBigDecimal (), delimeter, format, range);
      }
    return expression;
  }

  private static final String evaluate (String expression) {
    try {
      if (expression.isEmpty ())
        return expression;

      if (expression.equalsIgnoreCase ("ans"))
        return ANS;

      expression = expression
        .replace ('*', '×')
        .replace ('/', '÷')
        .replace ('Π', 'π')
        .replace ("ANS", "ans");

      expression = ans (expression);
      expression = pi (expression); 
      expression = of (expression);
      expression = val (expression);

      if (Pattern.matches (ANSWER_PATTERN, expression)) {
        expression = (expression.endsWith (".0")) ? expression.replace (".0", "") : expression;
        expression = (expression.startsWith ("+")) ? expression.replace ("+", "") : expression;
        ANS = expression;

        return expression;
      }
      else if (expression.equals ("Infinity") || expression.equals ("-Infinity"))
        return expression;
      else if (expression.equals ("NaN"))
        return "Math Error!";
    }
    catch (NumberFormatException | ArithmeticException | InputMismatchException e) {
      return "Math error!";
    }
    return "Syntax error!";
  }

  private static final String pi (String input) {
    if (!input.contains ("π"))
      return input;

    Pattern pattern = Pattern.compile ("((\\d+)([\\.]?)(\\d*))π");
    Matcher match;

    while ((match = pattern.matcher (input)).find ())
      input = match.replaceFirst (match.group (1) + "×π");

    Pattern pattern0 = Pattern.compile ("π((\\d+)([\\.]?)(\\d*))");
    Matcher match0;

    while ((match0 = pattern0.matcher (input)).find ())
      input = match0.replaceAll ("π×" + match0.group (1));

    return input.replaceAll ("π", "" + PI);
  }

  private static final String ans (String input) {
    if (!input.contains ("ans"))
      return input;

    Pattern pattern = Pattern.compile ("((\\d)([²³!]))ans");
    Matcher match;

    while ((match = pattern.matcher (input)).find ())
      input = match.replaceFirst (match.group (1) + "×ans");

    Pattern pattern0 = Pattern.compile ("ans((\\d+)([\\.]?)(\\d*))");
    Matcher match0;

    while ((match0 = pattern0.matcher (input)).find ())
      input = match0.replaceAll ("ans×" + match0.group (1));

    return input.replaceAll ("ans", ANS);
  }

  private static final String val (String input) {
    input = brackets (input);
    input = squared (input);
    input = cubed (input);
    input = factorial (input);
    input = squareRoot (input);
    input = divide (input);
    input = multiply (input);
    input = add (input);
    input = subtract (input);

    return input;
  }

  private static final String of (String input) {
    if (!input.contains ("("))
      return input;

    input = squared (input);
    input = cubed (input);
    input = squareRoot (input);
    input = factorial (input);

//    * Add new methods added in the
//      program here responsibly.

//    * At least all expressions that
//      are considered as one number eg 
//      [ 2² ],above,...

    Pattern p = Pattern.compile ("\\)((\\d+)([\\.]?)(\\d*))");
    Matcher m;

    while ((m = p.matcher (input)).find ())
      input = m.replaceFirst (")×" + m.group (1));

    Pattern pattern = Pattern.compile ("(([-+]?)(\\d+)([\\.]?)(\\d*))?(([√]*)\\(([^()]*)\\)([²³!]*))");
    Matcher matcher;

//    * Don't forget to add some
//      of those symbols to the last
//      group of the above regex.

//    * This is to enable brackets be
//      grouped together with them. eg
//      [ 2(1+1)² ],etc...
//      * Well check the whole regex again
//        to make sure all added methods are
//        computed correctly with this regex.
//        i.e [ √(...)(...) ],...

    while ((matcher = pattern.matcher (input)).find ())
      if (matcher.group (1) != null && matcher.group (6) != null)
        input = matcher.replaceFirst (multiply (matcher.group (1) + "×" + val (matcher.group (6))));
      else if (matcher.group (6) != null)
        input = matcher.replaceFirst (val (matcher.group (6)));

    return input;
  }

  private static final String brackets (String input) {
    if (!input.contains ("("))
      return input;

    Pattern p = Pattern.compile ("\\((([-+]?)(\\d+)([.]?)(\\d*))\\)");
    String tmp = "";

    Matcher m;
    while ((m = p.matcher (input)).find ()) input = m.replaceFirst (m.group (1));

    Pattern pattern = Pattern.compile ("\\(([^()]*)\\)");
    Matcher match;

    while ((match = pattern.matcher (input)).find ()) {
      tmp = evaluate (match.group (1));
      input = match.replaceFirst (tmp);
    }
    return input;
  }

  private static final String add (String input) {
    if (!input.contains ("+"))
      return input;

    Pattern pattern = Pattern.compile ("(([-+]?)(\\d+)([\\.]?)(\\d*))\\+(([-+]?)(\\d+)([\\.]?)(\\d*))");
    BigDecimal decimal;
    Matcher match;

    while ((match = pattern.matcher (input)).find ()) {
      try (Scanner sc = new Scanner (match.group (1)); Scanner sca = new Scanner (match.group (6))) {
        decimal = sc.nextBigDecimal ().add (sca.nextBigDecimal ());
      }
      input = match.replaceFirst (decimal.toPlainString ().startsWith ("-") ? decimal.toPlainString () : "+" + decimal.toPlainString ());
    }

    return input;
  }

  private static final String subtract (String input) {
    if (!input.contains ("-"))
      return input;

    Pattern pattern = Pattern.compile ("(([-+]?)(\\d+)([\\.]?)(\\d*))-(([-+]?)(\\d+)([\\.]?)(\\d*))");
    BigDecimal decimal;
    Matcher match;

    while ((match = pattern.matcher (input)).find ()) {
      try (Scanner sc = new Scanner (match.group (1)); Scanner sca = new Scanner (match.group (6))) {
        decimal = sc.nextBigDecimal ().subtract (sca.nextBigDecimal ());
      }
      input = match.replaceFirst (decimal.toPlainString ().startsWith ("-") ? decimal.toPlainString () : "+" + decimal.toPlainString ());
    }
    return input;
  }

  private static final String multiply (String input) {
    if (!input.contains ("×"))
      return input;

    Pattern pattern = Pattern.compile ("(([-+]?)(\\d+)([\\.]?)(\\d*))×(([-+]?)(\\d+)([\\.]?)(\\d*))");
    BigDecimal decimal;
    Matcher match;

    while ((match = pattern.matcher (input)).find ()) {
      try (Scanner sc = new Scanner (match.group (1)); Scanner sca = new Scanner (match.group (6))) {
        decimal = sc.nextBigDecimal ().multiply (sca.nextBigDecimal ());
      }
      input = match.replaceFirst (decimal.toPlainString ().startsWith ("-") ? decimal.toPlainString () : "+" + decimal.toPlainString ());
    }
    return input;
  }

  private static final String divide (String input) {
    if (!input.contains ("÷"))
      return input;

    Pattern pattern = Pattern.compile ("(([-+]?)(\\d+)([\\.]?)(\\d*))÷(([-+]?)(\\d+)([\\.]?)(\\d*))");
    BigDecimal decimal;
    Matcher match;

    while ((match = pattern.matcher (input)).find ()) {
      try {
        try (Scanner sc = new Scanner (match.group (1)); Scanner sca = new Scanner (match.group (6))) {
          decimal = sc.nextBigDecimal ().divide (sca.nextBigDecimal ());
        }
      }
      catch (ArithmeticException e) {
        decimal = BigDecimal.valueOf (Double.valueOf (match.group (1)) / Double.valueOf (match.group (6)));
      }
      input = match.replaceFirst (decimal.toPlainString ().startsWith ("-") ? decimal.toPlainString () : "+" + decimal.toPlainString ());
    }
    return input;
  }

  private static final String squared (String input) {
    if (!input.contains ("²"))
      return input;

    Pattern pattern = Pattern.compile ("(([-+]?)(\\d+)([\\.]?)(\\d*))²");
    BigDecimal decimal;
    Matcher match;

    while ((match = pattern.matcher (input)).find ()) {
      try (Scanner sc = new Scanner (match.group (1))) {
        decimal = sc.nextBigDecimal ().pow (2);
      }
      input = match.replaceFirst (decimal.toPlainString ().startsWith ("-") ? decimal.toPlainString () : "+" + decimal.toPlainString ());
    }
    return input;
  }

  private static final String cubed (String input) {
    if (!input.contains ("³"))
      return input;

    Pattern pattern = Pattern.compile ("(([-+]?)(\\d+)([\\.]?)(\\d*))³");
    BigDecimal decimal;
    Matcher match;

    while ((match = pattern.matcher (input)).find ()) {
      try (Scanner sc = new Scanner (match.group (1))) {
        decimal = sc.nextBigDecimal ().pow (3);
      }
      input = match.replaceFirst (decimal.toPlainString ().startsWith ("-") ? decimal.toPlainString () : "+" + decimal.toPlainString ());
    }
    return input;
  }

  private static final String squareRoot (String input) {
    if (!input.contains ("√"))
      return input;

    Pattern pattern = Pattern.compile ("(([-+]?)(\\d+)([\\.]?)(\\d*))?√(([-+]?)(\\d+)([\\.]?)(\\d*))");
    BigDecimal decimal;
    Matcher match;

    while ((match = pattern.matcher (input)).find ()) {
      decimal = BigDecimal.valueOf (sqrt (Double.valueOf (match.group (6))));

      if (match.group (1) != null)
        input = match.replaceFirst (multiply (match.group (1) + "×" + decimal.toPlainString ()));
      else input = match.replaceFirst (decimal.toPlainString ().startsWith ("-") ? decimal.toPlainString () : "+" + decimal.toPlainString ());
    }
    return input;
  }

  private static final String factorial (String input) {
    if (!input.contains ("!"))
      return input;

    final int MAX_FACTORIAL_VALUE = 50;
    Pattern pattern = Pattern.compile ("((\\d+)([\\.]?)(\\d*))!");
    BigInteger integer;
    Matcher matcher;

    while ((matcher = pattern.matcher (input)).find ()) {
      try (Scanner sc = new Scanner (matcher.group (1))) {
        BigInteger tmp = sc.nextBigInteger ();

        if (tmp.min (BigInteger.ZERO).equals (tmp))
          throw new InputMismatchException ("Factorial error! Less than 0.");
        else if (tmp.min (BigInteger.valueOf (MAX_FACTORIAL_VALUE)).equals (tmp))
          integer = tmp;
        else
          throw new InputMismatchException ("Factorial error! Exceeds max size.");
      }

      if (!integer.equals (BigInteger.ONE))
        for (BigInteger i = integer.subtract (BigInteger.ONE); !i.equals (BigInteger.ONE); i = i.subtract (BigInteger.ONE))
          integer = integer.multiply (i);

      input = matcher.replaceFirst (integer.toString ());
    }
    return input;
  }
}
