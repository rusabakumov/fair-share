package util

import occasion.Occasion
import participant.Participant
import project.Project

trait NamedAggregate[T] {
  def name: String
}

object NamedAggregate {
  implicit val occasionDescription: NamedAggregate[Occasion] = NamedAggregate("Occasion")
  implicit val participantDescription: NamedAggregate[Participant] = NamedAggregate("Participant")
  implicit val projectDescription: NamedAggregate[Project] = NamedAggregate("Project")

  def apply[T](aName: String): NamedAggregate[T] = new NamedAggregate[T] {
    def name: String = aName
  }
}
