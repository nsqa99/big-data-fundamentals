# SOME CONCEPT OF FUNCTIONAL PROGRAMING WITH SCALA

> Functional programming is a programming paradigm where programs are constructed by applying and composing functions. It is a declarative programming paradigm in which function definitions are trees of expressions that each return a value, rather than a sequence of imperative statements which change the state of the program.
> 
> In functional programming, functions are treated as first-class citizens, meaning that they can be bound to names (including local identifiers), passed as arguments, and returned from other functions, just as any other data type can. This allows programs to be written in a declarative and composable style, where small functions are combined in a modular manner.

## IMMUTABLE VALUES

In pure functional programming, only immutable values are used.

## PURE FUNCTIONS

- It always returns the same value for the same inputs.
- It has no side effects. A function with no side effects does nothing other than simply return a result. Any function that interacts with the state of the program can cause side effects.

## METHOD

```
def sum(x: Int, y: Int): Int = {
    x + y
}
```

## FUNCTION
```
val sum = (a: Int, b: Int) => a + b
```

In Scala, we treat a function as a value so function can be:
- assigned to a variable
- passed as an argument to other functions
- returned as a value from other functions

### ANONYMOUS FUNCTION
```
(a: Int, b: Int) => a + b
```
### NAMED FUNCTION
```
val sum = (a: Int, b: Int) => a + b
val anonfun2 = new Function2[Int, Int] {
     def apply(x: Int, y: Int): Int = x + y
   }
```

There is some “magic” behind-the-scenes that Scala does to allow the assignment of a function to a variable. All functions in Scala are special objects of type Function1 or Function2 or FunctionN, where N is the number of input arguments to the function.

The compiler will box the function code we provide into the appropriate object automatically. This object contains a method apply() that can be called to execute our function.

### BY-NAME PARAMETER
```
  def multi(a: Int, b: => Int) = {
    if (a % 2 == 0) 0
    else a * b
  }
  
  println(multi(0, 3 / 0)) // 0
  println(multi(1, 3 / 0)) // java.lang.ArithmeticException: / by zero
```

### PARTIALLY FUNCTION

### FUNCTION CURRING

### HIGH ORDER FUNCTION