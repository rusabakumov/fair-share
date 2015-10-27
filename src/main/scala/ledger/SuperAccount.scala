package ledger

import scalaz.State

case class SuperAccount(
  cash: CashAccount,
  receivable: ReceivableAccount,
  payable: PayableAccount,
  goods: GoodsAccount)

object SuperAccount {
  import ledger.operations.SuperAccountStateControl._

  type Transition = State[SuperAccount, List[AccountChangeRecord]]

  def lend(amount: Money): Transition = for {
    rr <- increase(Receivable, amount)
    cr <- decrease(Cash, amount)
  } yield rr :: cr :: Nil

  def borrow(amount: Money): Transition = for {
    cr <- increase(Cash, amount)
    pr <- increase(Payable, amount)
  } yield cr :: pr :: Nil

  def payback(amount: Money): Transition = for {
    pr <- decrease(Payable, amount)
    cr <- decrease(Cash, amount)
  } yield pr :: cr :: Nil


  def receive(amount: Money): Transition = for {
    cr <- increase(Cash, amount)
    rr <- decrease(Receivable, amount)
  } yield cr :: rr :: Nil
}
