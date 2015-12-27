package project

import aggregate.Version
import repo.AggregateRepo.syntax._
import util.Validation._
import util._

import scalaz.syntax.either._
import scalaz.{ Reader, \/ }

object ProjectService {
  import validation._

  def create(id: ProjectId, name: String): Reader[ProjectRepo, Valid[Unit]] = Reader { repo =>
    repo.getV(id).swap.fold(
      _ => s"Project with id = $id already exists.".left,
      _ => for {
        name <- validateName(name)
        toRepo <- repo.storeV(Commands.create(id, name))
      } yield toRepo
    )
  }

  def changeName(id: ProjectId, version: Version, name: String): Reader[ProjectRepo, Valid[Unit]] = Reader { repo =>
    for {
      project <- repo.getV(id).flatMap(validateVersion(version))
      name <- validateName(name)
      toRepo <- repo.storeV(Commands.changeName(project, name))
    } yield toRepo
  }

  object validation {
    def validateName(name: String): Valid[String] = if (name.length > 0) {
      name.right
    } else {
      "Name shouldn't be empty".left
    }

    def validateStatus(oldStatus: ProjectStatus, newStatus: ProjectStatus): String \/ ProjectStatus = {
      import ProjectStatus._
      val validTransitions = List(Open -> ProjectStatus.Finished, Open -> Removed, Removed -> Open, Finished -> Open)
      if (validTransitions.contains(oldStatus -> newStatus)) newStatus.right else s"Cannot transition to specified status".left
    }
  }
}
