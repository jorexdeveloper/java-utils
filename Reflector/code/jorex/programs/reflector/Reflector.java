package jorex.programs.reflector;

import java.io.FileWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Principal;
import java.security.ProtectionDomain;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Program that uses reflection to print all features of a class.
 *
 * @version 1.0
 * @author Jore
 */
public final class Reflector {
  private static final Logger LOGGER = Logger.getLogger ("jorex.programs.reflector.Reflector");

  public static final String USAGE_COMMAND_LINE = new StringBuffer ()
  .append (" Usage: [command] [options] [<full-class-names>...]                  \n\n")
  .append (" Print the details of (a) java class(es) using reflection.           \n\n")
  .append (" The command represents the command used to excecute this program e.g\n\n")
  .append ("$ java -jar ./Reflector/jorexreflector.jar                           \n\n")
  .append ("or                                                                   \n\n")
  .append ("$ java -cp ./Reflector jorex.programs.reflector.Reflector            \n\n")
  .append ("Options include:                                                     \n\n")
  .append ("  -A, --all                                                            \n")
  .append ("                    Print all details. Same as [-dfcmisI]. Used by     \n")
  .append ("                    default if no option is specified.                 \n")
  .append ("  -a, --almost-all                                                     \n")
  .append ("                    Print all details excluding class information and  \n")
  .append ("                    supers. Same as [-dfcmi]                           \n")
  .append ("  -d, --declaration                                                    \n")
  .append ("                    Include class declaration.                         \n")
  .append ("  -f, --fields                                                         \n")
  .append ("                    Include class fields.                              \n")
  .append ("  -c, --constructors                                                   \n")
  .append ("                    Include class constructors.                        \n")
  .append ("  -m, --methods                                                        \n")
  .append ("                    Include class methods.                             \n")
  .append ("  -i, --inner-classes                                                  \n")
  .append ("                    Include inner classes.                             \n")
  .append ("  -s, --super                                                          \n")
  .append ("                    Include inherited details.                         \n")
  .append ("  -I, --info                                                           \n")
  .append ("                    Include class and package information.             \n")
  .append ("  -[0-9], --indent=[0-9]                                               \n")
  .append ("                    Indent the output by 'n' spaces (default is 2).    \n")
  .append ("  -t, --time                                                           \n")
  .append ("                    Include the approximate time used to reflect each  \n")
  .append ("                    class.                                             \n")
  .append ("  -F, --file=<name>                                                    \n")
  .append ("                    Print all the output to the given file name, or if \n")
  .append ("                    no file name is given, each class details in a     \n")
  .append ("                    separate file named according to                   \n")
  .append ("                    [[<full-class-name>].txt] in the current directory.\n")
  .append ("  -h, --help                                                           \n")
  .append ("                    Print this message and exit.                     \n\n")
  .append ("  The details of the class names supplied are by default printed to    \n")
  .append ("System.out in the order in which they are supplied or if no class names\n")
  .append ("are supplied as arguments, they are read from System.in.               \n")
  .append ("  If the -F option is supplied, all the output is printed to the file  \n")
  .append ("with the given name or if no file name is given, each class details are\n")
  .append ("printed to a separate file named according to [[<full-class-name>].txt]\n")
  .append ("in the current directory.                                            \n\n")
  .append (" Note: Any files found with the same name as the output file name will \n")
  .append ("       be appended to.                                               \n\n")
  .append ("  Combining of short options e.g [-dfcmiItF [args]]  is allowed and all\n")
  .append ("arguments for the combined options (if any) must immediately follow the\n")
  .append ("combined options in their respective order.                            \n")
  .append ("  All supplied arguments excluding the above are considered to be class\n")
  .append ("names.                                                                 \n")
  .append ("  Incase multiple options resolving to the same function are supplied, \n")
  .append ("the last option overwrites any preceding ones.                         \n")
  .toString ();

  private static String indent = "  ";

