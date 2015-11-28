package occasion

import java.util.UUID

import account.Account
import participant.ParticipantId

case class OccasionId(id: UUID) extends AnyVal

case class Occasion(
  id: OccasionId,
  description: Option[String],
  accounts: Map[ParticipantId, Account]
)

object Occasion {
  def empty = Occasion(OccasionId(UUID.randomUUID()), None, Map())

  def foldLeft(events: List[OccasionEvent]): Occasion = {
    events.foldLeft(Occasion.empty) { (occasion, event) =>
      event match {
        case OccasionCreated(id) => occasion.copy(id = id)
        case OccasionDescriptionChanged(description) => occasion.copy(description = Some(description))
      }
    }
  }
}
