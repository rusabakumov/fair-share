package occasion

import account.AccountBalanceChanged
import participant.ParticipantId

trait OccasionEvent extends Product with Serializable

case class OccasionDescriptionChanged(id: OccasionId, description: String) extends OccasionEvent

case class OccasionLedgerChanged(id: OccasionId, ledgerChanges: Map[ParticipantId, List[AccountBalanceChanged]])
