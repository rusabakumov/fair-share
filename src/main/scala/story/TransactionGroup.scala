package story

case class TransactionId(id: Int) extends AnyVal

case class TransactionGroup(
  id: TransactionId,
  eventId: EventId,
  transactions: Transaction
)
