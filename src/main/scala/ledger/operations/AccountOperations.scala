package ledger.operations

import ledger._
import ledger.tagging.{Credit, Debit, Side, AccountLabel}

object AccountOperations {
  def debit[T <: Account](account: T, amount: Money)(implicit bo: BasicAccountOperations[T]): T =
    op(Debit, account, amount)

  def credit[T <: Account](account: T, amount: Money)(implicit bo: BasicAccountOperations[T]): T =
    op(Credit, account, amount)

  def increase[T <: Account](account: T)(amount: Money)(implicit bo: BasicAccountOperations[T], al: AccountLabel[T]): T =
    op(al.naturalSide, account, amount)

  def decrease[T <: Account](account: T)(amount: Money)(implicit bo: BasicAccountOperations[T], al: AccountLabel[T]): T =
    op(al.naturalContraSide, account, amount)

  def balance[T <: Account](account: T)(implicit al: AccountLabel[T]): Money = al.naturalSide match {
    case Debit => account.debitBalance - account.creditBalance
    case Credit => account.creditBalance - account.debitBalance
  }

  def op[T <: Account](side: Side, account: T, amount: Money)(implicit bo: BasicAccountOperations[T]): T = {
    bo.op(side, account, amount)
  }
}
