package com.viettel.truongvq.scala

object ByNameParameter {
  def multi(a: Int, b: => Int) = {
    if (a % 2 == 0) 0
    else a * b
  }
  def main(args: Array[String]) = {
    println(multi(0, 3 / 0))
    println(multi(1, 3 / 0))
  }
}
