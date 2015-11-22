package account

sealed trait LabelledBalance extends Product with Serializable {
  outer =>

  def naturalSide: Side

  def empty = Balance(0)

  case class Balance(balance: Money) {
    def naturalSide: Side = outer.naturalSide

    def op(side: Side, money: Money): Balance = side match {
      case Debit => debit(money)
      case Credit => credit(money)
    }

    def zeroIn: AccountBalanceChanged = naturalSide match {
      case Debit =>
        if (balance > 0) AccountBalanceChanged(Credit, outer, balance) else AccountBalanceChanged(Debit, outer, balance)
      case Credit =>
        if (balance > 0) AccountBalanceChanged(Debit, outer, balance) else AccountBalanceChanged(Credit, outer, balance)
    }

    private def debit(money: Money): Balance = naturalSide match {
      case Debit => copy(balance = balance + money)
      case Credit => copy(balance = balance - money)
    }

    private def credit(money: Money): Balance = naturalSide match {
      case Credit => copy(balance = balance + money)
      case Debit => copy(balance = balance - money)
    }
  }
}

sealed trait DebitBalance extends LabelledBalance {
  def naturalSide: Side = Debit
}

sealed trait CreditBalance extends LabelledBalance {
  def naturalSide: Side = Credit
}

case object CashBalance extends DebitBalance

case object ReceivableBalance extends DebitBalance

case object GoodsBalance extends DebitBalance

case object PayableBalance extends CreditBalance
