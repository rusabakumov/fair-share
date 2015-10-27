package ledger.operations

import ledger._

import scalaz.State

object SuperAccountStateControl {
  def debit(label: Label, amount: Money): State[SuperAccount, AccountChangeRecord] = State { sa =>
    label match {
      case Cash =>
        val (record, account) = RecordableAccountOperations.debit(sa.cash)(amount)
        (sa.copy(cash = account), record)

      case Receivable =>
        val (record, account) = RecordableAccountOperations.debit(sa.receivable)(amount)
        (sa.copy(receivable = account), record)

      case Payable =>
        val (record, account) = RecordableAccountOperations.debit(sa.payable)(amount)
        (sa.copy(payable = account), record)

      case Goods =>
        val (record, account) = RecordableAccountOperations.debit(sa.goods)(amount)
        (sa.copy(goods = account), record)
    }
  }

  def credit(label: Label, amount: Money): State[SuperAccount, AccountChangeRecord] = State { sa =>
    label match {
      case Cash =>
        val (record, account) = RecordableAccountOperations.credit(sa.cash)(amount)
        (sa.copy(cash = account), record)

      case Receivable =>
        val (record, account) = RecordableAccountOperations.credit(sa.receivable)(amount)
        (sa.copy(receivable = account), record)

      case Payable =>
        val (record, account) = RecordableAccountOperations.credit(sa.payable)(amount)
        (sa.copy(payable = account), record)

      case Goods =>
        val (record, account) = RecordableAccountOperations.credit(sa.goods)(amount)
        (sa.copy(goods = account), record)
    }
  }

  def increase(label: Label, amount: Money): State[SuperAccount, AccountChangeRecord] = State { sa =>
    label match {
      case Cash =>
        val (record, account) = RecordableAccountOperations.increase(sa.cash)(amount)
        (sa.copy(cash = account), record)

      case Receivable =>
        val (record, account) = RecordableAccountOperations.increase(sa.receivable)(amount)
        (sa.copy(receivable = account), record)

      case Payable =>
        val (record, account) = RecordableAccountOperations.increase(sa.payable)(amount)
        (sa.copy(payable = account), record)

      case Goods =>
        val (record, account) = RecordableAccountOperations.increase(sa.goods)(amount)
        (sa.copy(goods = account), record)
    }
  }

  def decrease(label: Label, amount: Money): State[SuperAccount, AccountChangeRecord] = State { sa =>
    label match {
      case Cash =>
        val (record, account) = RecordableAccountOperations.decrease(sa.cash)(amount)
        (sa.copy(cash = account), record)

      case Receivable =>
        val (record, account) = RecordableAccountOperations.decrease(sa.receivable)(amount)
        (sa.copy(receivable = account), record)

      case Payable =>
        val (record, account) = RecordableAccountOperations.decrease(sa.payable)(amount)
        (sa.copy(payable = account), record)

      case Goods =>
        val (record, account) = RecordableAccountOperations.decrease(sa.goods)(amount)
        (sa.copy(goods = account), record)
    }
  }
}
