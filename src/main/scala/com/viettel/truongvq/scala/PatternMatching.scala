package com.viettel.truongvq.scala

object PatternMatching {
  class Operator
  case class Plus(x: Int, y: Int) extends Operator
  case class Minus(x: Int, y: Int) extends Operator
  case class Multiple(x: Int, y: Int) extends Operator
  case class Divide(x: Int, y: Int) extends Operator

  def calculate(operator: Operator) = {
    operator match {
      case plus: Plus => plus.x + plus.y
      case minus: Minus => minus.x - minus.y
      case multiple: Multiple => multiple.x * multiple.y
      case divide: Divide => divide.x / divide.y
    }
  }

  def regexPatterns(toMatch: String) = {
    val numeric = "([0-9]+)".r
    val alphabetic = "([a-zA-Z]+)".r

    toMatch match {
      case numeric(value) => s"$value is numeric"
      case alphabetic(value) => s"$value is alphabetic"
      case _ => s"$toMatch is other type"
    }
  }

  def validate(operator: Operator) = {
    operator match {
      case divide: Divide if (divide.y == 0) => "Invalid operator"
      case _ => "Valid operator"
    }
  }

  def typedPatternMatching(any: Any): String = {
    any match {
      case string: String => s"My value: $string is a string"
      case integer: Int => s"My value: $integer is aa integer"
      case _ => s"My value: $any is an unknown type"
    }
  }

  def main(args: Array[String]) = {
    val operator = Plus(1, 2)
    val invalidOperator = new Divide(1, 0)
//    println(calculate(operator))
//    print(validate(invalidOperator))
//    println(regexPatterns("0212"))
//    println(regexPatterns("sda./"))
    println(typedPatternMatching("sas"))
  }
}
