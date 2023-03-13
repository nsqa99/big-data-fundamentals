package com.viettel.truongvq.scala

import scala.language.implicitConversions


object Implicit {
  implicit def squareToRectangle(square: Square): Rectangle = new Rectangle(square.x, square.x)
  class Rectangle(x: Int, y: Int) {
    def area() = x * y
  }
  case class Square(x: Int)
  def calculate(x: Int, y: Int)(implicit operator: (Int, Int) => Int) = operator(x, y)
  def main(args: Array[String]) = {
    implicit val plus: (Int, Int) => Int = (x: Int, y: Int) => x + y
    val square = Square(5)
    println(square.area())
  }
}
