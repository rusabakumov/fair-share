package occasion

import account.Account
import participant.ParticipantId

case class OccasionId(id: Int) extends AnyVal

case class Occasion(
  id: OccasionId,
  description: Option[String],
  accounts: Map[ParticipantId, Account]
  )
