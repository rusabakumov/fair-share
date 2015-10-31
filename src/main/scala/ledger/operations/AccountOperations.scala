package ledger.operations

import ledger._

import scalaz.State

object AccountOperations {
  type AccountOp[T <: LabelledAccount] = State[T#Account, AccountChangeRecord]

  def op[T <: LabelledAccount](label: T)(side: Side, money: Money): AccountOp[T] = State { account =>
    (account.op(side, money), AccountChangeRecord(side, label, money))
  }

  def debit[T <: LabelledAccount](label: T)(money: Money): AccountOp[T] = op(label)(Debit, money)

  def credit[T <: LabelledAccount](label: T)(money: Money): AccountOp[T] = op(label)(Credit, money)

  def increase[T <: LabelledAccount](label: T)(money: Money): AccountOp[T] = op(label)(label.naturalSide, money)

  def decrease[T <: LabelledAccount](label: T)(money: Money): AccountOp[T] = op(label)(label.naturalSide.contra, money)
}
