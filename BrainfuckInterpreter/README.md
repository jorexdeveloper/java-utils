BRAINFUCK INTERPRETER
---------------------

Program to interpret/excecute brainfuck code.

### FEATURES

* Supports a custom null in character.
* Supports a custom null out character.
* Full brainfuck support.

### INSTALLATION

Download and extract the zip from below.

> [JavaUtils.zip](https://github.com/jorexdeveloper/JavaUtils/archive/root.zip)

### USAGE

You can use this program both in your code and with the command line (requires at least **jre** installed).

#### CODE

To use this class in your java project, you **MUST ADD** the path `.../BrainfuckInterpreter/` or `.../BrainfuckInterpreter/jorexbinterpreter.jar` to your class path replacing `...` with the path to the the directory `BrainfuckInterpreter` found in the zip file in order to add the class to your project.

##### SYNTAX

```java
// Import the classes for usage
import java.io.IOException;
import jorex.programs.bi.BrainfuckInterpreter;

public class YourClass {

  void yourMethod () {
    // your code to be interpreted
    String brainfuckCode = ">----------[<++++++++++>-]<--.";

    try {
      // Invoke the static method
      // interpret with a String
      // containing brainfuck code
      // to interpret
      BrainfuckInterpreter.interpret (brainfuckCode);
    }
    catch (IOException e) {
      System.err.println (e);
    }
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
public static final void interpret (String code) throws IOException
```
Takes a `String` containing brainfuck code, and interprets/excecutes it, with all input read from `System.in` and all output printed to `System.out`.

```java
public static final void interpret (String code, char nullIn, char nullOut) throws IOException
```
Similar to the above method, also allowing a character used to input a null value, and a character to output in place of a null value.

#### COMMAND LINE

You must at least have **jre** installed on your device.

##### COMMAND

```shell
java [ -cp <class path> ] jorex.programs.bi.BrainfuckInterpreter
```

or

```shell
java -jar jorexbinterpreter.jar
```

##### OPTIONS

Thre are different options that you can use together with the above command, here is a full list of the available command line options.

```
 Usage: [command] [options] [<file-names>...]                           

 Interpret brainfuck code.                                              

 The command represents the command used to excecute this program e.g   

$ java -jar ./BrainfuckInterpreter/jorexbinterpreter.jar                

or                                                                      

$ java -cp ./BrainfuckInterpreter jorex.programs.bi.BrainfuckInterpreter

Options include:                                                        

  -i, --null-in=<character>                                               
                    Use this character to input a null value.             
  -o, --null-out=<character>                                              
                    Output this character in place of a null value.       
  -h, --help                                                              
                    Print this message and exit.                        

  The file names supplied are excecuted in the order in which they are    
supplied, with all input read from System.in and all output printed to    
System.out.                                                               
  Combining of short options e.g [-io [args]] is allowed and all arguments
for the combined options (if any) must immediately follow the combined    
options in their respective order.                                        
  All supplied arguments excluding the above are considered to be file    
names.                                                                    
  Incase multiple options resolving to the same function are supplied, the
last option overwrites any preceding ones.                                
```

##### EXAMPLE

Change to the directory `JavaUtils-root` (in the zip file), then excecute included class supplying any of the desired options above (options must be the last).

```shell
java -cp ./BrainfuckInterpreter/ jorex.programs.bi.BrainfuckInterpreter
```

The `-cp ./BrainfuckInterpreter/` argument sets the **jre** class path.

or excecute the jar with...

```shell
java -jar ./BrainfuckInterpreter/jorexbinterpreter.jar
```

Alternatively, you can supply expressions and any of the above command line options as follows...

```shell
java -cp ./BrainfuckInterpreter/ jorex.programs.bi.BrainfuckInterpreter -io '.' '_' brainfuckcode.txt
```
or with jar as...

```
java -jar ./BrainfuckInterpreter/jorexbinterpreter.jar --null-in='.' --null-out='_' brainfuckcode.txt
```

**Tip :** Do not forget to escape any command line characters.

---

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
