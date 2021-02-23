package jorex.programs.bi;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Interpreter for brainfuck code...
 *
 * @version 1.0
 * @author Jore
 */
public final class BrainfuckInterpreter {
  public static final String USAGE_COMMAND_LINE = new StringBuffer ()
  .append (" Usage: [command] [options] [<file-names>...]                           \n\n")
  .append (" Interpret brainfuck code.                                              \n\n")
  .append (" The command represents the command used to excecute this program e.g   \n\n")
  .append ("$ java -jar ./BrainfuckInterpreter/jorexbinterpreter.jar                \n\n")
  .append ("or                                                                      \n\n")
  .append ("$ java -cp ./BrainfuckInterpreter jorex.programs.bi.BrainfuckInterpreter\n\n")
  .append ("Options include:                                                        \n\n")
  .append ("  -i, --null-in=<character>                                               \n")
  .append ("                    Use this character to input a null value.             \n")
  .append ("  -o, --null-out=<character>                                              \n")
  .append ("                    Output this character in place of a null value.       \n")
  .append ("  -h, --help                                                              \n")
  .append ("                    Print this message and exit.                        \n\n")
  .append ("  The file names supplied are excecuted in the order in which they are    \n")
  .append ("supplied, with all input read from System.in and all output printed to    \n")
  .append ("System.out.                                                               \n")
  .append ("  Combining of short options e.g [-io [args]] is allowed and all arguments\n")
  .append ("for the combined options (if any) must immediately follow the combined    \n")
  .append ("options in their respective order.                                        \n")
  .append ("  All supplied arguments excluding the above are considered to be file    \n")
  .append ("names.                                                                    \n")
  .append ("  Incase multiple options resolving to the same function are supplied, the\n")
  .append ("last option overwrites any preceding ones.                                \n")
  .toString ();

  public static void main (String... args) {
    char nullIn = 0, nullOut = 0;
    ArrayList<String> fileNames = new ArrayList<> ();
    String prompt = " E: Option %s requires a(n) %s argument.\n Use '-h' or '--help' for more information.\n";
    for (int i = 0; i < args.length; i++) {
      String arg = args [i];
      if (arg.startsWith ("--null-in"))
        try {
          nullIn = arg.split ("=") [1].charAt (0);
        }
        catch (Throwable t) {
          System.err.printf (prompt, arg.split ("=") [0].replace ("=", ""), "character");
          return;
        }
      else if (arg.startsWith ("--null-out"))
        try {
          nullOut = arg.split ("=") [1].charAt (0);
        }
        catch (Throwable t) {
          System.err.printf (prompt, arg.split ("=") [0].replace ("=", ""), "character");
          return;
        }
      else if (arg.equals ("--help")) {
        System.out.println (USAGE_COMMAND_LINE);
        return;
      }
      else if (arg.charAt (0) == '-')
        for (char c : arg.toCharArray ())
          switch (c) {
            case 'i':
              try {
                nullIn = args [++i].charAt (0);
              }
              catch (Throwable t) {
                System.err.printf (prompt, c, "character");
                return;
              }
              continue;
            case 'o':
              try {
                nullOut = args [++i].charAt (0);
              }
              catch (Throwable t) {
                System.err.printf (prompt, c, "character");
                return;
              }
              continue;
            case 'h':
              System.out.println (USAGE_COMMAND_LINE);
              return;
            case '-':
              continue;
            default:
              System.err.printf (" E: Unknown option '-%s'.\n Use '-h' or '--help' for more information.\n", c);
              return;
          }
      else
        fileNames.add (arg);
    }
    if (fileNames.isEmpty ())
      try (Scanner sc = new Scanner (System.in)) {
        System.out.println (" Use 'q' or 'Q' to quit!");
        String code;
        while (true) {
          System.out.print ("\n Input brainfuck code:\n > ");
          code = sc.nextLine ().trim ();
          while (code.isEmpty ())
            code = sc.nextLine ().trim ();
          if (code.equalsIgnoreCase ("q"))
            return;
          else
            try {
              interpret (code, nullIn, nullOut);
            }
            catch (Throwable t) {
              System.err.printf ("\n E: Error interpreting code.\n %s\n", t);
            }
        }
      }
    else
      for (String name : fileNames)
        try {
          final StringBuffer buffer = new StringBuffer ();
          try (BufferedReader reader = new BufferedReader (new InputStreamReader (new FileInputStream (name)))) {
            Stream<String> lines = reader.lines ();
            lines.forEach (new Consumer<String> () {
                @Override
                public void accept (String s) {
                  buffer.append (s);
                }
              });
            System.out.printf ("\n\n %s\n\n", name);
            interpret (buffer.toString (), nullIn, nullOut);
            System.out.println ('\n');
          }
        }
        catch (FileNotFoundException fnfe) {
          System.err.printf ("\n E: Skipping '%s'. File Not Found.\n\n", name);
        }
        catch (Throwable t) {
          System.err.printf ("\n E: Error interpreting '%s'. %s.\n\n", name, t);
        }
  }
  public static final void interpret (String code) throws IOException {
    interpret (code, (char) 0, (char) 0);
  }
  public static final void interpret (String code, char nullIn, char nullOut) throws IOException {
    int loops = 0, pointer = 0, limit = 65535;
    char[] chars = code.toCharArray ();
    byte[] blocks = new byte[limit];
    for (int i = 0; i < chars.length; i++)
      switch (chars [i]) {
        case '>':
          if (pointer == limit - 1)
            pointer = 0;
          else
            pointer++;
          continue;
        case '<':
          if (pointer == 0)
            pointer = limit - 1;
          else
            pointer--;
          continue;
        case '+':
          blocks [pointer]++;
          continue;
        case '-':
          blocks [pointer]--;
          continue;
        case '.':
          char c = (char) (blocks [pointer]);
          System.out.print (c == 0 ? nullOut : c);
          continue;
        case ',':
          byte b = (byte) System.in.read ();
          blocks [pointer] = b == nullIn ? 0 : b;
          continue;
        case '[':
          if (blocks [pointer] == 0) {
            i++;
            while (loops > 0 || chars [i] != ']') {
              if (chars [i] == '[')
                loops++;
              else if (chars [i] == ']')
                loops--;
              i++;
            }
          }
          continue;
        case ']':
          if (blocks [pointer] != 0) {
            i--;
            while (loops > 0 || chars [i] != '[') {
              if (chars [i] == ']')
                loops++;
              else if (chars [i] == '[')
                loops--;
              i--;
            }
            i--;
          }
      }
  }
}
