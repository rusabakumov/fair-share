package occasion

import java.util.UUID

import event.{ Event, Version }
import util._

import scalaz._
import scalaz.syntax.either._

trait OccasionService {
  def changeDescription(
    id: OccasionId,
    version: Version,
    newDescription: String
  ): Reader[OccasionRepo, V[Unit]] = Reader { repo =>
    repo.get
      .andThen(validatePresence)
      .andThen(validateVersion(version))
      .andThen(changeDescription(newDescription))
      .andThen(repo.store)
      .run(id)
  }

  def validatePresence = Kleisli[V, Option[Occasion], Occasion] {
    case None => "The specified occasion doesn't exist.".left[Occasion]
    case Some(occasion) => occasion.right
  }

  def validateVersion(version: Version) = Kleisli[V, Occasion, Occasion] { occasion =>
    if (version != occasion.version) "You're working with an outdated occasion.".left[Occasion]
    else occasion.right
  }

  def changeDescription(newDescription: String) = Kleisli[V, Occasion, (OccasionId, Event[OccasionEvent])] { occasion =>
    val event = OccasionOps.changeDescription(occasion, newDescription)
    (occasion.id, event).right
  }

  def create(): Reader[OccasionRepo, V[OccasionId]] = Reader { repo =>
    val id = OccasionId(UUID.randomUUID())
    val event = OccasionOps.create(id)

    repo.store.run(id, event).map(_ => id)
  }
}
