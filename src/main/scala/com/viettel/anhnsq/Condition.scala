package com.viettel.anhnsq

import scala.io.StdIn._

object Condition {
  def main(args: Array[String]): Unit = {
    val num: Int = readInt()
    if (num < 0) {
      println("Số âm")
    } else if (num == 0) {
      println("Số không")
    } else {
      println("Số nguyên dương")
    }

    // Ternary operator
    val strVal: String = if (num <= 0) "Không phải số nguyên dương" else "Số nguyên dương"
    println(strVal)
  }
}
