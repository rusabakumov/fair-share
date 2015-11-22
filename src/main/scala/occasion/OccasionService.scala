package occasion

import account.Money
import participant.{ParticipantRepo, ParticipantId}

import scalaz.{Reader, ValidationNel}

case class ConsumeInfo(paid: Money, consumed: Money)

trait OccasionService {
  type V[T] = ValidationNel[String, T]

  def run(events: List[OccasionEvent]): Occasion => Occasion = ???

  def split(description: Map[ParticipantId, ConsumeInfo]): (OccasionRepo, ParticipantRepo) => V[OccasionLedgerChanged] = ???
}
