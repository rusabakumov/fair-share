package account

case class AccountBalanceChanged(side: Side, balance: LabelledBalance, change: Money)
