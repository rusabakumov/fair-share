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
  def blank: Occasion = Occasion(OccasionId(UUID.randomUUID()), Version.zero, None, Map())

  implicit val agg: Aggregate[Occasion] = Aggregate.build("Occasion", blank, _.id)

  implicit val eventHandler: EventHandler[Occasion] = new OccasionEventHandler
}
