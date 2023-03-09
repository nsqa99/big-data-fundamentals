package com.viettel

object PatternMatching {
  sealed abstract class Operator

  case class Plus(x: Int, y: Int) extends Operator
  case class Minus(x: Int, y: Int) extends Operator
  case class Multiple(x: Int, y: Int) extends Operator
  case class Divide(x: Int, y: Int) extends Operator

  def calculate(operator: Operator) = {
    operator match {
      case plus: Plus => plus.x + plus.y
      case minus: Minus => minus.x - minus.y
      case multiple: Multiple => multiple.x * multiple.y
//      case divide: Divide => divide.x / divide.y
    }
  }

  def main(args: Array[String]) = {
    val operator = Plus(1, 2)
    println(calculate(operator))
  }
}
