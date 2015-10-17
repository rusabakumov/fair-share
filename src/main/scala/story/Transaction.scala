package story

import ledger.{Money, Entry}
import ledger.Record._
import ledger.Account._

case class Transaction(details: Map[CharacterId, Entry])

object Transaction {
  def apply(mapping: (CharacterId, Entry)*): Transaction = apply(Map(mapping:_*))

  def lend(lender: CharacterId, debtor: CharacterId, amount: Money): Transaction = {
    val lenderEntry = Entry(Credit(Cash, amount), Debit(Receivable, amount))
    val debtorEntry = Entry(Debit(Cash, amount), Credit(Payable, amount))

    Transaction(
      lender -> lenderEntry,
      debtor -> debtorEntry
    )
  }

  def repay(debtor: CharacterId, lender: CharacterId, amount: Money): Transaction = {
    val debtorTransaction = Entry(Credit(Cash, amount), Debit(Payable, amount))
    val lenderTransaction = Entry(Debit(Cash, amount), Credit(Receivable, amount))
    Transaction(
      debtor -> debtorTransaction,
      lender -> lenderTransaction
    )
  }

  def split(description: Map[CharacterId, (Money, Money)]): Transaction = Transaction(description.mapValues({
    case (paid, consumed) => Entry(paid, consumed)
  }))
}
