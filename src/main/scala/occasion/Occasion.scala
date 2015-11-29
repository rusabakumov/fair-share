package occasion

import java.util.UUID

import account.Account
import event.{ Event, Version }
import participant.ParticipantId

case class OccasionId(id: UUID) extends AnyVal

case class Occasion(
  id: OccasionId,
  version: Version,
  description: Option[String],
  accounts: Map[ParticipantId, Account]
)

object Occasion {
  def empty = Occasion(OccasionId(UUID.randomUUID()), Version.zero, None, Map())

  def foldLeft(events: List[Event[OccasionEvent]]): Occasion = {
    events.foldLeft(Occasion.empty) { (occasion, event) =>
      val Event(payload, metadata) = event

      payload match {
        case OccasionCreated(id) => occasion.copy(id = id, version = metadata.version)
        case OccasionDescriptionChanged(description) => occasion.copy(description = Some(description), version = metadata.version)
      }
    }
  }
}
