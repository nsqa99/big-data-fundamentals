# Important notes on learning Scala

## Primitive types:

1. Byte

2. Short

3. Int

4. Long

5. Boolean

6. Char

7. Float: 32-bit floating points

8. Double: 64-bit floating points

## Variable declaration

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

## `val` and `var`

- `val` ~= `const` (JS)

- `var` ~= `var` (JS)

## Tuples

- Fixed-length collection of values, which may be of different types.

  ```
  val tuple = (1, true, "Hello")
  ```

- Tuple size varies from 1 to 22.

- Access tuple values: using `_${idx}`
  ```
  tuple._1 -> 1

  tuple._2 -> true

  tuple._3 -> "Hello"
  ```

- Extract tuple values:
  ```
  val (a, b, c) = tuple
  ```

## Arrays

- Initiating syntax: `Array[T](a, b, c,...)` 
  ```
  val a = Array[Int](1, 2, 3)

  val b = new Array[String](10) // create an empty array of String
  ```
- Access array items: `a(${idx})`
  ```
  a(0) -> 1

  a(1) -> 2
  ```

- Multi-dimensional array:
  ```
  val multiArr = Array(Array(1, 2), Array(3, 4))'

  multiArr(0)(0) -> 1

  multiArr(1)(1) -> 4
  ```

## Options

`Option[T]`

- Represent a value that may or may not exist.

- An `Option[T]` can be:
  
  + `Some(v: T)`: a value is present.

  + `None`: value not present.

- Options' helper method to deal with its value: `getOrElse(otherVal: T)`

## Ternary expression

```
c += (if (x == y) a else b)
```

## Range

- Range is exclusive by default:
  ```
  Range(1, 5) -> Range(1, 2, 3, 4)
  ```

- Range inclusive:
  ```
  Range.inclusive(1, 5) -> Range.inclusive(1, 2, 3, 4, 5)
  ```

## For loop

- Iterate through multi-dimensional array:
  ```
  val a = Array(Array(1, 2, 3), Array(3, 4))

  for (arr <- a; i <- arr) println(i) -> 1, 2, 3, 3, 4
  ```

- Condition in `for` loop:
  ```
  val a = Array(1, 2, 3, 4)

  for (i <- a if i % 2 == 0) println(i) -> 2, 4
  ```

## Collection comprehension

- Use `for` together with `yield` to transform a collection into new one:
  ```
  val a = Array(1, 2, 3)

  val processed = for (i <- a) yield i * 2 -> Array(2, 4, 6)
  ```

## Methods and Functions

**1. Methods**

- Define using `def` keyword
  ```
  def hello: Unit = {
    // TODOs
  }
  ```

  *syntactic sugar for Unit returning functions (depricated and should NOT use):*
  ```
  def hello {
    // TODOs
  }
  ```

- Can pass in optional parameters

  ```
  def hello(value: String = "world"): Unit = {
    println(s"Hello $value")
  }
  ```

- Can **NOT** be passed as parameter.

**2. Functions**

- Define using `=>` syntax.
  ```
  val hello: String => Unit = value => {
    println(s"Hello $value")
  }
  ```

- Functions are anonymous.

- Cannot have default parameters.

- Can be passed as parameter.

**3. Passing function as parameter**
```
class Box(var x: Int) {
  def updateValue(func: Int => Int) = x = func(x)
}
```

=>

```
val b = new Box(1)

b.x -> 1

b.updateValue(_ + 2)

b.x -> 3
```

**4. Multi Parameter Lists**
```
def reduce(list: List[Int], accum: Int)(func: (Int, Int) => Int): Int = {
  var sum = accum
  for (i <- Range(0, list.length)) {
    sum = func(sum, list(i))
  }
  sum
}
```

=>

```
val nums = List(1, 2, 3, 4)

reduce(nums, 0) {_ + _} -> 10

reduce(nums, 1) {_ * _} -> 24
```

## Collections
![scala-collection](./scala-collections.png)

### Common operations

**1. Builders**

* Create a builder as a temporary instance of collection which is modifiable, until the real instance is constructed.
* Most useful for Arrays or immutable collections which cannot add or remove elements once the collection has been constructed.

```
val b = Array.newBuilder[Int]
b += 1
b += 2

val arr = b.result()
```

