package ledger.tagging

import ledger.Label

trait AccountLabel[T] {
  def label: Label
}

object AccountLabel {
  def apply[T](aLabel: Label) = new AccountLabel[T] {
    def label: Label = aLabel
  }
}

