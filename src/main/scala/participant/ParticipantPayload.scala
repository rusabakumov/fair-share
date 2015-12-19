package participant

import event.Payload
import util._

sealed trait ParticipantPayload extends Payload[Participant]

object ParticipantPayload {
  case class Created(id: ParticipantId, name: String) extends ParticipantPayload

  case class NameChanged(id: ParticipantId, name: String) extends ParticipantPayload
}
