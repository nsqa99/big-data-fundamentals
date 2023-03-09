package com.viettel

object HOF {

  def main(args: Array[String]) = {
    val array = Array(1,2,3,4,5)
    println(array.map(f => f * 2).mkString("Array(", ", ", ")"))

    val calculate1 = (x: Int, y: Int, operator: (Int, Int) => Int) => operator(x, y)
    val minus = (x: Int, y: Int) => x - y
    val plus = (x: Int, y: Int) => x + y
    println(calculate1(1, 2, minus))
    println(calculate1(1, 2, plus))

  }
}
