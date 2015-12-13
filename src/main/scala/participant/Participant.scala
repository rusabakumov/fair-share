package participant

import java.util.UUID

import participant.ParticipantEvent.{Created, NameChanged}

case class ParticipantId(id: UUID) extends AnyVal

case class Participant(id: ParticipantId, name: String)

object Participant {
  def empty = Participant(ParticipantId(UUID.randomUUID()), "New participant")

  def foldLeft(events: List[ParticipantEvent]): Participant = {
    events.foldLeft(Participant.empty) { (participant, event) =>
      event match {
        case Created(id, name) => participant.copy(id = id, name = name)
        case NameChanged(name) => participant.copy(name = name)
      }
    }
  }
}