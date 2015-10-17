package ledger

import ledger.Account.{Goods, Cash, Payable, Receivable}
import ledger.Record.{Debit, Credit}

case class Entry(records: List[Record])

object Entry {
  def apply(records: Record*): Entry = apply(records.toList)

  def pay(paid: Money, consumed: Money): Entry = {
    val difference = paid - consumed

    val differenceRecord = difference match {
      case x if x >= 0 => Debit(Receivable, x)
      case x if x < 0 => Credit(Payable, -x)
    }

    apply(
      Debit(Goods, consumed),
      differenceRecord,
      Credit(Cash, paid)
    )
  }

  def lend(amount: Money): Entry = {
    apply(Debit(Receivable, amount), Credit(Cash, amount))
  }

  def borrow(amount: Money): Entry = {
    apply(Debit(Cash, amount), Credit(Payable, amount))
  }

  def payback(amount: Money): Entry = {
    apply(Debit(Payable, amount), Credit(Cash, amount))
  }

  def receive(amount: Money): Entry = {
    apply(Debit(Cash, amount), Credit(Receivable, amount))
  }
}
