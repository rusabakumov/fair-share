package ledger.tagging

import ledger.{Credit, Debit, Side}

trait AccountSide[-T] {
  def side: Side

  def contraSide: Side = side match {
    case Debit => Credit
    case Credit => Debit
  }
}

object AccountSide {
  def apply[T](aSide: Side) = new AccountSide[T] {
    def side: Side = aSide
  }
}