  public static void main (String[] args) throws Throwable {
    boolean
      A = false, a = false, d = false, f = false, c = false, m = false, i = false,s = false, I = false, F = false, t = false;
    ArrayList<String> classNames = new ArrayList<> ();
    LOGGER.addHandler (new FileHandler ());
    String fileName = null;

    for (int j = 0; j < args.length; j++) {
      String arg = args [j];

      if (arg.equals ("-A") || arg.equals ("--all"))
        A = true;
      else if (arg.equals ("-a") || arg.equals ("--almost-all"))
        a = true;
      else if (arg.equals ("-d") || arg.equals ("--declaration"))
        d = true;
      else if (arg.equals ("-f") || arg.equals ("--fields"))
        f = true;
      else if (arg.equals ("-c") || arg.equals ("--constructors"))
        c = true;
      else if (arg.equals ("-m") || arg.equals ("--methods"))
        m = true;
      else if (arg.equals ("-i") || arg.equals ("--inner-classes"))
        i = true;
      else if (arg.equals ("-s") || arg.equals ("--super"))
        s = true;
      else if (arg.equals ("-I") || arg.equals ("--info"))
        I = true;
      else if (arg.equals ("-t") || arg.equals ("--time"))
        t = true;
      else if (arg.equals ("-h") || arg.equals ("--help")) {
        System.out.println (USAGE_COMMAND_LINE);
        return;
      }
      else if (arg.equals ("-F")) {
        F = true;
        if ((args.length - j) > 1)
          fileName = args [++j];
      }
      else if (arg.startsWith ("--file")) {
        F = true;
        try {
          fileName = arg.split ("=") [1];
        }
        catch (Throwable th) {}
      }
      else if (arg.startsWith ("--indent"))
        try {
          StringBuffer b = new StringBuffer ();
          for (int k = Integer.parseInt (arg.split ("=") [1]); k > 0; k--)
            b.append (' ');
          indent = b.toString ();
        }
        catch (Throwable th) {
          System.out.println (" E: Option --indent requires an int argument.");
          return;
        }
      else if (arg.charAt (0) == '-') {
        boolean isClass = true;
        for (char ch: arg.substring (1).toCharArray ()) {
          if (Character.isDigit (ch)) {
            isClass = false;
            indent (Integer.parseInt (Character.toString (ch)));
            continue;
          }
          else
            switch (ch) {
              case 'A':
                isClass = false;
                A = true;
                continue;
              case 'a':
                isClass = false;
                a = true;
                continue;
              case 'd':
                isClass = false;
                d = true;
                continue;
              case 'f':
                isClass = false;
                f = true;
                continue;
              case 'c':
                isClass = false;
                c = true;
                continue;
              case 'm':
                isClass = false;
                m = true;
                continue;
              case 'i':
                isClass = false;
                i = true;
                continue;
              case 's':
                isClass = false;
                s = true;
                continue;
              case 'I':
                isClass = false;
                I = true;
                continue;
              case 'F':
                isClass = false;
                F = true;
                if ((args.length - j) > 1)
                  fileName = args [++j];
                continue;
              case 't':
                isClass = false;
                t = true;
                continue;
              case 'h':
                System.out.println (USAGE_COMMAND_LINE);
                return;
            }
        }
        if (isClass)
          classNames.add (arg);
      }
      else
        classNames.add (arg);
    }
    if (classNames.size () < 1)
      try (Scanner sc = new Scanner (System.in)) {
        System.out.print (" Use 'q' or 'Q' to quit!\n\n > ");
        String next = sc.next ();
        if (next.equalsIgnoreCase ("q"))
          return;
        else
          classNames.add (next);
      }
    if (!A && !a && !d && !f && !c && !m && !i && !s && !I)
      A = true;
    String end = "\n\n/**\n * Approximated reflection time : %dms\n */";
    boolean append = false;
    StringBuffer buffer;
    FileWriter writer;
    for (String name : classNames)
      try {
        System.out.printf ("\n Attempting reflection of class `%s`...\n", name);
        long time = System.currentTimeMillis ();
        buffer = new StringBuffer ("\n");
        Class<?> aClass = Class.forName (name);
        if (aClass.isAnonymousClass ()) {
          if (A || I) {
            buffer
              .append ("\n/**\n * Anonymous Class\n * ---------------\n * declared in class : ")
              .append (reflectDeclaration (aClass.getEnclosingClass (), true));
            Constructor<?> constructor = aClass.getEnclosingConstructor ();
            if (constructor != null)
              buffer
                .append ("\n * declared in constructor : ")
                .append (constructor (constructor, "\n */\n\n"));
            else {
              Method method = aClass.getEnclosingMethod ();
              buffer
                .append ("\n * declared in method : ")
                .append (method (method, "\n *", "\n */\n\n"));
            }
          }
          if (A || s)
            buffer
              .append (reflectSupers (aClass))
              .append ("\n");
        }
        else if ((aClass.isLocalClass () || aClass.isMemberClass ()) && (A || d))
          buffer
            .append ("/**\n * ")
            .append (aClass.getName ())
            .append ("\n */");
        else {
          if (A || I)
            buffer
              .append (reflectClassInfo (aClass))
              .append ("\n\n");
          if (A || s)
            buffer
              .append (reflectSupers (aClass))
              .append ("\n\n");
          if (A || a || d) {
            Package mpackage = aClass.getPackage ();
            String packagename = mpackage.getName ();
            if (packagename.isEmpty ())
              buffer.append ("//");
            buffer
              .append (annotations (mpackage.getDeclaredAnnotations (), "/** ", " */\n"))
              .append ("package ")
              .append (Reflector.<String>requireNonNullOrEmptyElse (packagename, "<unnamed package>"))
              .append (";\n");
          }
        }
        if (A || a || d)
          buffer
            .append ('\n')
            .append (reflectDeclaration (aClass, false))
            .append (' ');
        buffer.append ("{");
        String string;
        if (A || a || f) {
          string = reflectFields (aClass);
          if (!string.isEmpty ())
            buffer
              .append ('\n')
              .append (string);
        }
        if (A || a || c) {
          string = reflectConstructors (aClass);
          if (!string.isEmpty ())
            buffer
              .append ('\n')
              .append (string);
        }
        if (A || a || m) {
          string = reflectMethods (aClass);
          if (!string.isEmpty ())
            buffer
              .append ('\n')
              .append (string);
        }
        if (A || a || i) {
          string = reflectInnerclasses (aClass);
          if (!string.isEmpty ())
            buffer
              .append ('\n')
              .append (string);
        }
        buffer.append ('}').toString ();
        if (t)
          buffer.append (String.format (end, System.currentTimeMillis () - time));
        if (F) {
          writer = new FileWriter (fileName == null ? name + ".txt" : fileName, true);
          if (append && fileName != null)
            writer.write ("\n\n\n");
          else
            append = true;
          writer.write (buffer.toString ());
          writer.close ();
        }
        else
          System.out.printf ("%s\n\n\n", buffer);
      }
      catch (ClassNotFoundException cnfe) {
        System.err.printf ("\n E: Skipping class `%s`. Class not found.\n", name);
      }
      catch (Throwable th) {
        System.err.printf ("\n E: Error reflecting class `%s`, %s.\n", name, th.getMessage ());
        buffer = new StringBuffer (th.toString ());
        StackTraceElement[] elements = th.getStackTrace ();
        for (StackTraceElement element : elements)
          buffer
            .append ("\n\tat : ")
            .append (element.toString ());
        LOGGER.severe (buffer.append ('\n').toString ());
      }
  }
  public static final synchronized String reflectClass (String className) throws ClassNotFoundException {
    return reflectClass (Class.forName (className));
  }
  public static final synchronized String reflectClass (Class<?> aClass) {
    StringBuffer buffer = new StringBuffer ();
    buffer
      .append (resolveClass (aClass))
      .append ('\n')
      .append (reflectDeclaration (aClass, false))
      .append (" {");
    String string = reflectFields (aClass);
    if (!string.isEmpty ())
      buffer
        .append ('\n')
        .append (string);
    string = reflectConstructors (aClass);
    if (!string.isEmpty ())
      buffer
        .append ('\n')
        .append (string);
    string = reflectMethods (aClass);
    if (!string.isEmpty ())
      buffer
        .append ('\n')
        .append (string);
    string = reflectInnerclasses (aClass);
    if (!string.isEmpty ())
      buffer
        .append ('\n')
        .append (string);
    return buffer.append ('}').toString ();
  }
  public static final String multiThreadReflectClass (String className) throws ClassNotFoundException, ExecutionException, InterruptedException {
    return multiThreadReflectClass (Class.forName (className));
  }
  public static final String multiThreadReflectClass (final Class<?> aClass) throws ExecutionException, InterruptedException {
    StringBuffer buffer = new StringBuffer ();
    ExecutorService threadpool = Executors.newFixedThreadPool (4);
    Future<String> f0 = threadpool.submit (new Callable<String> () {
        @Override
        public String call () throws Exception {
          StringBuffer buffer = new StringBuffer (reflectFields (aClass));
          return buffer.length () < 1 ? "" : buffer.insert (0, '\n').toString ();
        }
      });
    Future<String> f1 = threadpool.submit (new Callable<String> () {
        @Override
        public String call () throws Exception {
          StringBuffer buffer = new StringBuffer (reflectConstructors (aClass));
          return buffer.length () < 1 ? "" : buffer.insert (0, '\n').toString ();
        }
      });
    Future<String> f2 = threadpool.submit (new Callable<String> () {
        @Override
        public String call () throws Exception {
          StringBuffer buffer = new StringBuffer (reflectMethods (aClass));
          return buffer.length () < 1 ? "" : buffer.insert (0, '\n').toString ();
        }
      });
    Future<String> f3 = threadpool.submit (new Callable<String> () {
        @Override
        public String call () throws Exception {
          StringBuffer buffer = new StringBuffer (reflectInnerclasses (aClass));
          return buffer.length () < 1 ? "" : buffer.insert (0, '\n').toString ();
        }
      });
    threadpool.shutdown ();
    buffer
      .append (resolveClass (aClass))
      .append ('\n')
      .append (reflectDeclaration (aClass, false))
      .append (" {")
      .append (f0.get ())
      .append (f1.get ())
      .append (f2.get ())
      .append (f3.get ());
    return buffer.append ('}').toString ();
  }
  public static final synchronized String reflectClassInfo (String className) throws ClassNotFoundException {
    return reflectClassInfo (Class.forName (className));
  }
  public static final synchronized String reflectClassInfo (Class<?> aClass) {
    StringBuffer buffer = new StringBuffer ();
    ProtectionDomain domain = aClass.getProtectionDomain ();
    if (domain != null) {
      buffer
        .append ("\n/**\n * Assertion Status : ")
        .append (aClass.desiredAssertionStatus ());
      CodeSource codesource = domain.getCodeSource ();
      if (codesource != null) {
        buffer
          .append ("\n * Location : ")
          .append (codesource.getLocation ());
        CodeSigner[] codesigners = domain.getCodeSource ().getCodeSigners ();
        buffer.append ("\n *\n *    Code Signers\n *    ------------");
        if (codesigners != null && codesigners.length > 0)
          for (CodeSigner codesigner : codesigners)
            buffer
              .append ("\n * ")
              .append (codesigner);
        else
          buffer.append ("\n * <no code signers>");
        Certificate[] certificates = domain.getCodeSource ().getCertificates ();
        buffer.append ("\n *\n *    Certificates\n *    ------------");
        if (certificates != null && certificates.length > 0)
          for (Certificate certificate : certificates)
            buffer
              .append ("\n * ")
              .append (certificate);
        else
          buffer.append ("\n * <no certificates>");
      }
      PermissionCollection permissioncollection = domain.getPermissions ();
      buffer.append ("\n *\n *    Protection Domain Permissions\n *    -----------------------------");
      if (permissioncollection != null) {
        buffer
          .append ("\n * Permissions read only : ")
          .append (permissioncollection.isReadOnly ());
        Enumeration<Permission> permcollenum = permissioncollection.elements ();
        while (permcollenum.hasMoreElements ())
          buffer
            .append ("\n * ")
            .append (permcollenum.nextElement ());
      }
      else
        buffer.append ("\n * <no permissions>");
      Principal[] principals = domain.getPrincipals ();
      buffer.append ("\n *\n *    Protection Domain Principals\n *    ----------------------------");
      if (principals != null && principals.length > 0)
        for (Principal principal : principals)
          buffer
            .append ("\n * ")
            .append (principal);
      else
        buffer.append ("\n * <no principals>");
      buffer
        .append ("\n *\n *    Class Annotations\n *    -----------------")
        .append (Reflector.<String>requireNonNullOrEmptyElse (annotations (aClass.getDeclaredAnnotations (), "\n * ", ""), "\n * <no annotations>"));
      Package classpackage = aClass.getPackage ();
      String none = "<none>";
      buffer
        .append ("\n *\n *    Package Information\n *    -------------------")
        .append ("\n * Name : ")
        .append (Reflector.<String>requireNonNullOrEmptyElse (classpackage.getName (), "<unnamed package>"))
        .append ("\n * Is Sealed : ")
        .append (classpackage.isSealed ())
        .append ("\n * Implementation Title : ")
        .append (Reflector.<String>requireNonNullOrEmptyElse (classpackage.getImplementationTitle (), none))
        .append ("\n * Implementation Version : ")
        .append (Reflector.<String>requireNonNullOrEmptyElse (classpackage.getImplementationVersion (), none))
        .append ("\n * Implementation Vendor : ")
        .append (Reflector.<String>requireNonNullOrEmptyElse (classpackage.getImplementationVendor (), none))
        .append ("\n * Specification Title : ")
        .append (Reflector.<String>requireNonNullOrEmptyElse (classpackage.getSpecificationTitle (), none))
        .append ("\n * Specification Version : ")
        .append (Reflector.<String>requireNonNullOrEmptyElse (classpackage.getSpecificationVersion (), none))
        .append ("\n * Specification Vendor : ")
        .append (Reflector.<String>requireNonNullOrEmptyElse (classpackage.getSpecificationVendor (), none))
        .append ("\n *\n *    Package Annotations\n *    -------------------")
        .append (Reflector.<String>requireNonNullOrEmptyElse (annotations (classpackage.getDeclaredAnnotations (), "\n * ", ""), "\n * <no annotations>"))
        .append ("\n */");
    }
    return buffer.toString ();
  }
  public static final synchronized String reflectSupers (String className) throws ClassNotFoundException {
    return reflectSupers (Class.forName (className));
  }
  public static final synchronized String reflectSupers (Class<?> aClass) {
    StringBuffer buffer = new StringBuffer ();
    Class<?>[] interfaces = aClass.getInterfaces ();
    boolean bool = aClass.isInterface ();
    if (bool)
      buffer.append ("/**\n *    Extended Interfaces\n *    -------------------");
    else
      buffer.append ("/**\n *    Implemented Interfaces\n *    ----------------------");
    if (interfaces != null && interfaces.length > 0)
      for (Class<?> minterface : interfaces) {
        buffer
          .append ("\n * ")
          .append (annotations (minterface.getDeclaredAnnotations (), "", "\n * "));
        int modifiers = minterface.getModifiers ();
        if (modifiers > 0)
          buffer
            .append (Modifier.toString (modifiers))
            .append (' ');
        buffer
          .append (minterface.getName ())
          .append (typeparameters (minterface.getTypeParameters ()))
          .append (' ')
          .append (genericinterfaces (minterface.getGenericInterfaces (), true));
      }
    else
    if (bool)
      buffer.append ("\n * <no extended interfaces>");
    else
      buffer.append ("\n * <no implemented interfaces>");
    Class<?> superclass = aClass.getSuperclass ();
    buffer.append ("\n * \n *    Super Classes\n *    -------------");
    if (superclass != null)
      while (superclass != null) {
        buffer
          .append ("\n * ")
          .append (reflectDeclaration (superclass, true));
        superclass = superclass.getSuperclass ();
      }
    else
      buffer.append ("\n * <no super classes>");
    Field[] fields = aClass.getFields ();
    bool = false;
    buffer.append ("\n * \n *    Super Fields\n *    ------------");
    if (fields != null && fields.length > 0)
      for (Field field : fields) {
        if (field.isEnumConstant () || field.getDeclaringClass () == aClass)
          continue;
        bool = true;
        buffer
          .append ("\n * ")
          .append (annotations (field.getDeclaredAnnotations (), "", "\n * "));
        int modifiers = field.getModifiers ();
        if (modifiers > 0)
          buffer
            .append (Modifier.toString (modifiers))
            .append (' ');
        buffer
          .append (type (field.getGenericType ()).replace ('$', '.'))
          .append (' ')
          .append (field.getName ())
          .append (';');
      }
    if (!bool)
      buffer.append ("\n * <no super fields>");
    Constructor<?>[] constructors = aClass.getConstructors ();
    bool = false;
    buffer.append ("\n * \n *    Super Constructors\n *    ------------------");
    if (constructors != null && constructors.length > 0)
      for (Constructor<?> constructor : constructors) {
        if (constructor.getDeclaringClass () == aClass)
          continue;
        bool = true;
        buffer
          .append ("\n * ")
          .append (annotations (constructor.getDeclaredAnnotations (), "", "\n * "))
          .append (constructor (constructor, " {}\n *"));
      }
    if (!bool)
      buffer.append ("\n * <no super constructors>");
    Method[] methods = aClass.getMethods ();
    bool = false;
    buffer.append ("\n * \n *    Super Methods\n *    -------------");
    if (methods != null && methods.length > 0)
      for (Method method : methods) {
        if (method.getDeclaringClass () == aClass)
          continue;
        bool = true;
        buffer.append ("\n * ")
          .append (annotations (method.getDeclaredAnnotations (), "", "\n * "))
          .append (method (method, ";\n *", " {}\n *"));
      }
    if (!bool)
      buffer.append ("\n * <no super methods>");
    buffer.append ("\n */");
    return buffer.toString ();
  }
  public static final synchronized String reflectDeclaration (String className, boolean useFullClassName) throws ClassNotFoundException {
    return reflectDeclaration (Class.forName (className), useFullClassName);
  }
  public static final synchronized String reflectDeclaration (Class<?> aClass, boolean useFullClassName) {
    StringBuffer buffer = new StringBuffer ();
    boolean isinterface = aClass.isInterface ();
    buffer.append (annotations (aClass.getDeclaredAnnotations (), "/** ", " */\n"));
    int modifiers = aClass.getModifiers ();
    if (modifiers > 0)
      buffer
        .append (Modifier.toString (modifiers))
        .append (' ');
    buffer
      .append (aClass.isEnum () ? "enum " : isinterface ? "" : "class ")
      .append (aClass.isAnonymousClass () ? "/* anonymous */" : useFullClassName ? aClass.getName () : aClass.getSimpleName ())
      .append (typeparameters (aClass.getTypeParameters ()));
    Type superclass = aClass.getGenericSuperclass ();
    if (superclass != null && !Objects.equals (Object.class, superclass) && !Objects.equals (Enum.class, superclass))
      buffer
        .append (" extends ")
        .append (type (superclass));
    buffer
      .append (' ')
      .append (genericinterfaces (aClass.getGenericInterfaces (), isinterface));
    return buffer.toString ();
  }
  public static final synchronized String reflectFields (String className) throws ClassNotFoundException {
    return reflectFields (Class.forName (className));
  }
  public static final synchronized String reflectFields (Class<?> aClass) {
    StringBuffer buffer = new StringBuffer ();
    Field[] fields = aClass.getDeclaredFields ();
    boolean fieldsaccessible = false;
    try {
      AccessibleObject.setAccessible (fields, true);
      fieldsaccessible = true;
    }
    catch (Throwable throwable) {
      LOGGER.warning ("Error retrieving field access!\n");
    }
    if (fields != null && fields.length > 0)
      for (Field field : fields) {
        if (field.isEnumConstant ())
          continue;
        buffer
          .append ('\n')
          .append (indent)
          .append (annotations (field.getDeclaredAnnotations (), "/** ", " */\n" + indent));
        int modifiers = field.getModifiers ();
        if (modifiers > 0)
          buffer
            .append (Modifier.toString (modifiers))
            .append (' ');
        buffer
          .append (type (field.getGenericType ()).replace ('$', '.'))
          .append (' ')
          .append (field.getName ());
        try {
          Object object = field.get (aClass);
          if (fieldsaccessible && object != null)
            buffer.append (object instanceof Character ? (" = '" + object + "'").replace ("\n", "\\n")
                           : object instanceof CharSequence ? (" = \"" + object + "\"").replace ("\n", "\\n")
                           : object.getClass ().isArray () ? " = " + array (object)
                           : field.getType ().isPrimitive () ? " = " + object
                           : "/* = " + object.toString ().replaceAll ("\n", "\\n") + " */");
        }
        catch (Throwable throwable) {
          LOGGER.info (new StringBuffer (" Error accessing field '")
                       .append (field.getName ())
                       .append ("' in class '")
                       .append (field.getDeclaringClass ().getName ())
                       .append ("'.\n")
                       .toString ());
        }
        buffer.append (";\n");
      }
    return buffer.toString ();
  }
  public static final synchronized String reflectConstructors (String className) throws ClassNotFoundException {
    return reflectConstructors (Class.forName (className));
  }
  public static final synchronized String reflectConstructors (Class<?> aClass) {
    StringBuffer buffer = new StringBuffer ();
    Constructor<?>[] constructors = aClass.getDeclaredConstructors ();
    if (constructors != null && constructors.length > 0)
      for (Constructor<?> constructor : constructors)
        buffer
          .append ('\n')
          .append (indent)
          .append (annotations (constructor.getDeclaredAnnotations (), "/** ", " */\n" + indent))
          .append (constructor (constructor, " {}\n"));
    return buffer.toString ();
  }
  public static final synchronized String reflectMethods (String className) throws ClassNotFoundException {
    return reflectMethods (Class.forName (className));
  }
  public static final synchronized String reflectMethods (Class<?> aClass) {
    StringBuffer buffer = new StringBuffer ();
    Method[] methods = aClass.getDeclaredMethods ();
    if (methods != null && methods.length > 0)
      for (Method method : methods) {
        buffer
          .append ('\n')
          .append (indent)
          .append ((method.isBridge () ? "/** Bridge method. */\n" + indent : method.isSynthetic () ? "/** Synthetic method. */\n" + indent : ""))
          .append (annotations (method.getDeclaredAnnotations (), "/** ", " */\n" + indent))
          .append (method (method, ";\n", " {}\n"));

        method.setAccessible (true);
      }
    return buffer.toString ();
  }
  public static final synchronized String reflectInnerClasses (String className) throws ClassNotFoundException {
    return reflectInnerclasses (Class.forName (className));
  }
  public static final synchronized String reflectInnerclasses (Class<?> aClass) {
    StringBuffer buffer = new StringBuffer ();
    Class<?>[] innerclasses = aClass.getClasses ();
    if (innerclasses != null && innerclasses.length > 0)
      for (Class<?> innerclass : innerclasses)
        try {
          Scanner scanner = new Scanner (reflectClass (innerclass.getName ()));
          while (scanner.hasNextLine ())
            buffer
              .append ('\n')
              .append (indent)
              .append (scanner.nextLine ());

          buffer.append ('\n');
          scanner.close ();
        }
        catch (Throwable throwable) {
          buffer
            .append ('\n')
            .append (indent)
            .append ("/**\n * Failed to load inner class '")
            .append (innerclass.getName ())
            .append ("'.\n")
            .append (indent)
            .append ("*/");
        }
    return buffer.toString ();
  }
  public static final void indent (int spaces) {
    StringBuffer b = new StringBuffer ();
    for (int i = spaces; i > 0; i--)
      b.append (' ');
    indent = b.toString ();
  }

