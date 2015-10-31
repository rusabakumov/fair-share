package ledger

sealed trait LabelledAccount extends Product with Serializable {
  outer =>

  def naturalSide: Side

  def empty = Account(0, 0)

  case class Account(debitBalance: Money, creditBalance: Money) {
    def debit(money: Money): Account = copy(debitBalance = debitBalance + money)

    def credit(money: Money): Account = copy(creditBalance = creditBalance + money)

    def op(side: Side, money: Money): Account = side match {
      case Debit => debit(money)
      case Credit => credit(money)
    }

    def increase(money: Money): Account = op(naturalSide, money)

    def decrease(money: Money): Account = op(naturalSide.contra, money)

    def balance: Money = naturalSide match {
      case Debit => debitBalance - creditBalance
      case Credit => creditBalance - debitBalance
    }
  }

}

sealed trait DebitAccount extends LabelledAccount {
  def naturalSide: Side = Debit
}

sealed trait CreditAccount extends LabelledAccount {
  def naturalSide: Side = Credit
}

case object CashAccount extends DebitAccount

case object ReceivableAccount extends DebitAccount

case object GoodsAccount extends DebitAccount

case object PayableAccount extends CreditAccount
