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

  def payback(amount: Money): State[SuperAccount, List[AccountChangeRecord]] = for {
    pr <- decrease(PayableAccount, amount)
    cr <- decrease(CashAccount, amount)
  } yield pr :: cr :: Nil


  def receive(amount: Money): State[SuperAccount, List[AccountChangeRecord]] = for {
    cr <- increase(CashAccount, amount)
    rr <- decrease(ReceivableAccount, amount)
  } yield cr :: rr :: Nil

  def debit(label: LabelledAccount, amount: Money): SuperAccountOp = op(Debit, label, amount)

  def credit(label: LabelledAccount, amount: Money): SuperAccountOp = op(Credit, label, amount)

  def increase(label: LabelledAccount, amount: Money): SuperAccountOp = op(label.naturalSide, label, amount)

  def decrease(label: LabelledAccount, amount: Money): SuperAccountOp = op(label.naturalSide.contra, label, amount)

  def op(side: Side, label: LabelledAccount, amount: Money): SuperAccountOp = label match {
    case x@CashAccount => adaptAccountOp(x, side, amount)(sa => sa.cash, (sa, a) => sa.copy(cash = a))
    case x@ReceivableAccount => adaptAccountOp(x, side, amount)(sa => sa.receivable, (s, a) => s.copy(receivable = a))
    case x@PayableAccount => adaptAccountOp(x, side, amount)(sa => sa.payable, (s, a) => s.copy(payable = a))
    case x@GoodsAccount => adaptAccountOp(x, side, amount)(sa => sa.goods, (s, a) => s.copy(goods = a))
  }

  def adaptAccountOp[T <: LabelledAccount](label: T, side: Side, money: Money)(
    f: SuperAccount => T#Account, g: (SuperAccount, T#Account) => SuperAccount
    ): State[SuperAccount, AccountChangeRecord] = {
    val op = AccountOperations.op(label)(side, money)
    State.get[SuperAccount] flatMap { sa => op.xmap[SuperAccount, SuperAccount](a => g(sa, a))(f) }
  }
}
