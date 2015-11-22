package account

trait AccountService {
  def run(events: List[AccountBalanceChanged]): Account => Account = a => {
    events.foldLeft(a)((a, event) => run(event)(a))
  }

  def lend(money: Money): List[AccountBalanceChanged] = List(
    AccountBalanceChanged(Debit, ReceivableBalance, money),
    AccountBalanceChanged(Credit, CashBalance, money)
  )

  def borrow(money: Money): List[AccountBalanceChanged] = List(
    AccountBalanceChanged(Debit, CashBalance, money),
    AccountBalanceChanged(Credit, PayableBalance, money)
  )

  def payback(money: Money): List[AccountBalanceChanged] = List(
    AccountBalanceChanged(Debit, PayableBalance, money),
    AccountBalanceChanged(Credit, CashBalance, money)
  )

  def receive(money: Money): List[AccountBalanceChanged] = List(
    AccountBalanceChanged(Debit, CashBalance, money),
    AccountBalanceChanged(Credit, ReceivableBalance, money)
  )

  def zeroIn(account: Account): List[AccountBalanceChanged] = List(
    account.cash.zeroIn,
    account.receivable.zeroIn,
    account.payable.zeroIn,
    account.goods.zeroIn
  )

  private def run(event: AccountBalanceChanged): Account => Account = a => event match {
    case AccountBalanceChanged(side, label, money) => a.op(side, label, money)
  }
}
