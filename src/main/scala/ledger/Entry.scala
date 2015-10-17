package ledger

import ledger.Account.{Goods, Cash, Payable, Receivable}
import ledger.Record.{Debit, Credit}

case class Entry(records: List[Record])

object Entry {
  def apply(records: Record*): Entry = apply(records.toList)

  def apply(paid: Money, consumed: Money): Entry = {
    val difference = paid - consumed

    val differenceRecord = difference match {
      case x if x >= 0 => Debit(Receivable, x)
      case x if x < 0 => Credit(Payable, x)
    }

    apply(
      Credit(Cash, paid),
      Debit(Goods, consumed),
      differenceRecord
    )
  }
}
