package account

case class Account(
  cash: CashBalance.Balance,
  receivable: ReceivableBalance.Balance,
  payable: PayableBalance.Balance,
  goods: GoodsBalance.Balance
) {
  def op(side: Side, label: LabelledBalance, money: Money): Account = label match {
    case x @ CashBalance =>
      copy(cash = cash.op(side, money))
    case x @ ReceivableBalance =>
      copy(receivable = receivable.op(side, money))
    case x @ PayableBalance =>
      copy(payable = payable.op(side, money))
    case x @ GoodsBalance =>
      copy(goods = goods.op(side, money))
  }

  def op(events: List[AccountBalanceChanged]): Account = {
    events.foldLeft(this)((a, event) => a.op(event))
  }

  def op(event: AccountBalanceChanged): Account = event match {
    case AccountBalanceChanged(side, label, money) => op(side, label, money)
  }
}

object Account {
  def empty: Account = Account(
    CashBalance.empty,
    ReceivableBalance.empty,
    PayableBalance.empty,
    GoodsBalance.empty
  )
}
