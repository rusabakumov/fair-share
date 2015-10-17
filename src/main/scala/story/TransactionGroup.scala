package story

import ledger.Transaction

case class TransactionId(id: Int) extends AnyVal

case class TransactionGroup(
  id: TransactionId,
  eventId: EventId,
  transactions: Map[CharacterId, Transaction]
)
