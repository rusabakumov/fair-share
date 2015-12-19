package project

import event.{ Event, EventHandler }

class ProjectEventHandler extends EventHandler[Project] {
  def handle(agg: Project, event: Event[Project]): Project = {
    val Event(payload, metadata) = event
    payload match {
      case ProjectPayload.Created(id) => agg.copy(id = id, version = metadata.version)
      case ProjectPayload.NameChanged(id, name) => agg.copy(name = name, version = metadata.version)
    }
  }
}
