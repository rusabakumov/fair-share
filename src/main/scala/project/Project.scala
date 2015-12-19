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
  implicit def empty: Empty[Project] = Empty(Project(ProjectId(UUID.randomUUID()), "", Version.zero))

  implicit val eventHandler: EventHandler[Project] = new ProjectEventHandler
}
