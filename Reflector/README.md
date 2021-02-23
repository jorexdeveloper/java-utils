Reflector
---------

Program to view details of java classes using reflection.

### FEATURES

* Full Generic Support
* Organized output
* Detailed information

### REFLECTED INFORMATION

* Assertion Status
* Protection Domain Permissions
* Protection Domain Principals
* Class Annotations
* Package Information
* Package Annotations
* Implemented Interfaces
* Super Classes
* Super Fields
* Super Constructors
* Super Methods
* Class Declaration
* Class Fields
* Auto Generated Class Fields
* Class Constructors
* Auto Generated Class Constructors
* Class Methods
* Synthetic methods
* Bridge Methods
* Inner Classes

**Note :** An attempt will be made to reflect the values of all generated fields.

### INSTALLATION

Download and extract the zip from below.

> [JavaUtils.zip](https://github.com/jorexdeveloper/JavaUtils/archive/root.zip)

### USAGE

You can use this program both in your code and with the command line (requires at least **jre** installed).

#### CODE

To use this class in your java project, you **MUST ADD** the path `.../Reflector/` or `.../Reflector/jorexreflector.jar` to your class path replacing `...` with the path to the the directory `Reflector` found in the zip file in order to add the class to your project.

##### SYNTAX

```java
// Import the class
import jorex.programs.reflector.Reflector;

public class YourClass {

  public void yourMethod () {
    // Create a Class instance to be reflected
    Class<?> yourClass = this.getClass ();

    // Get the String containing class details
    // by invoking the static method reflectClass
    // passing to it a Class instance to be reflected
    String reflectedClass = Reflector.reflectClass (yourClass);

    // Use the String
    System.out.println (reflectedClass);
  }
}
```

##### FIELDS

```java
public static final String USAGE_COMMAND_LINE
```
This contains the full command line usage of this program.

##### METHODS

```java
public static final synchronized String reflectClass (Class<?> aClass)

public static final synchronized String reflectClass (String className) throws ClassNotFoundException
```
Takes a `Class<?>` instance/full class name as a `String` and returns a `String` containing the full details of the class.


```java
public static final String multiThreadReflectClass (final Class<?> aClass) throws ExecutionException, InterruptedException

public static final String multiThreadReflectClass (String className) throws ClassNotFoundException, ExecutionException, InterruptedException
```
Similar to the above methods but use more than one Thread to get the details of the class. (Only recommended for large classes)


```java
public static final synchronized String reflectClassInfo (Class<?> aClass)

public static final synchronized String reflectClassInfo (String className) throws ClassNotFoundException
```
Takes a `Class<?>` instance/full class name as a `String` and returns a `String` containing only the class and package information.


```java
public static final synchronized String reflectSupers (Class<?> aClass)

public static final synchronized String reflectSupers (String className) throws ClassNotFoundException
```
Takes a `Class<?>` instance/full class name as a `String` and returns a `String` containing only the inherited details e.g super class methods and fields, etc.


```java
public static final synchronized String reflectDeclaration (Class<?> aClass, boolean useFullClassName)

public static final synchronized String reflectDeclaration (String className, boolean useFullClassName) throws ClassNotFoundException
```
Takes a `Class<?>` instance/full class name as a `String` with a boolean denoting whether to use of the full or short class name and returns a `String` containing only the class declaration.


```java
public static final synchronized String reflectFields (Class<?> aClass)

public static final synchronized String reflectFields (String className) throws ClassNotFoundException
```
Takes a `Class<?>` instance/full class name as a `String` and returns a `String` containing only the class fields.


```java
public static final synchronized String reflectConstructors (Class<?> aClass)

public static final synchronized String reflectConstructors (String className) throws ClassNotFoundException
```
Takes a `Class<?>` instance/full class name as a `String` and returns a `String` containing only the class constructors.


```java
public static final synchronized String reflectMethods (Class<?> aClass)

public static final synchronized String reflectMethods (String className) throws ClassNotFoundException
```
Takes a `Class<?>` instance/full class name as a `String` and returns a `String` containing only the class methods.


```java
public static final synchronized String reflectInnerclasses (Class<?> aClass)

public static final synchronized String reflectInnerClasses (String className) throws ClassNotFoundException
```
Takes a `Class<?>` instance/full class name as a `String` and returns a `String` containing only the full details of all the inner classes.


```java
public static final void indent (int spaces)
```

> Default Value : 2

Takes an `int` and sets the number of spaces to indent the output for all the above methods.


#### COMMAND LINE

You must at least have **jre** installed on your device.

##### COMMAND

```shell
java [ -cp <class path> ] jorex.programs.reflector.Reflector
```

or

```shell
java -jar jorexreflector.jar
```

##### OPTIONS

Thre are different options that you can use together with the above command, here is a full list of the available command line options.

```
 Usage: [command] [options] [<full-class-names>...]                  

 Print the details of (a) java class(es) using reflection.           

 The command represents the command used to excecute this program e.g

$ java -jar ./Reflector/jorexreflector.jar                           

or                                                                   

$ java -cp ./Reflector jorex.programs.reflector.Reflector            

Options include:                                                     

  -A, --all                                                            
                    Print all details. Same as [-dfcmisI]. Used by     
                    default if no option is specified.                 
  -a, --almost-all                                                     
                    Print all details excluding class information and  
                    supers. Same as [-dfcmi]                           
  -d, --declaration                                                    
                    Include class declaration.                         
  -f, --fields                                                         
                    Include class fields.                              
  -c, --constructors                                                   
                    Include class constructors.                        
  -m, --methods                                                        
                    Include class methods.                             
  -i, --inner-classes                                                  
                    Include inner classes.                             
  -s, --super                                                          
                    Include inherited details.                         
  -I, --info                                                           
                    Include class and package information.             
  -[0-9], --indent=[0-9]                                               
                    Indent the output by 'n' spaces (default is 2).    
  -t, --time                                                           
                    Include the approximate time used to reflect each  
                    class.                                             
  -F, --file=<name>                                                    
                    Print all the output to the given file name, or if 
                    no file name is given, each class details in a     
                    separate file named according to                   
                    [[<full-class-name>].txt] in the current directory.
  -h, --help                                                           
                    Print this message and exit.                     

  The details of the class names supplied are by default printed to    
System.out in the order in which they are supplied or if no class names
are supplied as arguments, they are read from System.in.               
  If the -F option is supplied, all the output is printed to the file  
with the given name or if no file name is given, each class details are
printed to a separate file named according to [[<full-class-name>].txt]
in the current directory.                                            

 Note: Any files found with the same name as the output file name will 
       be appended to.                                               

  Combining of short options e.g [-dfcmiItF [args]]  is allowed and all
arguments for the combined options (if any) must immediately follow the
combined options in their respective order.                            
  All supplied arguments excluding the above are considered to be class
names.                                                                 
  Incase multiple options resolving to the same function are supplied, 
the last option overwrites any preceding ones.                         
```

##### EXAMPLE

Change to the directory `JavaUtils-root` (in the zip file), then excecute included class supplying any of the desired options above (options must be the last).

```shell
java -cp ./Reflector/ jorex.programs.reflector.Reflector
```

The `-cp ./Reflector/` argument sets the **jre** class path.

or excecute the jar with...

```shell
java -jar ./Reflector/jorexreflector.jar
```

Alternatively, you can supply class names and any of the above command line options as follows...

```shell
java -cp ./Reflector/ jorex.programs.reflector.Reflector --all --indent=4 --time --file='String.txt' java.lang.String
```

or with jar as...

```
java -jar ./Reflector/jorexreflector.jar -A4tF 'String.txt' java.lang.String
```

**Tip :** Do not forget to escape any command line characters e.g replace `$` with `\$` in inner class names.

**OUTPUT**

```java
/**
 * Assertion Status : false
 *
 *    Protection Domain Permissions
 *    -----------------------------
 * Permissions read only : true
 * ("java.security.AllPermission" "<all permissions>" "<all actions>")
 *
 *    Protection Domain Principals
 *    ----------------------------
 * <no principals>
 *
 *    Class Annotations
 *    -----------------
 * <no annotations>
 *
 *    Package Information
 *    -------------------
 * Name : java.lang
 * Is Sealed : true
 * Implementation Title : <none>
 * Implementation Version : <none>
 * Implementation Vendor : <none>
 * Specification Title : <none>
 * Specification Version : <none>
 * Specification Vendor : <none>
 *
 *    Package Annotations
 *    -------------------
 * <no annotations>
 */

/**
 *    Implemented Interfaces
 *    ----------------------
 * public abstract interface java.io.Serializable 
 * public abstract interface java.lang.Comparable<T> 
 * public abstract interface java.lang.CharSequence 
 * 
 *    Super Classes
 *    -------------
 * public class java.lang.Object 
 * 
 *    Super Fields
 *    ------------
 * <no super fields>
 * 
 *    Super Constructors
 *    ------------------
 * <no super constructors>
 * 
 *    Super Methods
 *    -------------
 * public final void wait(long arg0, int arg1) throws java.lang.InterruptedException {}
 *
 * public final void wait() throws java.lang.InterruptedException {}
 *
 * public final native void wait(long arg0) throws java.lang.InterruptedException {}
 *
 * @jdk.internal.HotSpotIntrinsicCandidate()
 * public final native java.lang.Class<?> getClass() {}
 *
 * @jdk.internal.HotSpotIntrinsicCandidate()
 * public final native void notify() {}
 *
 * @jdk.internal.HotSpotIntrinsicCandidate()
 * public final native void notifyAll() {}
 *
 */

package java.lang;

public final class String implements java.io.Serializable, java.lang.Comparable<java.lang.String>, java.lang.CharSequence {

    /** @jdk.internal.vm.annotation.Stable() */
    private final byte[] value;

    private final byte coder;

    private int hash;

    private static final long serialVersionUID = -6849794470754667710;

    static final boolean COMPACT_STRINGS = false;

    private static final java.io.ObjectStreamField[] serialPersistentFields = {};

    public static final java.util.Comparator<java.lang.String> CASE_INSENSITIVE_ORDER/* = java.lang.String$CaseInsensitiveComparator@16e852b */;

    static final byte LATIN1 = 0;

    static final byte UTF16 = 1;


    public String(byte[] arg0) {}

    public String(byte[] arg0, int arg1, int arg2) {}

    public String(byte[] arg0, java.nio.charset.Charset arg1) {}

    public String(byte[] arg0, java.lang.String arg1) throws java.io.UnsupportedEncodingException {}

    public String(byte[] arg0, int arg1, int arg2, java.nio.charset.Charset arg3) {}

    String(char[] arg0, int arg1, int arg2, java.lang.Void arg3) {}

    String(java.lang.AbstractStringBuilder arg0, java.lang.Void arg1) {}

    String(char[] arg0, boolean arg1) {}

    public String(java.lang.StringBuilder arg0) {}

    public String(java.lang.StringBuffer arg0) {}

    String(byte[] arg0, byte arg1) {}

    public String(char[] arg0, int arg1, int arg2) {}

    public String(char[] arg0) {}

    /** @jdk.internal.HotSpotIntrinsicCandidate() */
    public String(java.lang.String arg0) {}

    public String() {}

    public String(byte[] arg0, int arg1, int arg2, java.lang.String arg3) throws java.io.UnsupportedEncodingException {}

    /** @java.lang.Deprecated(forRemoval=false, since="1.1") */
    public String(byte[] arg0, int arg1) {}

    /** @java.lang.Deprecated(forRemoval=false, since="1.1") */
    public String(byte[] arg0, int arg1, int arg2, int arg3) {}

    public String(int[] arg0, int arg1, int arg2) {}


    byte coder() {}

    public boolean equals(java.lang.Object arg0) {}

    public int length() {}

    public java.lang.String toString() {}

    public int hashCode() {}

    public void getChars(int arg0, int arg1, char[] arg2, int arg3) {}

    /** Bridge method. */
    public volatile int compareTo(java.lang.Object arg0) {}

    public int compareTo(java.lang.String arg0) {}

    public int indexOf(int arg0) {}

    static int indexOf(byte[] arg0, byte arg1, int arg2, java.lang.String arg3, int arg4) {}

    public int indexOf(java.lang.String arg0) {}

    public int indexOf(java.lang.String arg0, int arg1) {}

    public int indexOf(int arg0, int arg1) {}

    static void checkIndex(int arg0, int arg1) {}

    public static java.lang.String valueOf(int arg0) {}

    public static java.lang.String valueOf(char arg0) {}

    public static java.lang.String valueOf(boolean arg0) {}

    public static java.lang.String valueOf(float arg0) {}

    public static java.lang.String valueOf(double arg0) {}

    public static java.lang.String valueOf(java.lang.Object arg0) {}

    public static java.lang.String valueOf(long arg0) {}

    public static java.lang.String valueOf(char[] arg0) {}

    public static java.lang.String valueOf(char[] arg0, int arg1, int arg2) {}

    private static java.lang.Void rangeCheck(char[] arg0, int arg1, int arg2) {}

    public java.util.stream.IntStream codePoints() {}

    public boolean isEmpty() {}

    public char charAt(int arg0) {}

    public int codePointAt(int arg0) {}

    public int codePointBefore(int arg0) {}

    public int codePointCount(int arg0, int arg1) {}

    public int offsetByCodePoints(int arg0, int arg1) {}

    public byte[] getBytes(java.nio.charset.Charset arg0) {}

    void getBytes(byte[] arg0, int arg1, byte arg2) {}

    public byte[] getBytes() {}

    public byte[] getBytes(java.lang.String arg0) throws java.io.UnsupportedEncodingException {}

    /** @java.lang.Deprecated(forRemoval=false, since="1.1") */
    public void getBytes(int arg0, int arg1, byte[] arg2, int arg3) {}

    public boolean contentEquals(java.lang.CharSequence arg0) {}

    public boolean contentEquals(java.lang.StringBuffer arg0) {}

    private boolean nonSyncContentEquals(java.lang.AbstractStringBuilder arg0) {}

    public boolean equalsIgnoreCase(java.lang.String arg0) {}

    public int compareToIgnoreCase(java.lang.String arg0) {}

    public boolean regionMatches(int arg0, java.lang.String arg1, int arg2, int arg3) {}

    public boolean regionMatches(boolean arg0, int arg1, java.lang.String arg2, int arg3, int arg4) {}

    public boolean startsWith(java.lang.String arg0) {}

    public boolean startsWith(java.lang.String arg0, int arg1) {}

    public boolean endsWith(java.lang.String arg0) {}

    public int lastIndexOf(java.lang.String arg0, int arg1) {}

    public int lastIndexOf(java.lang.String arg0) {}

    public int lastIndexOf(int arg0, int arg1) {}

    public int lastIndexOf(int arg0) {}

    static int lastIndexOf(byte[] arg0, byte arg1, int arg2, java.lang.String arg3, int arg4) {}

    public java.lang.String substring(int arg0, int arg1) {}

    public java.lang.String substring(int arg0) {}

    public java.lang.CharSequence subSequence(int arg0, int arg1) {}

    public java.lang.String concat(java.lang.String arg0) {}

    public java.lang.String replace(java.lang.CharSequence arg0, java.lang.CharSequence arg1) {}

    public java.lang.String replace(char arg0, char arg1) {}

    public boolean matches(java.lang.String arg0) {}

    public boolean contains(java.lang.CharSequence arg0) {}

    public java.lang.String replaceFirst(java.lang.String arg0, java.lang.String arg1) {}

    public java.lang.String replaceAll(java.lang.String arg0, java.lang.String arg1) {}

    public java.lang.String[] split(java.lang.String arg0) {}

    public java.lang.String[] split(java.lang.String arg0, int arg1) {}

    public static transient java.lang.String join(java.lang.CharSequence arg0, java.lang.CharSequence... arg1) {}

    public static java.lang.String join(java.lang.CharSequence arg0, java.lang.Iterable<? extends java.lang.CharSequence> arg1) {}

    public java.lang.String toLowerCase(java.util.Locale arg0) {}

    public java.lang.String toLowerCase() {}

    public java.lang.String toUpperCase(java.util.Locale arg0) {}

    public java.lang.String toUpperCase() {}

    public java.lang.String trim() {}

    public java.util.stream.IntStream chars() {}

    public char[] toCharArray() {}

    public static transient java.lang.String format(java.util.Locale arg0, java.lang.String arg1, java.lang.Object... arg2) {}

    public static transient java.lang.String format(java.lang.String arg0, java.lang.Object... arg1) {}

    public static java.lang.String copyValueOf(char[] arg0) {}

    public static java.lang.String copyValueOf(char[] arg0, int arg1, int arg2) {}

    public native java.lang.String intern() {}

    private boolean isLatin1() {}

    static void checkOffset(int arg0, int arg1) {}

    static void checkBoundsOffCount(int arg0, int arg1, int arg2) {}

    static void checkBoundsBeginEnd(int arg0, int arg1, int arg2) {}

    /** Synthetic method. */
    static byte[] access$100(java.lang.String arg0) {}

    /** Synthetic method. */
    static boolean access$200(java.lang.String arg0) {}
}

/**
 * Approximated reflection time : 1558ms
 */
```

#### Author : Jore

#### License

```
    Copyright © 2021 Jore

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