**2. Factory methods**
```
Array.fill(3)("hi") // Array with "hi" repeated 3 times

Array.tabulate(3)(i => println(i)) // tabulate: Returns an array containing values of a given function over a range of integer values starting from 0.

dArray(1, 2, 3) ++ Array(4, 5, 6) // => Array(1, 2, 3, 4, 5, 6)
```

**3. Transforms**

Methods to transform a collection into a new one

```
Array(1, 2, 3, 4, 5).map(_ * 2) // => Array(2, 4, 6, 8, 10)

Array(1, 2, 3, 4, 5).filter(_ % 2 == 0) // => Array(2, 4)

Array(1, 2, 3, 4, 5).take(2) // Keep first two elements => Array(1, 2)

Array(1, 2, 3, 4, 5).drop(2) // Drop first two elements => Array(3, 4, 5)

Array(1, 2, 3, 4, 5).slice(1, 4) // Keep elements from index 1 to 4 (exclusive) => Array(2, 3, 4)

Array(1, 2, 2, 3, 3, 3, 4, 4).distinct // => Array(1, 2, 3, 4)
```

**4. Queries**

Methods to extract data from collections

```
Array(1, 2, 3).find(_ > 2) // => Optional[Int] = Some(3)

Array(1, 2, 3).find(_ % 5 == 0) // => None

Array(1, 2, 3).exists(_ > 2) // => true
```

**5. Aggregations**

Methods to combine data

```
Array(1, 2, 3, 4).mkString(",") // => "1,2,3,4"

Array(1, 2, 3, 4).mkString("[", ";", "]") // => "[1;2;3;4]"

Array(1, 2, 3).foldLeft(1)(_ * _) // => 6

Array(1, 2, 3, 4, 5).groupBy(_ % 2 == 0) // => Map(true -> Array(2, 4), false -> Array(1, 3, 5))
```

**6. Converters**

Convert among `Array` and other collections using `to`

```
Array(1, 2, 3).to[Vector]

Array(1, 2, 3, 3).to[Set]
```

**7. Views**

When chaining multiple transformations on a collection, multiple intermediate collections will be created and thrown away in the end.

Using `view` to defer the actual traversal and creation of a new collection until the end, so that only a single output collection is created.

```
Array(1, 2, 3, 4, 5).view.filter(_ % 2 == 0).map(_ * 3).slice(0, 2).to[Array]
```

### Immutable vs Mutable collections

* Immutable collections are useful in multi-threaded scenarios because immutable collections are thread-safety: cannot be modified once constructed.
* Operations on immutable collections usually slower than those on mutable ones because a new collection will be generated for each operation.

**Immutable collections**
```
// Array
val a = Array(1, 2, 3)

val tmp = a :+ 1 // => Array(1, 2, 3, 1)

Array(1, 2, 3) ++ Array(4, 5) // => Array(1, 2, 3, 4, 5)

// Vector
val v = Vector(1, 2, 3)

// List: singly-linked list -> append operation is time consuming
val l = List(1, 2, 3)
val l1 = 1 :: 2 :: 3 :: Nil

List(1, 2, 3) ++: List(4, 5) // => List(1, 2, 3, 4, 5)

// Set
val s = Set(1, 2, 3, 3) // => Set(1, 2, 3)

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

**Note**:

`++` and `++:` return different results when the operands are different types of collection. `++` returns the same collection type as the left side, and `++:` returns the same collection type as the right side

```
scala> List(5) ++ Vector(5)
res2: List[Int] = List(5, 5)

scala> List(5) ++: Vector(5)
res3: scala.collection.immutable.Vector[Int] = Vector(5, 5)
```

**Mutable collections**
```
// ArrayBuffer

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

