package ledger.operations

import ledger._
import ledger.tagging.AccountSide

object AccountOperations {
  def debit[T : BasicAccountOperations](account: T)(amount: Money): T = implicitly[BasicAccountOperations[T]].debit(account)(amount)

  def credit[T : BasicAccountOperations](account: T)(amount: Money): T = implicitly[BasicAccountOperations[T]].credit(account)(amount)

  def op[T : BasicAccountOperations](side: Side)(account: T)(amount: Money): T = {
    val ops = implicitly[BasicAccountOperations[T]]
    ops.op(side, account, amount)
  }

  def increase[T](account: T)(amount: Money)(implicit bo: BasicAccountOperations[T], as: AccountSide[T]): T = as.side match {
    case Debit => bo.debit(account)(amount)
    case Credit => bo.credit(account)(amount)
  }

  def decrease[T](account: T)(amount: Money)(implicit bo: BasicAccountOperations[T], as: AccountSide[T]): T = as.side match {
    case Debit => bo.credit(account)(amount)
    case Credit => bo.debit(account)(amount)
  }
}
