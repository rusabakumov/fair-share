package story

import ledger.Transaction

case class TransactionId(id: Int) extends AnyVal

case class TransactionGroup(
  id: TransactionId,
  topicId: TopicId,
  transactions: Map[CharacterId, Transaction]
)
