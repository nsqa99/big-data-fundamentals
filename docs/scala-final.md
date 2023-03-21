# Scala

## Overview

* Scala name: comes from the word `Scalable`
* Modern language created by Martin Odersky (the father of `javac`), influenced by Java, Ruby,  Smalltalk, ML, Haskell, Erlang, .etc.
* High level language
* Statically typed
* Has a sophisticated type inference system
* Concise but readable syntax
* Scala is a pure object-oriented language: every variable is an object, every "operator" is a method
* Scala is also a functional programming language: functions are also variables, can be passed into other functions.
* Scala source code compiles to `.class` files run on JVM
* Works well with Java libraries

## Basic built-in types

1. Byte
2. Short
3. Int
4. Long
5. Boolean
6. Char
7. String: a sequence of Char
8. Float: 32-bit floating points
9. Double: 64-bit floating points

## Variables

### Variable types

* `val`: creates an immutable variable (like `final` in Java)
* `var`: creates a mutable variable

### Declaration

- Type implicit:
  ```
  val x = 2

  var y = "Hello world"
  ```

- Type explicit:
  ```
  val x: Int = 2

  var y: String = "Hello world"
  ```

## Control structures

### If/then/else construct

* Inline `if` statement
```
if (a == b) doSomething()
```

* Normal `if` statement
```
if (a == b) {
    doSomething()
}
```

* `if - else` statement
```
if (a > b) {
    doA()
} else {
    doB()
}
```

* Multi-condition `if` statement
```
if (a > b) {
    doA()
} else if (a < b) {
    doB()
} else {
    doC()
}
```

* Ternary operator
```
val res = if (a > b) a else b
```

### For loops
* `for`
```
for ( receiver <- generator ) {
    statement
}
```

* Iterate through collections: `foreach`
```
col.foreach(x => println(x))
```

* Multiple generators
Same meaning with nested loop in Java with more concise syntax
```
val range1 = 1 to 3
val range2 = 5 until 7

// using brackets
for (i <- range1; j <- range2) {
    println (s"$i, $j")
}

// using curly braces
for {
    i <- range1
    j <- range2
} {
    println (s"$i, $j")
}
```

Result:
```
1, 5
1, 6
2, 5
2, 6
3, 5
3, 6
```

* Condition in `for` loop:
```
val range1 = 1 to 9
val range2 = 1 to 5

for {
    i <- range1
    j <- range2
    if j != i
} {
    println (s"$i$j")
}
```

### For comprehension
Use for transforming collection into new one
```
val nums = Seq(1, 2, 3, 4, 5)

val doubledNums = for (n <- nums) yield {
    n * 2
}

// inline style
val inlineDoubledNums = for (n <- nums) yield n * 2
```

## Collections (scala 2.12.x)
### Immutable

* Immutable collections are useful in multi-threaded scenarios because immutable collections are thread-safety: cannot be modified once constructed.
* Operations on immutable collections usually slower than those on mutable ones because a new collection will be generated on each operation.

#### Array
```
// Array
val a = Array(1, 2, 3)

val tmp = a :+ 1 // => Array(1, 2, 3, 1)

Array(1, 2, 3) ++ Array(4, 5) // => Array(1, 2, 3, 4, 5)
```

#### List
`List` is a singly-linked list -> append operation is time-consuming: O(n)
```
// List
val l = List(1, 2, 3)
val l1 = 1 :: 2 :: 3 :: Nil

List(1, 2, 3) ++ List(4, 5) // => List(1, 2, 3, 4, 5)
```

#### Vector
Methods are executed in effectively constant time
```
// Vector
val v = Vector(1, 2, 3)
```

#### Set
```
// Set
val s = Set(1, 2, 3, 3) // => Set(1, 2, 3)
```

#### Merge two collections
```
val a: A
val b: B

// Immutable
a ++ b

// Mutable
a ++= b

// C type = B
val c = a ++: b

// C type = A
val c = a :++ b
```

#### Map
```
// Map
val m = Map(
  1 -> "Hi",
  2 -> "Hai",
  3 -> "Ba"
)

m + (4 -> "Bon") // => Map(1 -> "Hi", 2 -> "Hai", 3 -> "Ba", 4 -> "Bon")

m - 1 // => Map(2 -> "Hai", 3 -> "Ba")

m - (1, 2) // => Map(3 -> "Ba")

m -- List(1, 2) // => Map(3 -> "Ba")

for((idx, numName) <- m) println(s"key=$idx, value=$numName")

m.foreach {
  case(idx, numName) => println(s"key=$idx, value=$numName")
}
```

### Mutable
#### ArrayBuffer
```
// ArrayBuffer
val names = ArrayBuffer("mot", "hai")

names += "ba"

names -= "mot"

names ++= Array("bon", "nam")

names --= Array("hai", "bon")
```

