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
  def blank: Participant = Participant(ParticipantId(UUID.randomUUID()), "No Name", Version.zero)

  implicit val agg: Aggregate[Participant] = Aggregate.build("Participant", blank, _.id)

  implicit val eventHandler: EventHandler[Participant] = new ParticipantEventHandler
}
