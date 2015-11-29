package occasion

import java.util.UUID

import event.{ Event, Metadata, Version }

import scalaz._

trait OccasionService {
  type V[T] = ValidationNel[String, T]

  def changeDescription(
    id: OccasionId,
    version: Version,
    newDescription: String
  ): Reader[OccasionRepo, V[Unit]] = Reader { repo =>
    val repoResult = repo.get(id)

    repoResult.fold(
      e => Failure(NonEmptyList(e.toString)),
      {
        case Some(occasion) =>
          if (version != occasion.version) Failure(NonEmptyList("You're working with an outdated occasion."))
          else {
            val payload = OccasionDescriptionChanged(newDescription)
            val metadata = Metadata(occasion.version.next)
            val event = Event(payload, metadata)

            repo.store(id, event).fold(
              e => Failure(NonEmptyList(e.toString)),
              _ => Success(())
            )
          }
        case None => Failure(NonEmptyList("Occasion with such ID isn't found."))
      }
    )
  }

  def create(): Reader[OccasionRepo, V[OccasionId]] = Reader { repo =>
    val id = OccasionId(UUID.randomUUID())
    val metadata = Metadata(Version.zero)
    val event = Event(OccasionCreated(id), metadata)

    repo.store(id, event).fold(
      e => Failure(NonEmptyList(e.toString)),
      _ => Success(id)
    )
  }
}
