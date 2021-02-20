HICULATOR
---------

Program to evaluate/solve mathematical expressions using **BODMAS**.

### FEATURES

* Works with large decimals.
* Supports mathematical constants.
* Supports output of answer in words.
* Stores answer to the previous successful evaluation.

### OPERATIONS

OPERATION      | DESCRIPTION
---------------|----------------------------------------------------------
` ÷ ` or ` / ` | For division
` × ` or ` * ` | For multiplication
` + `          | For addition
` - `          | For subtraction
` ² `          | For raising a number to power
` ³ `          | For raising a number to power
` √ `          | For finding square root of a number
` ! `          | For finding mathematical factorial of a number

**NOTE :** The maximum value for finding a factorial is `50`, equivalent to

```java
50! = 30414093201713378043612608166064768844377641568960512000000000000
```
and any value higher than `50` will resolve to a **Math error!**.

### SYMBOLS

SYMBOL             | DESCRIPTION
-------------------|----------------------------------------------------------------
` π ` or ` Π `     | Replaced with the mathematical constant **PI**
` ANS ` or ` ans ` | Replaced with the answer to the previous successful evaluation, or `0` if none

### INSTALLATION

Download and extract the zip from below.

> [JavaUtils.zip](https://github.com/jorexdeveloper/JavaUtils/archive/root.zip)

### USAGE

You can use this program both in your code and with the command line (requires at least **jre** installed).

#### CODE

To use this class in your java project, you **MUST** add the path `.../ScientificCalculator/` to your class path replacing `...` with the path to the the directory `ScientificCalculator` found in the zip file in order to add the class to your project.

##### SYNTAX

```java
// Import the class for usage
import jorex.programs.calc.Hiculator;

public class YourClass {

  void yourMethod () {
    // Your expression to be solved
    String expression = "2*5²(67-56)+5!";

    // Number of digits to round the
    // answer to.
    // Use Hiculator.ROUND_MODE_OFF
    // To prevent rounding off
    int roundTo = 6;

    // Invoke the static method
    // evaluate with the expression and
    // int value to round the answer to.
    // Hiculator.ROUND_MODE_OFF to prevent
    // rounding of the answer.
    String answer = Hiculator.evaluate (expression, roundTo);

    // Use your answer in your code
    System.out.println (answer);
  }
}
```

##### FIELDS

```java
public static final int ROUND_MODE_OFF
```
Used in place of argument `roundTo` to disable rounding off of the answer.

```java
public static final String DELIMETER_DEFAULT
```
This is the default delimeter/separator of words (a white space) used if none specified with method `evaluateToWords`.

```java
public static final String USAGE_COMMAND_LINE
```
This contains the full command line usage of this program.

```java
public static final String OPERATIONS
```
This contains a list of all the currently supported operations, and their representations.

```java
public static final String SYMBOLS
```
This contains a list of all the currently supported symbols, and their representations.

##### METHODS

```java
public static final String evaluate (String expression, int roundTo)
```
Takes a **String** containing a mathematical expression and an `int` used to round off the answer, then returns a **String** containing the answer or a message signaling failure. The answer is rounded off to the nearest place value of the digit at position `roundTo` from the left (using the [java.math.MathContext](https://docs.oracle.com/javase/7/docs/api/java/math/MathContext.html)).

**Tip :** Use the constant `Hiculator.ROUND_MODE_OFF` in place of `roundTo` to disable rounding off of the answer.

```java
public static final String evaluateToWords (String expression, int roundTo)
```
Similar to the above method but returns the answer in words using `Hiculator.DELIMETER_DEFAULT` as the default delimeter/separator, and format `NumberToWord.FORMAT_DEFAULT`(see below) as the output format of the words in the answer.

```java
public static final String evaluateToWords (String expression, int roundTo, int format)
```
Similar to the above methods, allowing a constant (one of the below) to specify the output format of the words in the answer.

Use any of the constants found in the class `jorex.programs.ntw.NumberToWord` below to specify the format above.

```java
public static final int FORMAT_DEFAULT
```
Specifies all output to be converted to lower case. If no format or a wrong format is specified, this is the default output format of the words.

```java
public static final int FORMAT_ALL_UPPERCASE
```
Specifies all output to be converted to upper case.

```java
public static final int FORMAT_FIRST_LETTER_UPPER_CASE
```
Specifies conversion of only the first letter of the first word to be converted to upper case.

```java
public static final int FORMAT_FIRST_LETTER_UPPER_CASE_ALL
```
Specifies conversion of the first letter of each word to be converted to upper case.


```java
public static final String evaluateToWords (String expression, int roundTo, String delimeter)
```
Similar to the above methods, allowing a `String` used as the delimeter/separator for the words.

```java
public static final String evaluateToWords (String expression, int roundTo, int format, int range)
```
**EXPERIMENTAL**

Similar to the above methods, allowing a special format constant in class `jorex.programs.ntw.NumberToWord` (below) and an `int` used as a range for formatting the answer. This may only be used with the format below.

```java
public static final int FORMAT_FIRST_LETTER_UPPER_CASE_RANGE
```
Specifies conversion of the first letter every after `range` digits in the answer from the right to upper case. It requires a range of digits to be specified with the above method.


```java
public static final String evaluateToWords (String expression, int roundTo, String delimeter, int format)
```
Similar to the above methods but allows a `String` delimeter/separator and a format constant.

```java
public static final String evaluateToWords (String expression, int roundTo, String delimeter, int format, int range)
```
**EXPERIMENTAL**

Similar to the above methods, allowing a delimeter/separator, a format constant and an `int` used as a range for formatting the answer.

#### COMMAND LINE

You must at least have **jre** installed on your device.

##### COMMAND

```shell
java [ -cp <class path> ] jorex.programs.calc.Hiculator
```

or

```shell
java -jar jorexcalc,jar
```

##### OPTIONS

Thre are different options that you can use together with the above command, here is a full list of the available command line options.

```
 Usage: [command] [options] [expressions...]                         

 Evaluate/solve (a) mathematical expression(s) using BODMAS.         

 The command represents the command used to excecute this program e.g

$ java -jar ./ScientificCalculator/jorexcalc.jar                     

or                                                                   

$ java -cp ./ScientificCalculator jorex.programs.calc.Hiculator      

Options include:                                                     

  -w, --words                                                          
                    Output the answer in words.                        
  -r, --round-off=<value>                                              
                    Round off the answer to the nearest place value of 
                    the digit at position 'value' in the answer from   
                    the left.                                          
  -d, --delimeter=<string>                                             
                    Used with -w to specify 'string' as  a delimeter/  
                    separator for the words.                           
  -1, --format-all-uppercase                                           
                    Used with -w to convert all the words to uppercase.
  -2, --format-first-letter-uppercase                                  
                    Used with -w to convert only the first letter of   
                    the first word to uppercase.                       
  -3, ---format-first-letter-uppercase-all                             
                    Used with -w to convert the first letter of each   
                    word to uppercase.                                 
  -f, --format-first-letter-uppercase-range=<range>                    
                    Used with -w to convert the first letter of each   
                    word after 'range' digits in the answer from the   
                    right to uppercase (Experimental)                  
  -h, --help                                                           
                    Print this message end exit.                       
  -l, --list                                                           
                    Print a list of supported operations and symbols   
                    and exit.                                        

 The answers to the supplied expressions are all output to System.out  
in the order in which they are supplied, using the specified format if 
if any. If none are supplied as arguments they are read from           
System.in.                                                           

 Note: Unfortunately, concatenation of short commands e.g -wrd1f is    
       currently not supported and will be interpreted as an expression
       resolving to a Syntax error!                                    
 All mandatory arguments for short options are mandatory for long      
options too.                                                           
 All supplied arguments excluding the above are considered to be       
expressions.                                                           
 By default, if -w specified without any formats, the output is all    
converted to lower case.                                               
 Incase multiple options resolving to the same function are supplied,  
the last option ovewrites any preceding ones.                          
```

##### EXAMPLE

Change to the directory `JavaUtils` (in the zip file), then excecute included class supplying any of the desired options above (options must be the last).

```shell
java -cp ./ScientificCalculator/ jorex.programs.calc.Hiculator
```

The `-cp ./ScientificCalculator/` argument sets the **jre** class path.

or excecute the jar with...

```shell
java -jar ./ScientificCalculator/jorexcalc.jar
```

Alternatively, you can supply expressions and any of the above command line options as follows...

```shell
java -cp ./ScientificCalculator/ jorex.programs.calc.Hiculator --words --round-off=6 --delimeter='--' --format-first-letter-uppercase-all 6÷2\(1+1\) 3³+2*3+6²\(49/67\)³-10!
```

or with jar as...

```
java -jar ./ScientificCalculator/jorexcalc.jar --words --round-off=6 --delimeter='--' --format-first-letter-uppercase-all 6÷2\(1+1\) 3³+2*3+6²\(49/67\)³-10!
```

**Tip :** Do not forget to escape any command line characters e.g replace `(` with `\(`. 

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

