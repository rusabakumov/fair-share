package catalog

import java.time.LocalDateTime
import java.util.UUID

import event.{ Metadata, Event }
import project.ProjectId

import scalaz.Reader
import scalaz.concurrent.Task

trait CatalogService {
  def createProject(name: String): Reader[CatalogRepo, Task[Option[ProjectId]]] = Reader { repo =>
    val id = ProjectId(UUID.randomUUID())
    val payload = ProjectCreated(id, name)
    val metadata = Metadata(LocalDateTime.now())
    val event = Event(payload, metadata)

    for {
      res <- repo.store(event)
    } yield if (res) Some(id) else None
  }
}
