package ledger.operations

import ledger._
import ledger.tagging.{AccountSide, AccountLabel}

object RecordableAccountOperations {
  def debit[T](account: T)(amount: Money)
    (implicit bao: BasicAccountOperations[T], as: AccountSide[T], al: AccountLabel[T]): (AccountChangeRecord, T) = {
    (AccountChangeRecord(Debit, al.label, amount), AccountOperations.debit(account)(amount))
  }

  def credit[T](account: T)(amount: Money)
    (implicit bao: BasicAccountOperations[T], as: AccountSide[T], al: AccountLabel[T]): (AccountChangeRecord, T) = {
    (AccountChangeRecord(Credit, al.label, amount), AccountOperations.credit(account)(amount))
  }

  def op[T](side: Side)(account: T)(amount: Money)
    (implicit bao: BasicAccountOperations[T], as: AccountSide[T], al: AccountLabel[T]): (AccountChangeRecord, T) = {
    (AccountChangeRecord(side, al.label, amount), AccountOperations.op(side)(account)(amount))
  }

  def increase[T](account: T)(amount: Money)
    (implicit bao: BasicAccountOperations[T], as: AccountSide[T], al: AccountLabel[T]): (AccountChangeRecord, T) = {
    (AccountChangeRecord(as.side, al.label, amount), AccountOperations.increase(account)(amount))
  }

  def decrease[T](account: T)(amount: Money)
    (implicit bao: BasicAccountOperations[T], as: AccountSide[T], al: AccountLabel[T]): (AccountChangeRecord, T) = {
    (AccountChangeRecord(as.contraSide, al.label, amount), AccountOperations.decrease(account)(amount))
  }
}
