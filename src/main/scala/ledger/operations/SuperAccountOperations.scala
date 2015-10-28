package ledger.operations

import ledger._
import ledger.tagging._

import scalaz.State

object SuperAccountOperations {
  type SuperAccountRecordedOperation = State[SuperAccount, List[AccountChangeRecord]]

  def debit(label: Label, amount: Money): SuperAccountRecordedOperation = op(Debit, label, amount)

  def credit(label: Label, amount: Money): SuperAccountRecordedOperation = op(Credit, label, amount)

  def increase(label: Label, amount: Money): SuperAccountRecordedOperation = op(label.naturalSide, label, amount)

  def decrease(label: Label, amount: Money): SuperAccountRecordedOperation = op(label.naturalContraSide, label, amount)

  def op(side: Side, label: Label, amount: Money): SuperAccountRecordedOperation = State { sa =>
    label match {
      case Cash =>
        val (record, account) = RecordableAccountOperations.op(side, sa.cash, amount)
        (sa.copy(cash = account), record :: Nil)

      case Receivable =>
        val (record, account) = RecordableAccountOperations.op(side, sa.receivable, amount)
        (sa.copy(receivable = account), record :: Nil)

      case Payable =>
        val (record, account) = RecordableAccountOperations.op(side, sa.payable, amount)
        (sa.copy(payable = account), record :: Nil)

      case Goods =>
        val (record, account) = RecordableAccountOperations.op(side, sa.goods, amount)
        (sa.copy(goods = account), record :: Nil)
    }
  }
}
