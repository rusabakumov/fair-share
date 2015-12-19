package participant

import java.util.UUID

import event.{ Version, Event }
import participant.ParticipantPayload.{ Created, NameChanged }
import util._

case class Participant(
  id: ParticipantId,
  name: String,
  version: Version
)

object Participant {
  def empty = Participant(ParticipantId(UUID.randomUUID()), "No Name", Version.zero)

  def foldLeft(events: List[Event[Participant]]): Participant = {
    events.foldLeft(Participant.empty) {
      case (participant, Event(payload, metadata)) =>
        payload match {
          case Created(id, name) => participant.copy(id = id, name = name)
          case NameChanged(id, name) => participant.copy(name = name)
        }
    }
  }
}