// Set
```

**Small note**
```
The _ character in Scala is something of a wildcard character.
```

## Classes

```
class Foo(bar: Int) {
  def getBar(): Unit = println(bar)
}
```
- Instance variables have default **private** access modifier.

- Declare variables with `val` or `var` to make them `public`.
  ```
  class Foo(val bar: Int) {
    def getBar(): Unit = println(bar)
  }

  val foo = new Foo(1)

  foo.bar -> 1
  ```

**Constructors**
- Constructor fields visibility:

  + If a field is declared as a `var`, Scala generates both getter and setter methods for that field.
    ```
    class Animal(var name: String)

    var a = new Animal("cat")

    a.name = "Tom"

    println(a.name) // -> Tom
    ```

  + If the field is a `val`, Scala generates only a getter method for it.
    ```
    class Animal(val name: String)

    var a = new Animal("cat")

    println(a.name) // -> cat

    a.name = "Tom" // this won't compile
    ```

  + If a field doesn’t have a `var` or `val` modifier, Scala gets conservative, and doesn’t generate a getter or setter method for the field.
    ```
    class Animal(name: String)

    var a = new Animal("cat")

    println(a.name) // -> this won't compile

    a.name = "Tom" // this won't compile
    ```

  + `var` and `val` fields can be modified with the `private` keyword, which prevents getters and setters from being generated.
    ```
    class Animal(private var name: String)

    var a = new Animal("cat")

    println(a.name) // -> this won't compile

    a.name = "Tom" // this won't compile
    ```

- Public constructor:
  ```
  class Animal(name: String, species: String)
  ```
  => Can be initialized using `new` keyword:
  ```
  var cat = new Animal("Tom", "cat")
  ```

- Private constructor:
  ```
  class Animal private (name: String, species: String)
  ```
  + Cannot initialize animal instance using `new` keyword.
  Use companion object to create instance:
  ```
  object Animal {
    def createCat(name: String): Animal = new Animal(name, "cat")
    def createLion(name: String): Animal = new Animal(name, "lion")
  }

  var tom = Animal.createCat("Tom")
  var siba = Animal.createLion("Siba")
  ```
  + Cannot extend a class with `private` constructor.

- Protected constructor: are similar to private constructors, in that their classes cannot be instantiated using the new keyword, but they can be extended.
  ```
  class Animal protected (name: String, species: String)

  class Cat(name: String) extends Animal(name, "cat")
  class Lion(name: String) extends Animal(name, "lion")
  ```

**Method scope**
- Object-private scope: method is available only to the current instance of the current object. Other instances of the same class cannot access the method.
  
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

  The current Foo instance cannot call other Foo instance isFoo method because the method is restricted to other Foo instance only.

- Private:
  + Similar to `private` scope in Java.
  + One instance can access to `private` method of other instance of the same class.

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
- Protected:
  + Protected methods are available to all subclasses.
  
    ```
    class Animal {
        protected def breathe {}
    }

    class Dog extends Animal {
        breathe
    }
    ```
  + Different when comparing with `protected` in Java:
    - Java: protected methods can be accessed by other classes in the same package.
    - Scala: protected methods CANNOT be accessed by other classes in the same package.

- Package: method is available to all members of the current package.
  **Syntax: private[package_name]**

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

**Difference between `val` and `final val` instance variables**
- `final`: declares that a member is final and CAN NOT be overriden in subclasses.

  https://stackoverflow.com/questions/24911664/in-scala-difference-between-final-val-and-val

**Operator overloading**
Can implement operator overloading in Scala
```
class IntNumber(val value: Int) {
  def +(other: IntNumber): IntNumber = {
    new IntNumber(this.value + other.value)
  }
}
```

## Case classes
Case classes are meant to represent classes which are `just data`: all data are immutable and public, without any mutable state or encapsulation. Like `structs` in C/C++, `POJOs` in Java.

Similar to regular `class`, with some more features that support functional programming:
- Constructor parameters are public `val` fields by default, so accessor methods are generated for each parameter.
- An `apply` method is created in the companion object of the class (automatically) => don't have to use `new` keyword to instantiate new instances.
  ```
  case class Animal(name: String)

  var a = Animal("Tom") // no need to use 'new' keyword
  ```
- A `copy` method is generated in the class => use all the time in FP.
- `equals` and `hashCode` methods are generated.
- A default `toString` method is generated.
- It is possible to use `var` in case classes but this is **discouraged**.
- Case classes are good replacement for large tuples.

**Biggest advantage:** case classes support pattern matching.

Characteristics:
  * Immutable
  * Decomposable through pattern matching
  * Allows for comparison based on structure instead of reference
  * Easy to use and manipulate

## Objects
- Objects in scala is singleton objects.

- Objects cannot be extended.

- Methods inside objects are like `static` methods in Java.

  ```
  object App {
    def main(): Unit = {
      println("Hello world")
    }
  }
  ```

**Companion objects**
- An object with the same name as a trait or class is called companion object.

- Companion objects are often used to group together implicits, static methods, factory methods, and other functionality that is related to a trait or class but does not belong to any specific instance.

## Case objects
- Similar to regular `objects`, with more features:
  + Serializable
  + Has default `hashCode` implementation
  + Has improved `toString` implementation

- Primarily used in two places:
  + When creating enumerations
    ```
    sealed trait Animal
    case object Cat extends Animal
    case object Lion extends Animal
    case object Dog extends Animal
    ```
  + When creating containers for Actor pattern messages

## `sealed` keyword
- Used to control extension of classes and traits.
- Declaring a class or a trait as `sealed` restricts its subclasses location: all subclasses must be declared in the same source file with `sealed` class or trait.
- **Advantage:** Using `sealed` with pattern matching provides extra safety because  the compiler will check that the `cases` of a `match` expression are exhaustive.
  ```
  sealed trait Animal
  case object Cat extends Animal
  case object Lion extends Animal
  case object Dog extends Animal

  def showAnimalName(animal: Animal): String = animal match {
    case Cat => "This is a cat"
    case Lion => "This is a lion"
  }
  ```
**sealed trait**
`sealed trait`s are good for modelling hierarchies where the number of sub-classes are expected to change very little or not-at-all.

## Functional error handling

**Option/Some/None**
```
def toInt(str: String): Option[Int] = {
  try {
    Some(str.trim.toInt)
  } catch {
    case e: Exception => None
  }
}

