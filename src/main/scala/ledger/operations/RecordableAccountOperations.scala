package ledger.operations

import ledger._
import ledger.tagging.{Credit, Debit, Side, AccountLabel}

object RecordableAccountOperations {
  def debit[T <: Account](account: T, amount: Money)
    (implicit bao: BasicAccountOperations[T], al: AccountLabel[T]): (AccountChangeRecord, T) = op(Debit, account, amount)

  def credit[T <: Account](account: T, amount: Money)
    (implicit bao: BasicAccountOperations[T], al: AccountLabel[T]): (AccountChangeRecord, T) = op(Credit, account, amount)

  def increase[T <: Account](account: T, amount: Money)
    (implicit bao: BasicAccountOperations[T], al: AccountLabel[T]): (AccountChangeRecord, T) = op(al.label.naturalSide, account, amount)

  def decrease[T <: Account](account: T, amount: Money)
    (implicit bao: BasicAccountOperations[T], al: AccountLabel[T]): (AccountChangeRecord, T) = op(al.label.naturalContraSide, account, amount)

  def op[T <: Account](side: Side, account: T, amount: Money)
    (implicit bao: BasicAccountOperations[T], al: AccountLabel[T]): (AccountChangeRecord, T) = {
    (AccountChangeRecord(side, al.label, amount), AccountOperations.op(side, account, amount))
  }
}
