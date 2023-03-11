package com.viettel.anhnsq

import scala.collection.mutable.ArrayBuffer

sealed trait Ranking
case object Excellent extends Ranking
case object Good extends Ranking
case object Normal extends Ranking
case object Bad extends Ranking
case object Fail extends Ranking

sealed trait HobbyGenre
case object Sport extends HobbyGenre
case object Book extends HobbyGenre
case object VideoGame extends HobbyGenre

case class Hobby(name: String, genre: HobbyGenre)

class Person(var name: String, var age: Int, var sex: String) {
  def printPersonalInfo(): Unit = {
    println(s"Info: name=$name, age=$age, sex=$sex")
  }
}

trait Subject {
  def requiredSubjects(): List[String]
  def gpaCalculationFormula(): String
}

class Student(name: String, age: Int, sex: String,
              val studentId: String, var classId: String, var gpa: Double) extends Person(name, age, sex) {
  private val hobbies: ArrayBuffer[Hobby] = ArrayBuffer[Hobby]()

  def attendClass(): Unit = {
    println(s"Attending to class $classId")
  }
}

class TechnicalStudent private (name: String, age: Int, sex: String, studentId: String, classId: String, gpa: Double)
    extends Student(name, age, sex, studentId, classId, gpa) with Subject {
  override def requiredSubjects(): List[String] = List("Algebra", "Statistic", "Calculus")

  override def gpaCalculationFormula(): String = "((Algebra + Calculus + Statistic) * 2 + S1 + .. + Sn) / (n + 6)"
}

object Student {
  def ranking(s: Student): Ranking = s.gpa match {
    case gpa if 8.5 <= gpa && gpa <= 10 => Excellent
    case gpa if 7.0 <= gpa && gpa <= 8.4 => Good
    case gpa if 5.5 <= gpa && gpa <= 6.9 => Normal
    case gpa if 4.0 <= gpa && gpa <= 5.4 => Bad
    case _ => Fail
  }

  def addHobby(s: Student, name: String, genre: HobbyGenre): Unit = {
    s.hobbies += Hobby(name, genre)
  }

  def getHobbies(s: Student): ArrayBuffer[Hobby] = {
    s.hobbies
  }
}

object TechnicalStudent {
  def of(name: String, age: Int, sex: String, studentId: String, classId: String, gpa: Double): TechnicalStudent = {
    new TechnicalStudent(name, age, sex, studentId, classId, gpa)
  }
}

object Classes {
  def main(args: Array[String]): Unit = {
    val techStu = TechnicalStudent.of("AnhNSQ", 18, "male", "b17dccn031", "cn7", 8)
    Student.addHobby(techStu, "Football", Sport)
    Student.addHobby(techStu, "Swimming", Sport)
    Student.addHobby(techStu, "Little Prince", Book)
//    techStu.name = "changed"
    techStu.printPersonalInfo()

    println("Hobbies: ")
    Student.getHobbies(techStu).foreach(println)
    println(Student.ranking(techStu) match {
      case Excellent => "Astounding"
      case Good => "Very good, but I believe you can do better"
      case Normal => "Try harder next time"
      case Bad => "Need to spend more attention"
      case _ => "I am extremely disappointed"
    })
    println(techStu.requiredSubjects())
    println(techStu.gpaCalculationFormula())
  }
}
