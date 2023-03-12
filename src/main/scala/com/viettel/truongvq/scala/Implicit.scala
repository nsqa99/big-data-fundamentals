package com.viettel.truongvq.scala

object Implicit {
  def calculate(x: Int, y: Int)(implicit operator: (Int, Int) => Int) = operator(x, y)
  def main(args: Array[String]) = {
    implicit val plus: (Int, Int) => Int = (x: Int, y: Int) => x + y
    println(calculate(1, 2))

  }

}
