package com.viettel.anhnsq

object ForLoop {
  def forLoop(): Unit = {
    val range = 1 to 3
    for (num <- range) {
      println(s"Số: $num")
    }
  }

  def main(args: Array[String]): Unit = {
//    forLoop()
//    nestedFor()
//    forWithCondition()
//    foreachDemo()
    forComprehension()
  }

  def nestedFor(): Unit = {
    val range1 = 1 to 3
    val range2 = 5 until 7

    // using brackets
    for (i <- range1; j <- range2) {
      println (s"$i, $j")
    }

    // using curly braces
    for {
      i <- range1
      j <- range2
    } {
      println (s"$i, $j")
    }
  }

  def forWithCondition(): Unit = {
    val range1 = 1 to 9
    val range2 = 0 to 9

    for {
      i <- range1
      j <- range2
      if j != i
    } {
      println (s"$i$j")
    }
  }

  def foreachDemo(): Unit = {
    val range = 1 to 9

    range.foreach(x => println(s"Số: $x"))
  }

  def forComprehension(): Unit = {
    val nums = Seq(1, 2, 3, 4, 5)

    val doubledNums = for (n <- nums) yield {
      n * 2
    }

    doubledNums.foreach(println)
  }
}
