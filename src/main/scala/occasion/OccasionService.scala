package occasion

import java.util.UUID

import account.Money
import event.{Event, Version}
import participant.{ParticipantId, ParticipantRepo}
import util._

import scalaz.Kleisli._
import scalaz._
import scalaz.syntax.either._

trait OccasionService {
  def create(): Reader[OccasionRepo, V[OccasionId]] = Reader { repo =>
    val id = OccasionId(UUID.randomUUID())
    val event = OccasionOps.create(id)

    repo.store.run(id, event).map(_ => id)
  }

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

  def transfer(
    id: OccasionId,
    version: Version,
    fromId: ParticipantId,
    toId: ParticipantId,
    money: Money
  ): Reader[(OccasionRepo, ParticipantRepo), V[Unit]] = Reader {
    case (occasionRepo, participantRepo) =>
      val fromParticipant = participantRepo.get.andThen(validatePresence).run(fromId)
      val toParticipant = participantRepo.get.andThen(validatePresence).run(toId)
      val occasion = occasionRepo.get.andThen(validatePresence).andThen(validateVersion(version)).run(id)

      for {
        from <- fromParticipant
        to <- toParticipant
        target <- occasion
      } yield occasionRepo.store(target.id, OccasionOps.mkMoneyTransfer(target, from, to, money))
  }

  def validatePresence[T] = Kleisli[V, Option[T], T] {
    case None => "The specified occasion doesn't exist.".left[T]
    case Some(x) => x.right
  }

  def validateVersion(version: Version) = Kleisli[V, Occasion, Occasion] { occasion =>
    if (version != occasion.version) "You're working with an outdated occasion.".left[Occasion]
    else occasion.right
  }

  def changeDescription(newDescription: String) = Kleisli[V, Occasion, (OccasionId, Event[OccasionEvent])] { occasion =>
    val event = OccasionOps.changeDescription(occasion, newDescription)
    (occasion.id, event).right
  }

}