toInt("123") match {
  case Some(i) => println(i)
  case None => println("error")
}
```

**Try/Success/Failure**
- Works just like `Option/Some/None` but with two nice features:
  + `Try` makes it very simple to catch exceptions
  + `Failure` contains exception

```
import scala.util.{Try, Success, Failure}

def toInt(str: String): Try[Int] = Try {
  s.trim.toInt
}

// ---------------shorten version-------------------
def toInt(str: String): Try[Int] = Try(s.trim.toInt)
// -------------------------------------------------

toInt(x) match {
  case Success(i) => println(i)
  case Failure(s) => println(s"Failed with reason: $s")
}
```


## By-name parameters
```
def func(arg: => String) = ...

```
By-name parameters are evaluated **each time** they are referenced in the method body.
Primary use cases:
  - Avoiding evaluation if the argument does not end up being used
    ```
    def log(level: Int, msg: => String): Unit = {
      if (level > 2) println(msg)
    }

    log(1, "Hello world " + 123) // (level = 1 < 2) => `"Hello world " + 123` will not be evaluated
    ```
  - Wrapping evaluation to run setup and teardown code before and after the argument evaluates
    ```
    def measureTime(f: => Unit): Unit = {
      val start = System.currentTimeMillis()
      f
      val end = System.currentTimeMillis()
      println(end - start)
    }
    ```
    The evaluation of `f: => Unit` is defered, allowing to measure the taken time.
    
    Some use cases:
      + Setting some thread-local context while the argument is being evaluated
      + Evaluating the argument inside a `try - catch` block so we can handle exceptions
      + Evaluating the argument in a Future so the logic runs asynchronously on another thread
  - Repeating evaluation of the argument more than once
    ```
    def retry[T](maxRetry: Int)(f: => T): T = {
      var retryCount = 0
      var result: Option[T] = None
      while (result.isEmpty) {
        try {
          result = Some(f) // f is evaluated each time being referenced
        } catch {
          case e: Throwable =>
            retryCount += 1
            if (retryCount > maxRetry) throw e
            else println(s"Retry $retryCount")
        }
      }

      result.get
    }
    ```

## Implicit parameters
Parameter which is automatically filled in when calling a function.
```
implicit val foo = 1

def bar(implicit foo: Int) = foo + 10

bar // => 11
```

**Example about `implicit parameters` use case:**

Code using `Future` needs an `ExecutionContext` value in order to work.
So that, we need to pass `ExecutionContext` to all functions that need it.

```
def getEmployee(ec: ExecutionContext, id: Int): Future[Employee] = ...

def getRole(ec: ExecutionContext, employee: Employee): Future[Role] = ...

val executionContext: ExecutionContext = ...

val bigEmployee: Future[EmployeeWithRole] = {
  getEmployee(executionContext, 100).flatMap(
    executionContext,
    e =>
      getRole(executionContext, e)
        .map(executionContext, r => EmployeeWithRole(e, r))
  )
}
```

The code can be cleaner with the use of `implicit paramter`:
```
def getEmployee(id: Int)(implicit ec: ExecutionContext): Future[Employee] = ...

