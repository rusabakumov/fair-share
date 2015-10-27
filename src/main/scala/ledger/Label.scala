package ledger

sealed trait Label extends Product with Serializable

/**
 * Asset
 * Debit account representing the actual money of a person
 */
case object Cash extends Label

/**
 * Asset
 * Debit account representing the amount of money owned to a person
 */
case object Receivable extends Label

/**
 * Liability
 * Credit account representing the amount of money owned by a person
 */
case object Payable extends Label

/**
 * Equity
 * Debit account representing the value of goods consumed by a person
 */
case object Goods extends Label
