package ledger

sealed trait Record extends Product with Serializable

object Record {
  case class Debit(account: Account, amount: Money) extends Record
  case class Credit(account: Account, amount: Money) extends Record
}
