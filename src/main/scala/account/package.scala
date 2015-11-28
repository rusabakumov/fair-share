package object account {
  type Money = BigDecimal

  object Money {
    def apply(v: Int) = BigDecimal(v)

    def apply(v: Double) = BigDecimal(v)
  }

  case class Check(spent: Money, paid: Money)
}
