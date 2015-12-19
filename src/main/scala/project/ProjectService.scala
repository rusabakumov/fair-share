package project

import java.util.UUID

import event.{ Event, Version }
import util._
import util.syntax.repo._
import scalaz.syntax.either._

import scalaz.{ Kleisli, Reader }

object ProjectService {
  def create(): Reader[ProjectRepo, V[ProjectId]] = Reader { repo =>
    val id = ProjectId(UUID.randomUUID())

    repo.storeV(id, ProjectCommandHandler.create(id)).map(_ => id)
  }

  def changeName(
    id: ProjectId,
    version: Version,
    aName: String
  ): Reader[ProjectRepo, V[Unit]] = Reader { repo =>
    repo.getK
      .andThen(validateVersion(version))
      .andThen(changeName(aName))
      .andThen(repo.storeK)
      .run(id)
  }

  def validateVersion(version: Version) = Kleisli[V, Project, Project] { project =>
    if (version != project.version) "You're working with an outdated project.".left[Project]
    else project.right
  }

  def changeName(aName: String) = Kleisli[V, Project, (ProjectId, Event[Project])] { project =>
    (project.id, ProjectCommandHandler.changeName(project, aName)).right
  }
}
