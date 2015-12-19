package participant

import java.util.UUID

import event.Event
import participant.ParticipantPayload.{ Created, NameChanged }
import util._

case class Participant(id: ParticipantId, name: String)

object Participant {
  def empty = Participant(ParticipantId(UUID.randomUUID()), "New participant")

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
