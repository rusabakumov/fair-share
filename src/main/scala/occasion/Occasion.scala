package occasion

import java.util.UUID

import account.Account
import event.{ EventHandler, Version }
import util._

case class Occasion(
  id: OccasionId,
  version: Version,
  description: Option[String],
  accounts: Map[ParticipantId, Account]
)

object Occasion {
  implicit def empty: Empty[Occasion] = Empty(Occasion(OccasionId(UUID.randomUUID()), Version.zero, None, Map()))

  implicit val eventHandler: EventHandler[Occasion] = new OccasionEventHandler

}
