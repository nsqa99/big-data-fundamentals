package com.viettel.anhnsq

import scala.collection.mutable.ArrayBuffer

object Collection {
  object ArrayDemo {
    def execute(): Unit = {
      val arr = Array(1, 2, 3)

      println(s"Append: ${stringify(arr :+ 4)}")
      println(s"Add all: ${stringify(arr ++ Array(5, 6))}")
    }

    def stringify[T](array: Array[T]): String = {
      array.mkString("[", ",", "]")
    }
  }

  object ListDemo {
    def execute(): Unit = {
      val list = List(2, 3)

      println(s"Prepend: ${1 +: list}")
      println(s"Append: ${list :+ 1}")
      println(s"Add all: ${list ++ List(4, 5, 6)}")
    }
  }

  object VectorDemo {
    def execute(): Unit = {
      val vector = Vector(2, 3)

      println(s"Prepend: ${0 +: vector}")
      println(s"Append: ${vector :+ 1}")
      println(s"Add all: ${vector ++ Vector(4, 5, 6)}")
    }
  }

  object SetDemo {
    def execute(): Unit = {
      val set = Set(1, 1, 2, 3, 3, 3)

      println(s"Append: ${set + 4}")
      println(s"Add all: ${set ++ Set(5, 5, 6, 7, 7, 7, 7)}")
    }
  }

  object MapDemo {
    def execute(): Unit = {
      val map = Map(1 -> "mot", 2 -> "hai")

      println(s"Append: ${map + (4 -> "bon")}")
      println(s"Add all: ${map ++ Map(3 -> "ba", 5 -> "nam")}")
      println(s"Remove key: ${map - 2}")
      println(s"Remove all keys: ${(map ++ Map(3 -> "ba")) -- List(1, 2)}")
      println(s"Get value: ${map.get(1)}")
    }
  }

  object ArrayBufferDemo {
    def execute(): Unit = {
      val arrayBuffer = ArrayBuffer[Int](4, 5)

      arrayBuffer += 1
      println(s"Append: $arrayBuffer")

      arrayBuffer ++= List(2, 3)
      println(s"Add all: $arrayBuffer")

      arrayBuffer -= 2
      println(s"Remove element: $arrayBuffer")

      arrayBuffer --= List(1, 3)
      println(s"Remove all: $arrayBuffer")
    }
  }

  def main(args: Array[String]): Unit = {
//    ArrayDemo.execute()
//    ListDemo.execute()
//    VectorDemo.execute()
//    SetDemo.execute()
//    MapDemo.execute()
    ArrayBufferDemo.execute()
  }
}
