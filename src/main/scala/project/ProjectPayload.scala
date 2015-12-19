package project

import event.Payload
import util._

sealed trait ProjectPayload extends Payload[Project]

object ProjectPayload {
  case class Created(id: ProjectId) extends ProjectPayload

  case class NameChanged(id: ProjectId, name: String) extends ProjectPayload
}