  /* -------------------- HELPER METHODS -------------------- */
  private static final synchronized String resolveClass (Class<?> aClass) {
    StringBuffer buffer = new StringBuffer ();
    if (aClass.isAnonymousClass ()) {
      buffer
        .append ("\n/**\n * Anonymous Class\n * ---------------\n * declared in class : ")
        .append (reflectDeclaration (aClass.getEnclosingClass (), true));
      Constructor<?> constructor = aClass.getEnclosingConstructor ();
      if (constructor != null)
        buffer
          .append ("\n * declared in constructor : ")
          .append (constructor (constructor, "\n */\n\n"));
      else {
        Method method = aClass.getEnclosingMethod ();
        buffer
          .append ("\n * declared in method : ")
          .append (method (method, "\n *", "\n */\n\n"));
      }
      buffer
        .append (reflectSupers (aClass))
        .append ("\n");
    }
    else if (aClass.isLocalClass () || aClass.isMemberClass ())
      buffer
        .append ("/**\n * ")
        .append (aClass.getName ().replace ('$', '.'))
        .append ("\n */");
    else {
      buffer
        .append (reflectClassInfo (aClass))
        .append ("\n\n");
      buffer
        .append (reflectSupers (aClass))
        .append ("\n\n");
      Package mpackage = aClass.getPackage ();
      String packagename = mpackage.getName ();
      if (packagename.isEmpty ())
        buffer.append ("//");
      buffer
        .append (annotations (mpackage.getDeclaredAnnotations (), "/** ", " */\n"))
        .append ("package ")
        .append (Reflector.<String>requireNonNullOrEmptyElse (packagename, "<unnamed package>"))
        .append (";\n");
    }
    return buffer.toString ();
  }
  private static final synchronized String type (Type type) {
    StringBuffer buffer = new StringBuffer ();
    if (type instanceof Class) {
      Class<?> aclass = (Class<?>) type;
      if (aclass.isArray ())
        buffer.append (aclass.getTypeName ());
      else if (aclass.isLocalClass () || aclass.isMemberClass () || aclass.isAnonymousClass ())
        buffer.append (aclass.getSimpleName ());
      else
        buffer.append (aclass.getName ());
    }
    else if (type instanceof TypeVariable) {
      TypeVariable<?> typevariable = (TypeVariable<?>) type;
      buffer.append (typevariable.getName ());
      Type[] bounds = typevariable.getBounds ();
      if (bounds != null && bounds.length > 0 && !Arrays.equals (bounds, new Type[]{Object.class})) {
        buffer.append (" extends ");
        for (int i = 0; i < bounds.length; i++) {
          if (i > 0)
            buffer.append (" & ");
          buffer.append (type (bounds [i]));
        }
      }
    }
    else if (type instanceof WildcardType) {
      WildcardType wildcardtype = (WildcardType) type;
      buffer.append ('?');
      Type[] bounds = wildcardtype.getUpperBounds ();
      if (bounds != null && bounds.length > 0 && !Arrays.equals (bounds, new Type[]{Object.class})) {
        buffer.append (" extends ");
        for (int i = 0; i < bounds.length; i++) {
          if (i > 0)
            buffer.append (" & ");
          buffer.append (type (bounds [i]));
        }
      }
      bounds = wildcardtype.getUpperBounds ();
      if (bounds != null && bounds.length > 0 && !Arrays.equals (bounds, new Type[]{Object.class})) {
        buffer.append (" super ");
        for (int i = 0; i < bounds.length; i++) {
          if (i > 0)
            buffer.append (" & ");
          buffer.append (type (bounds [i]));
        }
      }
    }
    else if (type instanceof ParameterizedType) {
      ParameterizedType parameterizedtype = (ParameterizedType) type;
      Type owner = parameterizedtype.getOwnerType ();
      if (owner != null)
        buffer
          .append (type (owner))
          .append ('.');
      buffer.append (type (parameterizedtype.getRawType ()));
      Type[] args = parameterizedtype.getActualTypeArguments ();
      if (args != null && args.length > 0) {
        buffer.append ('<');
        for (int i = 0; i < args.length; i++) {
          if (i > 0)
            buffer.append (", ");
          buffer.append (type (args [i]));
        }
        buffer.append ('>');
      }
    }
    else if (type instanceof GenericArrayType) {
      GenericArrayType genericarraytype = (GenericArrayType) type;
      buffer
        .append (type (genericarraytype.getGenericComponentType ()))
        .append ("[]");
    }
    return buffer.toString ();
  }
  private static final synchronized String constructor (Constructor<?> constructor, String post) {
    StringBuffer buffer = new StringBuffer ();
    int modifiers = constructor.getModifiers ();
    if (modifiers > 0)
      buffer
        .append (Modifier.toString (modifiers))
        .append (' ');
    String typeparams = typeparameters (constructor.getTypeParameters ());
    buffer
      .append ((typeparams.isEmpty () ? "" : typeparams + ' '))
      .append (constructor.getDeclaringClass ().getSimpleName ())
      .append ('(')
      .append (parameters (constructor.getParameters ()))
      .append (')')
      .append (exceptions (constructor.getGenericExceptionTypes (), " throws "))
      .append (post);
    return buffer.toString ();
  }
  private static final synchronized String method (Method method, String postabstract, String postdeclared) {
    StringBuffer buffer = new StringBuffer ();
    int modifiers = method.getModifiers ();
    if (modifiers > 0)
      buffer
        .append (Modifier.toString (modifiers))
        .append (' ');
    String typeparams = typeparameters (method.getTypeParameters ());
    buffer
      .append ((typeparams.isEmpty () ? "" : typeparams + ' '))
      .append (type (method.getGenericReturnType ()).replace ('$', '.'))
      .append (' ')
      .append (method.getName ())
      .append ('(')
      .append (parameters (method.getParameters ()))
      .append (')')
      .append (exceptions (method.getGenericExceptionTypes (), " throws "))
      .append ((Modifier.isAbstract (modifiers) ? postabstract : postdeclared));
    return buffer.toString ();
  }
  private static final synchronized String array (Object array) {
    StringBuffer buffer = new StringBuffer ().append ('{');
    int length = Array.getLength (array);
    Object object;
    for (int i = 0; i < length; i++) {
      if (i > 0)
        buffer.append (", ");
      else
        buffer
          .append ("\n")
          .append (indent)
          .append (indent);
      object = Array.get (array, i);
      buffer.append ((object.getClass ().isArray () ? array (object)
                     : object instanceof Character ? '\'' + object.toString () + '\''
                     : object instanceof String ? '"' + object.toString () + '"' : object).toString ().replace ("\n", "\\n"));
      if (i == length - 1)
        buffer
          .append ('\n')
          .append (indent);
    }
    buffer.append ('}');
    return buffer.toString ();
  }
  private static final synchronized String genericinterfaces (Type[] interfaces, boolean isinterface) {
    StringBuffer buffer = new StringBuffer ();
    if (interfaces != null && interfaces.length > 0) {
      if (isinterface)
        buffer.append ("extends ");
      else
        buffer.append ("implements ");
      for (int i = 0; i < interfaces.length; i++) {
        if (i > 0)
          buffer.append (", ");
        buffer.append (type (interfaces [i]));
      }
    }
    return buffer.toString ();
  }
  /** Reflects enum constants but not used anywhere. */
  private static final synchronized String enumconstants (Class<?> aClass) {
    StringBuffer buffer = new StringBuffer ();
    Object[] enumconstants = aClass.getEnumConstants ();
    if (enumconstants != null && enumconstants.length > 0)
      for (int i = 0; i < enumconstants.length; i++) {
        if (i > 0)
          buffer.append (", ");
        buffer.append (enumconstants [i].toString ());
      }
    return buffer.toString ();
  }
  private static final synchronized String annotations (Annotation[] annotations, String pre, String post) {
    StringBuffer buffer = new StringBuffer ();
    if (annotations != null && annotations.length > 0)
      for (Annotation annotation : annotations)
        buffer
          .append (pre)
          .append (annotation)
          .append (post);
    return buffer.toString ();
  }
  private static final synchronized String typeparameters (TypeVariable<?>[] typeparams) {
    StringBuffer buffer = new StringBuffer ();
    if (typeparams != null && typeparams.length > 0) {
      buffer.append ('<');
      for (int i = 0; i < typeparams.length; i++) {
        if (i > 0)
          buffer.append (", ");
        buffer.append (type (typeparams [i]));
      }
      buffer.append ('>');
    }
    return buffer.toString ();
  }
  private static final synchronized String parameters (Parameter[] parameters) {
    StringBuffer buffer = new StringBuffer ();
    if (parameters != null && parameters.length > 0)
      for (int i = 0; i < parameters.length; i++) {
        if (i > 0)
          buffer.append (", ");
        buffer.append (parameters [i]);
      }
    return buffer.toString ().replace ('$', '.');
  }
  private static final synchronized String exceptions (Type[] exceptions, String pre) {
    StringBuffer buffer = new StringBuffer ();
    if (exceptions != null && exceptions.length > 0) {
      buffer.append (pre);
      for (int i = 0; i < exceptions.length; i++) {
        if (i > 0)
          buffer.append (", ");
        buffer.append (type (exceptions [i]));
      }
    }
    return buffer.toString ();
  }
  private static final synchronized <T> T requireNonNullOrEmptyElse (T obj0, T obj1) {
    if (obj0 != null && !obj0.toString ().isEmpty ())
      return obj0;
    else
      return obj1;
  }
}
