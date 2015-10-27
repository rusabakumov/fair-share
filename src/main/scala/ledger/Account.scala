package ledger

import ledger.operations.BasicAccountOperations
import ledger.tagging.{AccountSide, AccountLabel}

sealed trait Account extends Product with Serializable {
  self =>
  def debitBalance: Money
  def creditBalance: Money

  def balance(implicit as: AccountSide[self.type]) = as.side match {
    case Debit => debitBalance - creditBalance
    case Credit => creditBalance - debitBalance
  }
}

trait DebitAccount extends Account

object DebitAccount {
  implicit val debitAccount: AccountSide[DebitAccount] = AccountSide(Debit)
}

case class CashAccount(debitBalance: Money, creditBalance: Money) extends DebitAccount

object CashAccount {
  implicit val basicOperations: BasicAccountOperations[CashAccount] = BasicAccountOperations(
    (a, amount) => a.copy(debitBalance = a.debitBalance + amount),
    (a, amount) => a.copy(creditBalance = a.creditBalance + amount)
  )

  implicit val label: AccountLabel[CashAccount] = AccountLabel(Cash)
}

case class ReceivableAccount(debitBalance: Money, creditBalance: Money) extends DebitAccount

object ReceivableAccount {
  implicit val basicOperations: BasicAccountOperations[ReceivableAccount] = BasicAccountOperations(
    (a, amount) => a.copy(debitBalance = a.debitBalance + amount),
    (a, amount) => a.copy(creditBalance = a.creditBalance + amount)
  )

  implicit val label: AccountLabel[ReceivableAccount] = AccountLabel(Receivable)
}

case class GoodsAccount(debitBalance: Money, creditBalance: Money) extends DebitAccount

object GoodsAccount {
  implicit val basicOperations: BasicAccountOperations[GoodsAccount] = BasicAccountOperations(
    (a, amount) => a.copy(debitBalance = a.debitBalance + amount),
    (a, amount) => a.copy(creditBalance = a.creditBalance + amount)
  )

  implicit val label: AccountLabel[GoodsAccount] = AccountLabel(Goods)
}

trait CreditAccount extends Account

object CreditAccount {
  implicit val creditAccount: AccountSide[CreditAccount] = AccountSide(Credit)
}

case class PayableAccount(debitBalance: Money, creditBalance: Money) extends CreditAccount

object PayableAccount {
  implicit val basicOperations: BasicAccountOperations[PayableAccount] = BasicAccountOperations(
    (a, amount) => a.copy(debitBalance = a.debitBalance + amount),
    (a, amount) => a.copy(creditBalance = a.creditBalance + amount)
  )

  implicit val label: AccountLabel[PayableAccount] = AccountLabel(Payable)
}
