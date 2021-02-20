package jorex.programs.ntw;

import java.math.BigDecimal;
import java.util.Scanner;
import java.util.StringJoiner;

/**
 * Converts numbers to words...
 *
 * @version 1.0
 *
 * @author Jore-X
 */
public class NumberToWord {

  public static final int FORMAT_DEFAULT = 0;

  public static final int FORMAT_ALL_UPPERCASE = 1;

  public static final int FORMAT_FIRST_LETTER_UPPER_CASE = 2;

  public static final int FORMAT_FIRST_LETTER_UPPER_CASE_ALL = 3;

  public static final int FORMAT_FIRST_LETTER_UPPER_CASE_RANGE = 4;

  public static final String[] RANGES = new String[] {
    "hundred", "thousand", "million", "billion", "trillion", "quadrillion", "quintillion", "sextillion", "septillion", "octillion", "nonillion", "decillion", "undecillion", "duodecillion", "tredecillion", "quattuordecillion", "quindecillion", "sexdecillion", "septendecillion", "octodecillion", "novemdecillion", "vigintillion"
  };

  private static final String[] a = new String[] {
    "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"
  };

  private static final String[] b = new String[] {
    "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"
  };

  private static final String[] c = new String[] {
    "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"
  };

  //-------------------- PUBLIC METHODS --------------------

  public static final String numberToWord (BigDecimal number, String delimeter) {
    if (number.equals (BigDecimal.ZERO))
      return a [0];

    String[] split = number.toPlainString ().split ("\\.");
    StringBuffer buffer = new StringBuffer ();

    try {
      if (split [0].startsWith ("-"))
        buffer
          .append ("negative")
          .append (getWords (split [0].substring (1), delimeter));
      else
        buffer.append (getWords (split [0], delimeter));

      if (split.length > 1) {
        while (split [1].endsWith ("0"))
          split [1] = split [1].substring (0, (split [1].length () - 1));

        int length = split [1].length ();

        if (length > 0 && (BigDecimal.ZERO.compareTo (new Scanner (split [1]).nextBigDecimal ()) < 0)) {
          String result = getWords (split [1], delimeter);

          if (!result.isEmpty ()) {
            buffer
              .append (delimeter)
              .append ("and")
              .append (result)
              .append (delimeter);

            switch (length) {
              case 1:
                buffer.append (b [0]);
                break;

              case 2:
                buffer.append (RANGES [0]);
                break;

              default:
                for (int i = 3, j = 0, r = 1; true; i++) {
                  if (i == length) {
                    switch (j) {
                      case 1:
                        buffer
                          .append (b [0])
                          .append (delimeter);
                        break;

                      case 2:
                        buffer
                          .append (RANGES [0])
                          .append (delimeter);
                    }
                    buffer.append (RANGES [r]);
                    break;
                  }

                  if (j == 2)
                    j = 0;
                  else
                    j++;

                  if (j == 0)
                    r++;
                }
            }
            buffer.append ("th");
          }
        }
      }
      return buffer.toString ().startsWith (delimeter) ? buffer.substring (delimeter.length ()) : buffer.toString ();
    }
    catch (ArrayIndexOutOfBoundsException e) {
      return "E: Maximum range exceeded!";
    }
  }

  public static final String numberToWordFormatted (BigDecimal number, String delimeter, int format, int range) {
    String words = numberToWord (number, delimeter);
    StringBuffer buffer;

    if (words.length () > 1 && !words.substring (0, 2).equals ("E:"))
      switch (format) {
        case FORMAT_ALL_UPPERCASE:
          return words.toUpperCase ();

        case FORMAT_FIRST_LETTER_UPPER_CASE:
          buffer = new StringBuffer (words);
          return buffer.replace (0, 1, words.substring (0, 1).toUpperCase ()).toString ();

        case FORMAT_FIRST_LETTER_UPPER_CASE_ALL:
          buffer = new StringBuffer ();

          try (Scanner sc = new Scanner (words).useDelimiter (delimeter)) {
            while (sc.hasNext ()) {
              String s = sc.next ();

              buffer
                .append (buffer.length () < 1 ? "" : delimeter)
                .append (Character.toUpperCase (s.charAt (0)))
                .append (s.substring (1));
            }
            return buffer.toString ();
          }

        case FORMAT_FIRST_LETTER_UPPER_CASE_RANGE:
          ;
      }
    return words;
  }

  //-------------------- PRIVATE METHODS --------------------

  private static final String getWords (String num, String del) {
    int char0 = Integer.parseInt (Character.toString (num.charAt (0)));

    switch (num.length ()) {
      case 1:
        return del + a [Integer.parseInt (num)];

      case 2:
        int char1 = Integer.parseInt (Character.toString (num.charAt (1)));

        switch (char0) {
          case 0:
            return (char1 == 0 ? "" : del + a [char1]);

          case 1:
            return del + b [char1];

          default:
            return del + c [char0 - 2] + (char1 == 0 ? "" : del + a [char1]);
        }

      case 3:
        return (char0 == 0 ? "" : del + a [char0] + del + RANGES [0]) + getWords (num.substring (1), del);

      default:
        for (int length = 1, range = 0, subRange = 1; true; length++) {
          if (length == num.length ())
            switch (subRange) {
              case 1:
                return (char0 == 0 ? "" : del + a [char0] + del + RANGES [range]) + getWords (num.substring (1), del);

              default:
                return (char0 == 0 ? "" : getWords (num.substring (0, subRange), del) + del + RANGES [range]) + getWords (num.substring (subRange), del);
            }

          if ((length % 3) == 0)
            range++;

          if (subRange == 1 || subRange == 2)
            subRange++;

          else if (subRange == 3)
            subRange = 1;
        }
    }
  }
}