def getRole(employee: Employee)(implicit ec: ExecutionContext, ): Future[Role] = ...

implicit val executionContext: ExecutionContext = ...

val bigEmployee: Future[EmployeeWithRole] = {
  getEmployee(100).flatMap(e =>
    getRole(e).map(r => 
      EmployeeWithRole(e, r)
    )
  )
}
```
Declaring `ExecutionContext` as an `implicit parameter` makes the code much more readable.

**Implicit conversions**

Scala can scan for implicit conversions from one type to another
```
case class IntNumber(value: Int) {
  def +(other: IntNumber): IntNumber = {
    IntNumber(this.value + other.value)
  }

  def printIntNumberValue(): Unit = println(value)
}

object IntNumber {
  implicit def convertFromString(s: String): IntNumber = IntNumber(s.toInt)
}

object App {
  def main(args: Arrayp[String]): Unit = {
    import IntNumber._

    "2".printIntNumberValue() // this works
  }
}
```

**Typeclass inference**

Implicit paramters are also useful to associate values to types. This is called by term `typeclass`.

* **Problem statement:** Need a parser to parse data from string to different data types, such as: Int, Boolean, Double, .etc.
* **Regular solution:**
  1. Create a generic method to parse the value:
    ```
    def parseFromString[T](s: String): T = ...

    val intVal = parseFromString[Int]("1")
    val booleanVal = parseFromString[Boolean]("true")
    ```
    
    This solution seems impossible because the program cannot know how to parse String into an arbitrary T.
  
  2. Using trait and define concrete parser class for each type
    ```
    trait Parser {
      def parse(s: String): T
    }

    object parseInt extends Parser[Int] {
      override def parse(s: String): Int = ...
    }

    object parseDouble extends Parser[Double] {
      override def parse(s: String): Double = ...
    }
    ```

    This works, but leads to another problem when we need to parse data in different way.
    For example: when need parsing data from console, we have two options:
  
    * Create another trait with new `parse` method definition
      ```
      trait ConsoleParser {
        def parse(): T
      }
      ```
      This way you have to duplicate the whole Type parsing methods.

    * Create a helper method which use `Parser` as an argument:
      ```
      def consoleParsingHelper[T](parser: Parser[T]) = parser.parse(scala.Console.in.readLine())
      ```
      This way you have to declare and pass `Parser` whenever calling method.

* **Implicit Solution**
  ```
  trait Parser {
    def parse(s: String): T
  }

  object Parser {
    implicit object ParseInt extends Parser[Int] {
      override def parse(s: String): Int = s.toInt
    }

    implicit object ParseDouble extends Parser[Double] {
      override def parse(s: String): Double = s.toDouble
    }

    implicit object ParseBoolean extends Parser[Boolean] {
      override def parse(s: String): Boolean = s.toBoolean
    }

    def parseFromString[T](s: String)(implicit parser: Parser[T]): T = {
      parser.parse(s)
    }
  }

  Parser.parseFromString[Int]("1") // => 1
  Parser.parseFromString[Double]("1") // => 1.0
  Parser.parseFromString[Boolean]("true") // => true
  ```
  And when needing change definition of parse method:

  ```
  object Parser {
    ...

    def parseFromConsole[T](implicit parser: Parser[T]): T = {
      parser.parse(scala.Console.in.readLine())
    }
  }
  ```

**Implicit class**

* Available since scala 2.10
* Helps add additional methods to existing classes
* Must be declared inside `trait`, `class`, `object`, or `package object`

```
object StringHelpers {
  implicit class RichString(value: String) {
    def withLength(): Unit = println(value.length)
  }
}

import StringHelpers._

"ten toi la".withLength
```

* Limitations:

Not all classes can be implicit classes because:
  * They cannot be defined as top-level objects
  * They cannot take multiple non-implicit arguments in their constructor
  * We cannot use implicit classes with case classes
  * There cannot be any member or object in scope with the same name as the implicit class


## Functional programming

**Definition**

Functional programming is a way of writing software applications using only `pure functions` and immutable values.

**Pure functions**
...

## Varargs

```
def takeVararg(args: String *) {
  for (str <- args) println(str)
}

val listStr = List("mot", "hai", "ba")

takeVararg(listStr: _*)
```
