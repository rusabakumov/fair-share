package ledger

sealed trait Account extends Product with Serializable

object Account {

  /**
   * Asset
   * Debit account representing the actual money of a person
   */
  case object Cash extends Account

  /**
   * Asset
   * Debit account representing the amount of money owned to a person
   */
  case object Receivable extends Account

  /**
   * Liability
   * Credit account representing the amount of money owned by a person
   */
  case object Payable extends Account

  /**
   * Equity
   * Debit account representing the value of goods consumed by a person
   */
  case object Goods extends Account
}
