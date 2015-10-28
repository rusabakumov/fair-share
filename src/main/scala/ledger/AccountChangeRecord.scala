package ledger

import ledger.tagging.{Side, Label}

case class AccountChangeRecord(side: Side, label: Label, amount: Money)
