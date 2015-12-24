package project

import java.util.UUID

import event.{ EventHandler, Version }
import util._

case class Project(
  id: ProjectId,
  name: String,
  version: Version
)

object Project {
  def blank: Project = Project(ProjectId(UUID.randomUUID()), "", Version.zero)

  implicit val agg: Aggregate[Project] = Aggregate.build("Project", blank, _.id)

  implicit val eventHandler: EventHandler[Project] = new ProjectEventHandler
}
