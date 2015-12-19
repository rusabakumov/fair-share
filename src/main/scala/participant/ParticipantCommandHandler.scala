package participant

import event.{ Event, Metadata, Version }
import util._

object ParticipantCommandHandler {
  def create(id: ParticipantId, name: String): Event[Participant] = {
    val payload = ParticipantPayload.Created(id, name)
    Event(payload, Metadata(Version.zero))
  }

  def changeName(participant: Participant, aName: String): Event[Participant] = {
    val payload = ParticipantPayload.NameChanged(participant.id, aName)
    Event(payload, Metadata(participant.version.next))
  }
}