#### Set
#### Map
```
// Map
val m = Map(
  1 -> "Hi",
  2 -> "Hai",
  3 -> "Ba"
)

m += (4 -> "Bon") // => Map(1 -> "Hi", 2 -> "Hai", 3 -> "Ba", 4 -> "Bon")

m -= 1 // => Map(2 -> "Hai", 3 -> "Ba", 4 -> "Bon")

m -= (1, 2) // => Map(3 -> "Ba", 4 -> "Bon")

m --= List(1, 2) // => Map(3 -> "Ba", 4 -> "Bon")
```

## Classes
### Declaration
```
class Foo
```

### Fields
```
class Foo(bar: Int) {
  def getBar(): Unit = println(bar)
}
```
* Instance variables have default **private** access modifier.

* Declare variables with `val` or `var` to make them `public`.
  ```
  class Foo(val bar: Int) {
    def getBar(): Unit = println(bar)
  }

  val foo = new Foo(1)

  foo.bar -> 1
  ```

* Fields visibility:
    * If a field is declared as a `var`, Scala generates both getter and setter methods for that field.
      ```
      class Animal(var name: String)
  
      var a = new Animal("cat")
  
      a.name = "Tom"
  
      println(a.name) // -> Tom
      ```

    * If the field is a `val`, Scala generates only a getter method for it.
      ```
      class Animal(val name: String)
  
      var a = new Animal("cat")
  
      println(a.name) // -> cat
  
      a.name = "Tom" // this won't compile
      ```

    * If a field doesn’t have a `var` or `val` modifier, the field will be compiled to `private[this] field_name`.
    So, the constructor parameter can only be accessed by the instance itself.
      ```
      class Animal(name: String) // similar (private[this] name: String)
  
      var a = new Animal("cat")
  
      println(a.name) // -> this won't compile
  
      a.name = "Tom" // this won't compile
      ```

    * `var` and `val` fields can be modified with the `private` keyword, which prevents getters and setters from being generated.
      ```
      class Animal(private var name: String)
  
      var a = new Animal("cat")
  
      println(a.name) // -> this won't compile
  
      a.name = "Tom" // this won't compile
      ```

### Constructors
* Public constructor
  ```
  class Animal(name: String, species: String)

  var cat = new Animal("Tom", "cat")
  ```

* Private constructor
  ```
  class Animal private (name: String, species: String)
  
  // Cannot initialize animal instance using `new` keyword. Use companion object to create instance:
  object Animal {
    def createCat(name: String): Animal = new Animal(name, "cat")
    def createLion(name: String): Animal = new Animal(name, "lion")
  }

  var tom = Animal.createCat("Tom")
  var siba = Animal.createLion("Siba")
  ```
  * Cannot extend a class with `private` constructor.

* Protected constructor
  
  Similar to private constructors, in that their classes cannot be instantiated using the new keyword, but they can be extended.
  ```
  class Animal protected (name: String, species: String)

  class Cat(name: String) extends Animal(name, "cat")
  class Lion(name: String) extends Animal(name, "lion")
  ```

* Default field values
    ```
    class Foo(name: String = "unknown")
  
    val foo = new Foo()
    val bar = new Foo("bar")
    ```
  * Benefits:
     * Provide preferred, default values for parameters
     * Let consumers of the class override values for their own needs

* Named parameters
    ```
    class Foo(name: String, address: String)
  
    val bar = new Foo(name = "bar", address = "HN")
    ```
    * Benefit: Make code more readable

### Method scopes
* Object-private scope: method is available only to the current instance of the current object. Other instances of the same class cannot access the method.
  **Syntax: `private[this]`**

  ```
  class Foo {
    private[this] def isFoo = true

    def test(other: Foo) {
      if (other.isFoo) { // => this line won't compile
        // ...
      }
    }
  }
  ```

* Private:
    * Similar to `private` scope in Java.
    * One instance can access to `private` method of other instance of the same class.

      ```
      class Foo {
        private def isFoo = true
  
        def test(other: Foo) {
          if (other.isFoo) { // => this works
            // ...
          }
        }
      }
      ```

* Protected:
    * Protected methods are available to all subclasses.

      ```
      class Animal {
          protected def breathe {}
      }
  
      class Dog extends Animal {
          breathe
      }
      ```
    * Different when comparing with `protected` in Java:
      * Java: protected methods can be accessed by other classes in the same package.
      * Scala: protected methods CANNOT be accessed by other classes in the same package.

* Package: method is available to all members of the current package.
  `private[package_name]`

  ```
  package com.example.oop

  class Animal {
    private[oop] def breathe {}
  }
  ```

  =>

  ```
  package com.example.oop

  object Main {
    def main(args: Array[String]): Unit = {
      val a = new Animal
      a.makeSound // this works
    }
  }
  ```

### Inheritance
```
class Animal(name: String) {
  def makeSound: Unit = println(s"$name is making sound...")
}

class Cat(name: String) extends Animal(name) {
  override def makeSound: Unit = println(s"$name is meowing...")
}
```

**Note:** A Scala class can extend only one class.

