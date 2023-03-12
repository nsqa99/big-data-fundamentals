package com.viettel.truongvq.scala

object Currying {

  def main(args: Array[String]) = {
    val getOperator: String => (Int, Int) => Int = (s: String) => (x: Int, y: Int) => {
      s match {
        case "+" => x + y
        case "-" => x - y
      }
    }
    val plus = getOperator("+")
    println(plus(1, 2))
  }

}
