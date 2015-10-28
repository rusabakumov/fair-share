package ledger

import ledger.operations.BasicAccountOperations
import ledger.tagging._

sealed trait Account extends Product with Serializable {
  def debitBalance: Money
  def creditBalance: Money
}

case class CashAccount(debitBalance: Money, creditBalance: Money) extends Account

object CashAccount {
  implicit val basicOperations: BasicAccountOperations[CashAccount] = BasicAccountOperations(
    (a, amount) => a.copy(debitBalance = a.debitBalance + amount),
    (a, amount) => a.copy(creditBalance = a.creditBalance + amount)
  )

  implicit val label: AccountLabel[CashAccount] = AccountLabel(Cash)
}

case class ReceivableAccount(debitBalance: Money, creditBalance: Money) extends Account

object ReceivableAccount {
  implicit val basicOperations: BasicAccountOperations[ReceivableAccount] = BasicAccountOperations(
    (a, amount) => a.copy(debitBalance = a.debitBalance + amount),
    (a, amount) => a.copy(creditBalance = a.creditBalance + amount)
  )

  implicit val label: AccountLabel[ReceivableAccount] = AccountLabel(Receivable)
}

case class GoodsAccount(debitBalance: Money, creditBalance: Money) extends Account

object GoodsAccount {
  implicit val basicOperations: BasicAccountOperations[GoodsAccount] = BasicAccountOperations(
    (a, amount) => a.copy(debitBalance = a.debitBalance + amount),
    (a, amount) => a.copy(creditBalance = a.creditBalance + amount)
  )

  implicit val label: AccountLabel[GoodsAccount] = AccountLabel(Goods)
}

case class PayableAccount(debitBalance: Money, creditBalance: Money) extends Account

object PayableAccount {
  implicit val basicOperations: BasicAccountOperations[PayableAccount] = BasicAccountOperations(
    (a, amount) => a.copy(debitBalance = a.debitBalance + amount),
    (a, amount) => a.copy(creditBalance = a.creditBalance + amount)
  )

  implicit val label: AccountLabel[PayableAccount] = AccountLabel(Payable)
}
