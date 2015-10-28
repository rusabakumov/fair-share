package ledger

import ledger.tagging.{Payable, Receivable, Cash}

import scalaz.State

case class SuperAccount(
  cash: CashAccount,
  receivable: ReceivableAccount,
  payable: PayableAccount,
  goods: GoodsAccount)

object SuperAccount {
  import ledger.operations.SuperAccountOperations._

  def empty: SuperAccount = SuperAccount(
    CashAccount(0, 0),
    ReceivableAccount(0, 0),
    PayableAccount(0, 0),
    GoodsAccount(0, 0))


  def lend(amount: Money): SuperAccountRecordedOperation = for {
    rr <- increase(Receivable, amount)
    cr <- decrease(Cash, amount)
  } yield rr ::: cr

  def borrow(amount: Money): SuperAccountRecordedOperation = for {
    cr <- increase(Cash, amount)
    pr <- increase(Payable, amount)
  } yield cr ::: pr

  def payback(amount: Money): SuperAccountRecordedOperation = for {
    pr <- decrease(Payable, amount)
    cr <- decrease(Cash, amount)
  } yield pr ::: cr


  def receive(amount: Money): SuperAccountRecordedOperation = for {
    cr <- increase(Cash, amount)
    rr <- decrease(Receivable, amount)
  } yield cr ::: rr
}