#### Inherit multiple classes
In Scala, this is not achievable with classes. Instead, multiple inheritance is supported via traits.
```
class Animal(name: String) {
  def makeSound: Unit = println(s"$name is making sound...")
}

class Carnivore {
    def favouriteFood: Unit = println("Still a mystery")
}

class Cat(name: String) extends Animal(name) with Carnivore { // this will not work
}
```

## Abstraction
### Traits
#### As Interfaces
```
trait Animal {
  def makeSound(): Unit
  def speedInKMH(): Double
}

class Cat extends Animal {
  override def makeSound(): Unit = {
    println("Meow~")
  }

  override def speedInKMH(): Double = 48.02
}
```

* Extending multiple traits
Using `with` keyword

```
trait Animal {
  def makeSound(): Unit

  def speedInKMH(): Double
}

trait Hobby {
  def favouriteFoods(): Array[String]
}

trait Specialty {
  def skills(): Array[String]
}

class Cat extends Animal with Hobby with Specialty {
  override def makeSound(): Unit = {
    println("Meow~")
  }

  override def speedInKMH(): Double = 48.02

  override def favouriteFoods() = Array("mice", "fish")

  override def skills() = Array("jump over fire ring")
}
```

#### As Abstract Classes
```
trait Animal {
  def makeSound(): Unit = {
    println("Try making a sound...")
  }

  def speedInKMH(): Double
}

class Leopard extends Animal {
  override def speedInKMH(): Double = 58
}
```

**Note:** `Trait`s don’t allow constructor parameters

#### Mixing in `interface`-like and `abstract class`-like traits
```
trait Animal {
  def makeSound(): Unit = {
    println("Try making a sound...")
  }

  def speedInKMH(): Double
}

trait Hobby {
  def favouriteFoods(): Array[String]
}

class Lion extends Animal with Hobby {
  override def speedInKMH(): Double = 80

  override def favouriteFoods() = Array("zebra", "deer")
}
```

#### Mixing traits in on the fly (at run time)

```
trait LocalConfiguration {
  def hostName: String = "http://localhost"
  def port: String = "8080" 
  def contextPath: String = "/example"
}

class App(name: String)
class Service(baseUrl: String)

val devApp = new App("devApp") with LocalConfiguration
val url = s"${devApp.hostName}${devApp.port}${devApp.contextPath}"
val someService = new Service(url)
```

### Abstract Classes
Due to the power of Traits, Abstract Classes are only used when:
  * Creating a base class requires constructor arguments
  ```
  abstract class Animal(name: String)
  
  class Dog(name: String) extends Animal(name) { ... }
  ```
  * Scala code is called from Java code

## Objects
* Objects in scala is singleton objects.

* Objects cannot be extended.

* Methods inside objects are like `static` methods in Java.

  ```
  object Utils {
    def sayHello(): Unit = {
      println("Hello world")
    }
  }
  
  Utils.sayHello()
  ```

### Companion objects
* An object with the same name as a trait or class is called companion object.
* Companion objects are often used to group together implicits, static methods, factory methods, and other functionality
that is related to a trait or class but does not belong to any specific instance.
* A class and its companion object can access to each other’s members, even `private`.

```
class Person(private val name: String) {
  def checkinBusStation(): Unit = {
    Person.busStationCheckCounter += 1
  }
}

object Person {
  private var busStationCheckCounter = 1
  def sayHelloTo(p: Person): Unit = println(s"Saying hello to $p.name")
  def printCounter: Int = busStationCheckCounter
}
```

## `sealed` keyword

* Used to control extension of classes and traits.
* Declaring a class or a trait as `sealed` restricts its subclasses location: all subclasses must be declared in the same source file with `sealed` class or trait.
* **Benefit:** Using `sealed` with pattern matching provides extra safety because the compiler will check that the `cases` of a `match` expression are exhaustive.
  ```
  sealed trait Animal
  case object Cat extends Animal
  case object Lion extends Animal
  case object Dog extends Animal

  def whichAnimal(animal: Animal): String = animal match {
    case Cat => "She's a cat"
    case Lion => "He's a lion"
  }
  ```
  
## `case` keyword

### Case Classes
Case classes are meant to represent classes which are `just data`: all data are immutable and public, without any mutable state 
or encapsulation; like `structs` in C/C++, `POJOs` in Java.

* Some features
  * Support pattern matching via `case` keyword
  * Constructor parameters are public `val` fields by default, so accessor methods are generated for each parameter
  * An `apply` method is created in the companion object of the class (automatically), so don't have to use `new` keyword to instantiate new instances
    ```
    case class Animal(name: String)

    var a = Animal("Tom") // no need to use 'new' keyword
    ```
  * A `copy` method is generated in the class
  * `equals` and `hashCode` methods are generated
  * A default `toString` method is generated

### Case Objects
* Similar to regular `objects`, with more features:
  * Serializable
  * Has default `hashCode` implementation
  * Has improved `toString` implementation

* Primarily used in two places:
  * When creating enumerations
    ```
    sealed trait Animal
    case object Cat extends Animal
    case object Lion extends Animal
    case object Dog extends Animal
    ```
  * When creating containers for Actor pattern messages

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

