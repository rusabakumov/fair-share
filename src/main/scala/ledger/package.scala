package object ledger {
  type Money = BigDecimal

  object Money {
    def apply(v: Int) = BigDecimal(v)

    def apply(v: Double) = BigDecimal(v)
  }

}
