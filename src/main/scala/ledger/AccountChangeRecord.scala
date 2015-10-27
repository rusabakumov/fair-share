package ledger

case class AccountChangeRecord(side: Side, label: Label, amount: Money)
