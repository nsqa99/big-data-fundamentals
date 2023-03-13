# SOME CONCEPT OF FUNCTIONAL PROGRAMING WITH SCALA

> Functional programming is a programming paradigm where programs are constructed by applying and composing functions. It is a declarative programming paradigm in which function definitions are trees of expressions that each return a value, rather than a sequence of imperative statements which change the state of the program.
> 
> In functional programming, functions are treated as first-class citizens, meaning that they can be bound to names (including local identifiers), passed as arguments, and returned from other functions, just as any other data type can. This allows programs to be written in a declarative and composable style, where small functions are combined in a modular manner.

## IMMUTABLE VALUES

In pure functional programming, only immutable values are used.
- All variables are created as val fields
- Only immutable collections classes are used, such as List, Vector, and the immutable Map and Set classes

## PURE FUNCTIONS
A pure function has two key properties:
- It always returns the same value for the same inputs.
- It has no side effects. A function with no side effects does nothing other than simply return a result. Any function that interacts with the state of the program can cause side effects
  - Modifying a variable
  - Modifying a data structure in place
  - Setting a field on an object
  - Throwing an exception or halting with an error
  - Printing to the console or reading user input
  - Reading from or writing to a file
  - Drawing on the screen

## METHOD

```
def sum(x: Int, y: Int): Int = {
    x + y
}
```
### DEFAULT PARAMETER VALUES
```
def sum(x: Int = 1, y: Int): Int = {
    x + y
}
sum(2) // 3
```

### NAMED PARAMETER
```
def divide(numerator: Int, denominator: Int): Double = {
  numerator / denominator
}
divide(denominator = 1, numerator = 0) // 0
```

### BY-NAME PARAMETER
By-name parameters are evaluated every time they are used. They won’t be evaluated at all if they are unused
```
  def multi(a: Int, b: => Int) = {
    if (a % 2 == 0) 0
    else a * b
  }
  
  println(multi(0, 3 / 0)) // 0
  println(multi(1, 3 / 0)) // java.lang.ArithmeticException: / by zero
```

## FUNCTION
```
val sum = (a: Int, b: Int) => a + b
```

In Scala, we treat a function as a value so function can be:
- assigned to a variable
- passed as an argument to other functions
- returned as a value from other functions


```
def double(number: Int): Int = {
  number * 2
}

val array = Array(1,2,3,4,5)
println(array.map(f => f * 2).mkString("Array(", ", ", ")")) // Array(2, 4, 6, 8, 10)
println(array.map(double).mkString("Array(", ", ", ")")) // Array(2, 4, 6, 8, 10)
```


### ANONYMOUS FUNCTION
A function that has no name but has a body, input parameters, and return type (optional) is an anonymous function
```
(a: Int, b: Int) => a + b
```
### NAMED FUNCTION
```
val sum = (a: Int, b: Int) => a + b
```
```
val sumFunc2 = new Function2[Int, Int] {
    def apply(x: Int, y: Int): Int = x + y
}
```

There is some “magic” behind-the-scenes that Scala does to allow the assignment of a function to a variable. All functions in Scala are special objects of type Function1 or Function2 or FunctionN, where N is the number of input arguments to the function.

The compiler will box the function code we provide into the appropriate object automatically. This object contains a method apply() that can be called to execute our function.

if you write a symbol name followed by an argument list in parentheses (or just a pair of parentheses for an empty argument list), Scala converts that into a call to the apply method for the named object.



### HIGH ORDER FUNCTION
A higher-order function has at least one of the following properties:
- It takes one or more functions as parameters
- It returns a function

```
    val array = Array(1,2,3,4,5)
    println(array.map(f => f * 2).mkString("Array(", ", ", ")")) // Array(2, 4, 6, 8, 10)

    val calculate = (x: Int, y: Int, operator: (Int, Int) => Int) => operator(x, y)
    val minus = (x: Int, y: Int) => x - y
    val plus = (x: Int, y: Int) => x + y
    println(calculate(1, 2, minus)) // -1
    println(calculate(1, 2, plus)) // 3
```

### FUNCTION CURRYING
Currying is the process of converting a function with multiple arguments into a sequence of functions that take one argument. Each function returns another function that consumes the following argument.
```
    val getOperator: String => (Int, Int) => Int = (s: String) => (x: Int, y: Int) => {
        s match {
          case "+" => x + y
          case "-" => x - y
        }
    }
    val plus = getOperator("+")
    println(plus(1, 2)) // 3
```
### PATTERN MATCHING
Pattern matching is a mechanism for checking a value against a pattern.

#### Matching with case class
Case classes help us use the power of inheritance to perform pattern matching
```
  class Operator
  case class Plus(x: Int, y: Int) extends Operator
  case class Minus(x: Int, y: Int) extends Operator
  case class Multiple(x: Int, y: Int) extends Operator
  case class Divide(x: Int, y: Int) extends Operator

  def calculate(operator: Operator) = {
    operator match {
      case Plus(x, y) => x + y
      case Minus(x, y) => x - y
      case Multiple(x, y) => x * y
      case Divide(x, y) => x / y
    }
  }
```
#### Matching with type pattern
Each object has a static type that cannot be changed, so it easy to match object against type patterns.
```
    def typedPatternMatching(any: Any): String = {
    any match {
      case string: String => s"My value: $string is a string"
      case integer: Int => s"My value: $integer is aa integer"
      case _ => s"My value: $any is an unknown type"
    }
  }
```
#### Matching with regex
We can also use regular expressions when matching objects in our match expressions.
```
  def regexPatterns(toMatch: String) = {
    val numeric = "([0-9]+)".r
    val alphabetic = "([a-zA-Z]+)".r

    toMatch match {
      case numeric(value) => s"$value is numeric"
      case alphabetic(value) => s"$value is alphabetic"
      case _ => s"$toMatch is other type"
    }
  }
```

#### Pattern guards
Pattern guards are boolean expressions which are used to make cases more specific.
```
  def validate(operator: Operator) = {
    operator match {
      case divide: Divide if (divide.y == 0) => "Invalid operator"
      case _ => "Valid operator"
    }
  }
```

### IMPLICIT
#### Implicit parameter
Pass parameter to a method silently without going through the regular parameters list.
```
def calculate(x: Int, y: Int)(implicit operator: (Int, Int) => Int) = operator(x, y)
implicit val plus: (Int, Int) => Int = (x: Int, y: Int) => x + y
calculate(1, 2) // 3
```

#### Implicit conversion
Implicit conversions give the ability to convert one type into another
```
  class Rectangle(x: Int, y: Int) {
    def area() = x * y
  }
  
  case class Square(x: Int)
  
  implicit def squareToRectangle(square: Square): Rectangle = new Rectangle(square.x, square.x)

  val square = Square(5)
  println(square.area()) // 25
 ```

