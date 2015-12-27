package project

import aggregate._
import monocle.macros.GenLens
import util._

case class Project(
  id: ProjectId,
  name: String,
  status: ProjectStatus,
  version: Version,
  uncommittedEvents: Vector[EventXor[Project, ProjectCreated, ProjectChanged]] = Vector.empty
)

sealed trait ProjectStatus extends Product with Serializable

object ProjectStatus {

  case object Open extends ProjectStatus

  case object Removed extends ProjectStatus

  case object Finished extends ProjectStatus

}

object Project extends Handlers {
  implicit val agg: Aggregate[Project] = Aggregate(GenLens[Project](_.id), GenLens[Project](_.version), "project")

  implicit val es: EventSourced[Project, ProjectCreated, ProjectChanged] = EventSourced(
    GenLens[Project](_.uncommittedEvents)
  )
}
