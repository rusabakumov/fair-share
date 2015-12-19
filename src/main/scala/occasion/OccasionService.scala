package occasion

import java.util.UUID

import account.Money
import event.{ Event, Version }
import participant.ParticipantRepo
import util._
import util.syntax.repo._

import scalaz._
import scalaz.syntax.either._

trait OccasionService {
  def create(): Reader[OccasionRepo, V[OccasionId]] = Reader { repo =>
    val id = OccasionId(UUID.randomUUID())
    val event = OccasionOps.create(id)

    repo.storeV(id, event).map(_ => id)
  }

  def changeDescription(
    id: OccasionId,
    version: Version,
    newDescription: String
  ): Reader[OccasionRepo, V[Unit]] = Reader { repo =>
    repo.getK
      .andThen(validateVersion(version))
      .andThen(changeDescription(newDescription))
      .andThen(repo.storeK)
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
      val fromParticipant = participantRepo.getV(fromId)
      val toParticipant = participantRepo.getV(toId)
      val occasion = occasionRepo.getK.andThen(validateVersion(version)).run(id)

      for {
        from <- fromParticipant
        to <- toParticipant
        target <- occasion
      } yield occasionRepo.storeV(target.id, OccasionOps.mkMoneyTransfer(target, from, to, money))
  }

  def validateVersion(version: Version) = Kleisli[V, Occasion, Occasion] { occasion =>
    if (version != occasion.version) "You're working with an outdated occasion.".left[Occasion]
    else occasion.right
  }

  def changeDescription(newDescription: String) = Kleisli[V, Occasion, (Id[Occasion], Event[Occasion])] { occasion =>
    val event = OccasionOps.changeDescription(occasion, newDescription)
    (occasion.id, event).right
  }
}
