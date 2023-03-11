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
* Scala source code compiles to ".class" files run on JVM
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
for (i <- range; j <- rangeUntil) {
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

### Constructors
* Constructor fields visibility:
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

    * If a field doesn’t have a `var` or `val` modifier, Scala will not generate a getter or setter method for the field.
      ```
      class Animal(name: String)
  
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

* Public constructor:
  ```
  class Animal(name: String, species: String)

  var cat = new Animal("Tom", "cat")
  ```

* Private constructor:
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

* Protected constructor: similar to private constructors, in that their classes cannot be instantiated using the new keyword, but they can be extended.
  ```
  class Animal protected (name: String, species: String)

  class Cat(name: String) extends Animal(name, "cat")
  class Lion(name: String) extends Animal(name, "lion")
  ```

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
### Abstract classes
### Traits

## Objects

## Special keywords
### `case`
### `sealed`