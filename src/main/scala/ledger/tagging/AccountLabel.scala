package ledger.tagging

trait AccountLabel[T] {
  def label: Label
  def naturalSide: Side = label.naturalSide
  def naturalContraSide: Side = label.naturalContraSide
}

object AccountLabel {
  def apply[T](aLabel: Label) = new AccountLabel[T] {
    def label: Label = aLabel
  }
}

