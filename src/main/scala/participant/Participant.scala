package participant

import java.util.UUID

import event.{ EventHandler, Version }
import util._

case class Participant(
  id: ParticipantId,
  name: String,
  version: Version
)

object Participant {
  implicit def empty: Empty[Participant] = Empty(Participant(ParticipantId(UUID.randomUUID()), "No Name", Version.zero))

  implicit val eventHandler: EventHandler[Participant] = new ParticipantEventHandler
}
