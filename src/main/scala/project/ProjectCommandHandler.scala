package project

import event.{ Metadata, Version, Event }
import util._

object ProjectCommandHandler {
  def create(id: ProjectId): Event[Project] = {
    val payload = ProjectPayload.Created(id)
    Event(payload, Metadata(Version.zero))
  }

  def changeName(project: Project, aName: String): Event[Project] = {
    val payload = ProjectPayload.NameChanged(project.id, aName)
    Event(payload, Metadata(project.version.next))
  }
}
