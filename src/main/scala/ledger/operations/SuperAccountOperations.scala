package ledger.operations

import ledger._

import scalaz.State

object SuperAccountOperations {
  type SuperAccountOp = State[SuperAccount, AccountChangeRecord]

  def lend(amount: Money): State[SuperAccount, List[AccountChangeRecord]] = for {
    rr <- increase(ReceivableAccount, amount)
    cr <- decrease(CashAccount, amount)
  } yield rr :: cr :: Nil

  def borrow(amount: Money): State[SuperAccount, List[AccountChangeRecord]] = for {
    cr <- increase(CashAccount, amount)
    pr <- increase(PayableAccount, amount)
  } yield cr :: pr :: Nil

  def increase(label: LabelledAccount, amount: Money): SuperAccountOp = op(label.naturalSide, label, amount)

  def op(side: Side, label: LabelledAccount, amount: Money): SuperAccountOp = State { sa =>
    label match {
      case x @ CashAccount =>
        val (account, change) = AccountOperations.op(x)(side, amount)(sa.cash)
        (sa.copy(cash = account), change)

      case x @ ReceivableAccount =>
        val (account, change) = AccountOperations.op(x)(side, amount)(sa.receivable)
        (sa.copy(receivable = account), change)

      case x @ PayableAccount =>
        val (account, change) = AccountOperations.op(x)(side, amount)(sa.payable)
        (sa.copy(payable = account), change)

      case x @ GoodsAccount =>
        val (account, change) = AccountOperations.op(x)(side, amount)(sa.goods)
        (sa.copy(goods = account), change)
    }
  }

  def payback(amount: Money): State[SuperAccount, List[AccountChangeRecord]] = for {
    pr <- decrease(PayableAccount, amount)
    cr <- decrease(CashAccount, amount)
  } yield pr :: cr :: Nil

  def receive(amount: Money): State[SuperAccount, List[AccountChangeRecord]] = for {
    cr <- increase(CashAccount, amount)
    rr <- decrease(ReceivableAccount, amount)
  } yield cr :: rr :: Nil

  def decrease(label: LabelledAccount, amount: Money): SuperAccountOp = op(label.naturalSide.contra, label, amount)

  def debit(label: LabelledAccount, amount: Money): SuperAccountOp = op(Debit, label, amount)

  def credit(label: LabelledAccount, amount: Money): SuperAccountOp = op(Credit, label, amount)
}
