package ledger

case class AccountChangeRecord(side: Side, label: LabelledAccount, amount: Money)
