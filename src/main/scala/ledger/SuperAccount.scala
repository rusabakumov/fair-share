package ledger

case class SuperAccount(
  cash: CashAccount.Account,
  receivable: ReceivableAccount.Account,
  payable: PayableAccount.Account,
  goods: GoodsAccount.Account
  )

object SuperAccount {
  def empty: SuperAccount = SuperAccount(
    CashAccount.empty,
    ReceivableAccount.empty,
    PayableAccount.empty,
    GoodsAccount.empty
  )
}
