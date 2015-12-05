package object account {
  type Money = BigDecimal

  object Money {
    def apply(v: Int) = BigDecimal(v)

    def apply(v: Double) = BigDecimal(v)
  }

  case class Bill(spent: Money, paid: Money)
}
