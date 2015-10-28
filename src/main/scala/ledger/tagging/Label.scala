package ledger.tagging

sealed trait Label extends Product with Serializable {
  def naturalSide: Side
  def naturalContraSide: Side = naturalSide.contra
}

/**
 * Asset
 * Debit account representing the actual money of a person
 */
case object Cash extends Label {
  def naturalSide: Side = Debit
}

/**
 * Asset
 * Debit account representing the amount of money owned to a person
 */
case object Receivable extends Label{
  def naturalSide: Side = Debit
}

/**
 * Liability
 * Credit account representing the amount of money owned by a person
 */
case object Payable extends Label {
  def naturalSide: Side = Credit
}

/**
 * Equity
 * Debit account representing the value of goods consumed by a person
 */
case object Goods extends Label {
  def naturalSide: Side = Debit
}
